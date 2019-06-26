package com.mechanicalwood.PoetryAnalyse.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.mechanicalwood.PoetryAnalyse.Analyse.dao.AnalyzeDao;
import com.mechanicalwood.PoetryAnalyse.Analyse.dao.impl.AnalyzeDaoImpl;
import com.mechanicalwood.PoetryAnalyse.Analyse.service.AnalyzeService;
import com.mechanicalwood.PoetryAnalyse.Analyse.service.impl.AnalyzeServiceImpl;
import com.mechanicalwood.PoetryAnalyse.crawler.Crawler;
import com.mechanicalwood.PoetryAnalyse.crawler.common.Page;
import com.mechanicalwood.PoetryAnalyse.crawler.pipeline.ConsolePipeline;
import com.mechanicalwood.PoetryAnalyse.crawler.pipeline.DatabasePipeline;
import com.mechanicalwood.PoetryAnalyse.crawler.prase.DataPagePrase;
import com.mechanicalwood.PoetryAnalyse.crawler.prase.DocumentParse;
import com.mechanicalwood.PoetryAnalyse.web.WebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


/**
 * Author: MechanicalWood
 * Data: 2019/6/25 12:26
 */
public final class ObjectFactory {
    private static final ObjectFactory instance = new ObjectFactory();

    private final Logger LOGGER = LoggerFactory.getLogger(ObjectFactory.class);
    private final Map<Class, Object> objectHashMap = new HashMap<>();
    private ObjectFactory(){
        initConfigProperties();
        initDataSource();
        //爬虫对象
        initcrawler();

        //web对象
        initWebController();
        //打印对象清单
    }

    private void initWebController() {
        DataSource dataSource = getObjectMap(DataSource.class);
        AnalyzeDao analyzeDao = new AnalyzeDaoImpl(dataSource);
        AnalyzeService analyzeService = new AnalyzeServiceImpl(analyzeDao);
        WebController webController = new WebController(analyzeService);
        objectHashMap.put(WebController.class, webController);
    }

    private void initcrawler() {
        ConfigProperties configProperties = getObjectMap(ConfigProperties.class);
        DataSource dataSource = getObjectMap(DataSource.class);
        final Page page = new Page(
                configProperties.getCrawlerBase(),
                configProperties.getCrawlerPath(),
                configProperties.isCrawlerDetail()
        );
        Crawler crawler = new Crawler();
        crawler.addParsse(new DocumentParse());
        crawler.addParsse(new DataPagePrase());
        //控制是否打印到控制台
        if (configProperties.isEnableConsole()){
            crawler.addPipeline(new ConsolePipeline());
        }

        crawler.addPipeline(new DatabasePipeline(dataSource));
        crawler.addPage(page);

        objectHashMap.put(Crawler.class, crawler);
    }

    private void initDataSource() {
        //获取数据源
        ConfigProperties configProperties = getObjectMap(ConfigProperties.class);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(configProperties.getDbUsername());
        dataSource.setPassword(configProperties.getDbPassword());
        dataSource.setDriverClassName(configProperties.getDbDriverClass());
        dataSource.setUrl(configProperties.getDbUrl());

        objectHashMap.put(DataSource.class, dataSource);
    }

    private void initConfigProperties() {
        ConfigProperties configProperties = new ConfigProperties();
        objectHashMap.put(ConfigProperties.class, configProperties);

        LOGGER.info("ConfigProperties info:\n{}", configProperties.toString());
    }

    public <T> T getObjectMap(Class classz) {
        if (!objectHashMap.containsKey(classz)){
            throw new IllegalArgumentException("Class" + classz.getName() + "not foud Object");
        }
        return (T)objectHashMap.get(classz);
    }

    public static ObjectFactory getInstance(){
        return instance;
    }

    public void printObjectList(){
        LOGGER.info("========= ObjectFactory List =========");
        for (Map.Entry<Class, Object> entry : objectHashMap.entrySet()){
            LOGGER.info(String.format("\t[%s] ==> [%s]", entry.getKey().getCanonicalName(), entry.getValue().getClass().getCanonicalName()));
        }
        LOGGER.info("======================================");
    }
}

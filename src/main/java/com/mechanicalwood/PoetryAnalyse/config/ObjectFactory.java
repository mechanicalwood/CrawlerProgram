package com.mechanicalwood.PoetryAnalyse.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.mechanicalwood.PoetryAnalyse.crawler.Crawler;
import com.mechanicalwood.PoetryAnalyse.crawler.common.Page;
import com.mechanicalwood.PoetryAnalyse.crawler.pipeline.ConsolePipeline;
import com.mechanicalwood.PoetryAnalyse.crawler.pipeline.DatabasePipeline;
import com.mechanicalwood.PoetryAnalyse.crawler.prase.DataPagePrase;
import com.mechanicalwood.PoetryAnalyse.crawler.prase.DocumentParse;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: MechanicalWood
 * Data: 2019/6/25 12:26
 */
public final class ObjectFactory {
    private static final ObjectFactory instance = new ObjectFactory();
    private final Map<Class, Object> objectHashMap = new HashMap<>();
    private ObjectFactory(){
        initConfigProperties();
        initDataSource();
        //爬虫对象
        initcrawler();
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
        crawler.addPipeline(new ConsolePipeline());

        crawler.addPipeline(new DatabasePipeline(dataSource));
        crawler.addPage(page);

        objectHashMap.put(Crawler.class, crawler);
    }

    private void initDataSource() {
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
}

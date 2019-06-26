package com.mechanicalwood.PoetryAnalyse;

import com.alibaba.druid.pool.DruidDataSource;
import com.mechanicalwood.PoetryAnalyse.Analyse.dao.AnalyzeDao;
import com.mechanicalwood.PoetryAnalyse.Analyse.dao.impl.AnalyzeDaoImpl;
import com.mechanicalwood.PoetryAnalyse.Analyse.service.AnalyzeService;
import com.mechanicalwood.PoetryAnalyse.Analyse.service.impl.AnalyzeServiceImpl;
import com.mechanicalwood.PoetryAnalyse.config.ConfigProperties;
import com.mechanicalwood.PoetryAnalyse.config.ObjectFactory;
import com.mechanicalwood.PoetryAnalyse.crawler.Crawler;
import com.mechanicalwood.PoetryAnalyse.crawler.common.Page;
import com.mechanicalwood.PoetryAnalyse.crawler.pipeline.DatabasePipeline;
import com.mechanicalwood.PoetryAnalyse.crawler.prase.DataPagePrase;
import com.mechanicalwood.PoetryAnalyse.crawler.prase.DocumentParse;
import com.mechanicalwood.PoetryAnalyse.web.WebController;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import javax.sql.DataSource;
import static spark.route.HttpMethod.get;

/**
 * 唐诗分析的主类
 * Author: MechanicalWood
 * Data: 2019/3/10 18:25
 */
public class PoetryAnalyseApplication {
    public void code1(){
        //        System.out.println("hello world!");

        ConfigProperties configProperties = new ConfigProperties();
        final Page page = new Page(configProperties.getCrawlerBase(), configProperties.getCrawlerPath(), configProperties.isCrawlerDetail()
        );
        Crawler crawler = new Crawler();
        crawler.addParsse(new DocumentParse());
        crawler.addParsse(new DataPagePrase());

//        crawler.addPipeline(new ConsolePipeline());

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(configProperties.getDbUsername());
        dataSource.setPassword(configProperties.getDbPassword());
        dataSource.setDriverClassName(configProperties.getDbDriverClass());
        dataSource.setUrl(configProperties.getDbUrl());


        crawler.addPipeline(new DatabasePipeline(dataSource));

        crawler.addPage(page);
        crawler.start();

//        AnalyzeDao analyzeDao = new AnalyzeDaoImpl(dataSource);
//        System.out.println("test 1");
////        analyzeDao.analyzeAuthorCount().forEach(
////                authorCount -> {
////                    System.out.println(authorCount);
////                }
////        );
////        System.out.println("test 2");
////        analyzeDao.queryAllPoetry().forEach(
////                poetry -> {
////                    System.out.println(poetry);
////                }
////        );

//        AnalyzeService analyzeService = new AnalyzeServiceImpl(analyzeDao);
//        analyzeService.analyzeWordCloud().forEach(System.out::println);
    }

    /**
     * 用于从数据库查询每个作者创作数量的升序打印
     */
    public void code2(){
        ConfigProperties configProperties = new ConfigProperties();
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(configProperties.getDbUsername());
        dataSource.setPassword(configProperties.getDbPassword());
        dataSource.setDriverClassName(configProperties.getDbDriverClass());
        dataSource.setUrl(configProperties.getDbUrl());

        AnalyzeDao analyzeDao = new AnalyzeDaoImpl(dataSource);

        AnalyzeService analyzeService = new AnalyzeServiceImpl(analyzeDao);
        analyzeService.analyzeAuthorCount().forEach(System.out::println);
    }

    /**
     * 打印分词结果
     */
    public void code3(){
        ConfigProperties configProperties = new ConfigProperties();
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(configProperties.getDbUsername());
        dataSource.setPassword(configProperties.getDbPassword());
        dataSource.setDriverClassName(configProperties.getDbDriverClass());
        dataSource.setUrl(configProperties.getDbUrl());

        AnalyzeDao analyzeDao = new AnalyzeDaoImpl(dataSource);

        AnalyzeService analyzeService = new AnalyzeServiceImpl(analyzeDao);
        analyzeService.analyzeWordCloud().forEach(System.out::println);
    }

    /**
     * 用对象工厂启动爬虫
     */
    public void code4(){
        Crawler crawler = ObjectFactory.getInstance().getObjectMap(Crawler.class);
        crawler.start();
    }

    public void code5(){
        //动态web服务器（演示）
        Spark.get("/hello", (req, resp) -> {
            return "hello Spark Java";
        });
    }
    public static void main(String[] args) {
        WebController webController = ObjectFactory.getInstance().getObjectMap(WebController.class);
        //运行了wen服务，提供接口
        webController.launch();
    }
}

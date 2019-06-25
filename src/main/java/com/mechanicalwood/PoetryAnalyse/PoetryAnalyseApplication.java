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

import javax.sql.DataSource;

/**
 * 唐诗分析的主类
 * Author: MechanicalWood
 * Data: 2019/3/10 18:25
 */
public class PoetryAnalyseApplication {
    public void code1(){
        //        System.out.println("hello world!");

        ConfigProperties configProperties = new ConfigProperties();
//        final Page page = new Page(configProperties.getCrawlerBase(), configProperties.getCrawlerPath(), configProperties.isCrawlerDetail()
//        );
//        Crawler crawler = new Crawler();
//        crawler.addParsse(new DocumentParse());
//        crawler.addParsse(new DataPagePrase());

//        crawler.addPipeline(new ConsolePipeline());

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(configProperties.getDbUsername());
        dataSource.setPassword(configProperties.getDbPassword());
        dataSource.setDriverClassName(configProperties.getDbDriverClass());
        dataSource.setUrl(configProperties.getDbUrl());


//        crawler.addPipeline(new DatabasePipeline(dataSource));
//
//        crawler.addPage(page);
//        crawler.start();

        AnalyzeDao analyzeDao = new AnalyzeDaoImpl(dataSource);
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

        AnalyzeService analyzeService = new AnalyzeServiceImpl(analyzeDao);
        analyzeService.analyzeWordCloud().forEach(System.out::println);
    }
    public static void main(String[] args) {
        Crawler crawler = ObjectFactory.getInstance().getObjectMap(Crawler.class);
        crawler.start();
    }
}

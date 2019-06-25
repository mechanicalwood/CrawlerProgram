package com.mechanicalwood.PoetryAnalyse.config;
import lombok.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Author: MechanicalWood
 * Data: 2019/5/14 10:25
 */
@Data
public class ConfigProperties {

    private String crawlerBase;
    private String crawlerPath;
    private boolean crawlerDetail;

    private String dbUsername;
    private String dbPassword;
    private String dbUrl;
    private String dbDriverClass;

    public ConfigProperties(){
        //从外部文件加载
        InputStream inputStream = ConfigProperties.class.getClassLoader().getResourceAsStream("config.properties");
        Properties p = new Properties();
        try {
            p.load(inputStream);
            System.out.println(p);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.crawlerBase = String.valueOf(p.get("crawler.base"));
        this.crawlerPath = String.valueOf(p.get("crawler.path"));
        this.crawlerDetail = Boolean.parseBoolean(
                String.valueOf(p.get("crawler.detail"))
        );
        this.dbUsername = String.valueOf(p.get("db.username"));
        this.dbPassword = String.valueOf(p.get("db.password"));
        this.dbUrl = String.valueOf(p.get("db.url"));
        this.dbDriverClass = String.valueOf(p.get("db.driver_class"));


    }

//    public static void main(String[] args) {
//        new ConfigProperties();
//    }
}

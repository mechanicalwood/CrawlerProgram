package com.mechanicalwood.PoetryAnalyse.crawler.pipeline;

import com.mechanicalwood.PoetryAnalyse.crawler.common.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Author: MechanicalWood
 * Data: 2019/4/26 21:40
 */
public class DatabasePipeline implements Pipeline {
    private final Logger LOGGER = LoggerFactory.getLogger(DatabasePipeline.class);

    //数据源，通过构造方法传过来的
    private final DataSource dataSource;

    public DatabasePipeline(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void pipeline(Page page) {
        //避免重复
        //Poetry poetry = (Poetry) page.getDataSet().getData("poetry");

        //Poetry poetry = new Poetry();

        String title = (String) page.getDataSet().getData("title");
        String dynasty = (String) page.getDataSet().getData("dynasty");
        String author = (String) page.getDataSet().getData("author");
        String content = (String) page.getDataSet().getData("content");


//        poetry.setTitle((String) page.getDataSet().getData("title"));
//        poetry.setTitle((String) page.getDataSet().getData("dynasty"));
//        poetry.setTitle((String) page.getDataSet().getData("author"));
//        poetry.setTitle((String) page.getDataSet().getData("content"));



        //System.out.println("存储到数据库: " + poetry);
        String sql = "insert into poetry_info (title, dynasty, author, content) values (?, ?, ?, ?)";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
                ){

            statement.setString(1, title);
            statement.setString(2, dynasty);
            statement.setString(3, author);
            statement.setString(4, content);

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Database insert occur exception {}.", e.getMessage());
        }
    }
}

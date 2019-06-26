package com.mechanicalwood.PoetryAnalyse.web;

import com.google.gson.Gson;
import com.mechanicalwood.PoetryAnalyse.Analyse.model.AuthorCount;
import com.mechanicalwood.PoetryAnalyse.Analyse.model.WordCount;
import com.mechanicalwood.PoetryAnalyse.Analyse.service.AnalyzeService;
import spark.ResponseTransformer;
import spark.Spark;

import javax.xml.ws.Response;
import java.util.List;

/**
 * Author: MechanicalWood
 * Data: 2019/6/26 10:22
 */
public class WebController {
    private final AnalyzeService analyzeService;

    public WebController(AnalyzeService analyzeService) {
        this.analyzeService = analyzeService;
    }


    public List<AuthorCount> analyzeAuthorCount(){
        return analyzeService.analyzeAuthorCount();
    }

    public List<WordCount> analyzeWordCloud(){
        return analyzeService.analyzeWordCloud();
    }

    //将对象变成字符串（Web接口）
    public void launch(){
        ResponseTransformer transformer = new JSONResponseTreansformer();
        //src/main/resources/static
        Spark.staticFileLocation("/static");
        Spark.get("/analyze/author_count", ((request, response) -> analyzeAuthorCount()), transformer);
        Spark.get("/analyze/word_cloud", ((request, response) -> analyzeWordCloud()), transformer);
    }


    public static class JSONResponseTreansformer implements ResponseTransformer{
        Gson gson = new Gson();

        @Override
        public String render(Object o) throws Exception {
            return gson.toJson(o);
        }
    }

    public static void main(String[] args) {
        //javascript对象要用到字符串，所以要进行转换
        Gson gson = new Gson();

        WordCount wordCount = new WordCount();
        wordCount.setWord("java");
        wordCount.setCount(10);

        System.out.println(gson.toJson(wordCount));

        String str = "{\"word\":\"java\",\"count\":10}";
        WordCount wordCount1 = gson.fromJson(str, WordCount.class);
        System.out.println(wordCount1);
    }
}

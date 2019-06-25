package com.mechanicalwood.PoetryAnalyse.crawler.prase;

import com.gargoylesoftware.htmlunit.html.*;
import com.mechanicalwood.PoetryAnalyse.crawler.common.Page;



/**
 * Author: MechanicalWood
 * Data: 2019/3/17 17:13
 */
public class DataPagePrase implements Parse{
    @Override
    public void parse(Page page) {
        if (!page.isDetail()){
            return;
        }
//        HtmlDivision domElement = (HtmlDivision) page.getHtmlPage().getElementById("contsond3ba5a3a4d29");//向下强转
//        String divContent = domElement.asText();
//        page.getDataSet().putData("正文", divContent);
        //System.out.println(divContent);

        HtmlPage htmlPage = page.getHtmlPage();
        HtmlElement body = htmlPage.getBody();
        //标题
        String titlePath = "//div[@class='cont']/h1/text()";
        DomText titleDom = (DomText) body.getByXPath(titlePath).get(0);
        String title = titleDom.asText();

        //作者


        String authorPath = "//div[@class='cont']/p/a[2]";
        HtmlAnchor authorDom = (HtmlAnchor) body.getByXPath(authorPath).get(0);
        String author = authorDom.asText();

        //朝代
        String dynastyPath= "//div[@class='cont']/p/a[1]";
        HtmlAnchor dynastyDom = (HtmlAnchor) body.getByXPath(dynastyPath).get(0);
        String dynasty = dynastyDom.asText();



        //正文
        String contentPath = "//div[@class='cont']/div[@class='contson']";
        HtmlDivision contentDom = (HtmlDivision) body.getByXPath(contentPath).get(0);
        String content = contentDom.asText();



//        Poetry poetry = new Poetry();
//        poetry.setAuthor(author);
//        poetry.setContent(content);
//        poetry.setDynasty(dynasty);
//        poetry.setTitle(title);
//        page.getDataSet().putData("poetry", poetry);



        //方便添加更多的数据，上面的代码在解析完成后就构建了一个对象
        page.getDataSet().putData("title", title);
        page.getDataSet().putData("dynasty", dynasty);
        page.getDataSet().putData("author", author);
        page.getDataSet().putData("content", content);

        page.getDataSet().putData("url", page.getUrl());


    }
}

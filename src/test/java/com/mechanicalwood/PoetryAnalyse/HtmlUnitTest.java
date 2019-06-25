package com.mechanicalwood.PoetryAnalyse;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.mechanicalwood.PoetryAnalyse.crawler.Crawler;

import com.sun.xml.internal.ws.wsdl.writer.document.soap.Body;

import java.io.IOException;

/**
 * Author: MechanicalWood
 * Data: 2019/4/24 21:44
 */
public class HtmlUnitTest {
    public void code1(){

    }
    public static void main(String[] args) {
        try(WebClient webClient = new WebClient(BrowserVersion.CHROME)){
            try {
                //禁用执行js文件
                webClient.getOptions().setJavaScriptEnabled(false);
                HtmlPage htmlPage = webClient.getPage("https://so.gushiwen.org/shiwenv_eeb217f8cb2d.aspx");
                //                HtmlElement bodyElement = htmlPage.getBody();
//                String s = bodyElement.asXml();//asXml()连带结构一并取出
//                System.out.println(s);

//                DomElement domElement = htmlPage.getElementById("contsonecde26b7f368");
//                System.out.println(domElement.getClass().getName());//com.gargoylesoftware.htmlunit.html.HtmlDivision

//                HtmlDivision domElement = (HtmlDivision) htmlPage.getElementById("contson5d405a1d5b64");//向下强转
//                String divContent = domElement.asText();
//                System.out.println(divContent);


                HtmlElement body = htmlPage.getBody();
                //标题
                //  /html/body/div[3]/div[1]/div[2]/div[1]
                //  document.querySelector('body > div.main3 > div.left > div:nth-child(2) > div.cont')
                String titlePath = "//div[@class='cont']/h1/text()";
                DomText titleDom = (DomText) body.getByXPath(titlePath).get(0);
                String title = titleDom.asText();

                //作者


                String authorPath = "//div[@class='cont']/p/a[2]";
//                body.getByXPath(authorPath).forEach(o ->{
//                    HtmlAnchor anchor = (HtmlAnchor)o;
//                    System.out.println(anchor.asText());
//                });
                HtmlAnchor authorDom = (HtmlAnchor) body.getByXPath(authorPath).get(0);
                String author = authorDom.asText();

                //朝代
                String dynastyPath= "//div[@class='cont']/p/a[1]";
                HtmlAnchor dynastyDom = (HtmlAnchor) body.getByXPath(dynastyPath).get(0);
                String dynasty = dynastyDom.asText();



                //正文
                String contentPath = "//div[@class='cont']/div[@class='contson']";
//                body.getByXPath(contentPath)
//                        .forEach(o -> {
//                            HtmlDivision division = (HtmlDivision) o;
//                            System.out.println(division.asText());
//                            //System.out.println(o.getClass().getName());
//                        });
                HtmlDivision contentDom = (HtmlDivision) body.getByXPath(contentPath).get(0);
                String content = contentDom.asText();

                //DomText titleDom = (DomText) body.getByXPath(titlePath).get(0);

                //System.out.println(titleDom.asText());
//                DomText titleDom = (DomText) body.getByXPath(titlePath).get(0);
//                System.out.println(titleDom.asText());


//                Poetry poetry = new Poetry();
//                poetry.setAuthor(author);
//                poetry.setContent(content);
//                poetry.setDynasty(dynasty);
//                poetry.setTitle(title);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.mechanicalwood.PoetryAnalyse.crawler.prase;

import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.mechanicalwood.PoetryAnalyse.crawler.common.Page;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Author: MechanicalWood
 * Data: 2019/3/17 16:57
 */
public class DocumentParse implements Parse {
    @Override
    public void parse(final Page page) {
        if (page.isDetail()) {
            return;

        }
//        final AtomicInteger atomicInteger = new AtomicInteger(0);
        HtmlPage htmlPage = page.getHtmlPage();

        htmlPage.getBody()
                .getElementsByAttribute("div",
                        "class",
                        "typecont")
                .forEach(htmlElement -> {
                    //System.out.println(htmlElement.asXml());
                    DomNodeList<HtmlElement> nodeList = htmlElement.getElementsByTagName("a");
                    nodeList.forEach(
                            aNode -> {
                                String path = aNode.getAttribute("href");
//                                    atomicInteger.getAndIncrement();
//                                    System.out.println(path);
                                Page subPage = new Page(
                                        page.getBase(),
                                        path,
                                        true
                                );
                                page.getSubPage().add(subPage);
                            }
                    );
                });
//        System.out.println("总共解析了" + atomicInteger.get() + "个地址");
    }

/*    @Override
    public void parse(Page page) {
        if (page.isDetail()) {
            return;

        }

        page.getHtmlPage()
                .getBody()
                .getElementsByAttribute("div", "class", "typecont")
                .forEach(div -> {

                    DomNodeList<HtmlElement> aNodeList = div.getElementsByTagName("a");

                    aNodeList.forEach(
                            aNode -> {
                                String path = aNode.getAttribute("herf");
                                Page subPage = new Page(page.getBase(), path, true);
                                page.getSubPage().add(subPage);
                            }
                    );
                });


    }*/

}

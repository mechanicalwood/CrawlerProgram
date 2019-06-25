package com.mechanicalwood.PoetryAnalyse.crawler.common;


import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.*;


import java.util.HashSet;
import java.util.Set;

/**
 * Author: MechanicalWood
 * Data: 2019/3/17 16:39
 */
@Data


public class Page {

    private final String base;//数据网站根地址

    private final String path;//具体的网页路径

    private HtmlPage htmlPage;//网页的DOM对象

    private final boolean detail;//标识网页是否是详情页

    private Set<Page> subPage = new HashSet<>();//子页面对象集合

    /**

     * 数据对象
     * @return
     * 不用map而定义一个数据集合的好处在于，避免在代码中全程引入一个HashMap对象
     */
    private DataSet dataSet = new DataSet();


    public String getUrl() {
        return this.base + this.path;
    }
}

package com.mechanicalwood.PoetryAnalyse.crawler.pipeline;

import com.mechanicalwood.PoetryAnalyse.crawler.common.Page;


import java.util.Map;

/**
 * Author: MechanicalWood
 * Data: 2019/4/24 22:39
 */
public class ConsolePipeline implements Pipeline {
    @Override
    public void pipeline(Page page) {
        Map<String, Object> data = page.getDataSet().getData();

        //存储数据
        System.out.println(data);
    }
}

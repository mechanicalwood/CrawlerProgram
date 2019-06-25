package com.mechanicalwood.PoetryAnalyse.crawler.pipeline;


import com.mechanicalwood.PoetryAnalyse.crawler.common.Page;

/**
 * Author: MechanicalWood
 * Data: 2019/4/24 22:21
 */
public interface Pipeline {
    /**
     *
     * @param page
     */
    void pipeline(Page page);
}

package com.mechanicalwood.PoetryAnalyse.Analyse.service;

import com.mechanicalwood.PoetryAnalyse.Analyse.model.AuthorCount;
import com.mechanicalwood.PoetryAnalyse.Analyse.model.WordCount;

import java.util.List;

/**
 * Author: MechanicalWood
 * Data: 2019/5/14 12:17
 */
public interface AnalyzeService {

    /**
     * 分析作者创作数量
     * @return
     */

    List<AuthorCount> analyzeAuthorCount();

    /**
     * 分析词云
     * @return
     */
    List<WordCount> analyzeWordCloud();
}

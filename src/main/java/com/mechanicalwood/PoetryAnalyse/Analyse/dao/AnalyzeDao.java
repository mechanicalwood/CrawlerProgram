package com.mechanicalwood.PoetryAnalyse.Analyse.dao;

import com.mechanicalwood.PoetryAnalyse.Analyse.entity.Poetry;
import com.mechanicalwood.PoetryAnalyse.Analyse.model.AuthorCount;

import java.util.List;

/**
 * Author: MechanicalWood
 * Data: 2019/5/14 11:14
 */
public interface AnalyzeDao {
    /**
     * 分析唐诗中作者的创作数量
     * @return
     */
    List<AuthorCount> analyzeAuthorCount();

    /**
     * 查询所有诗文提供给业务层分析
     * @return
     */
    List<Poetry> queryAllPoetry();
}

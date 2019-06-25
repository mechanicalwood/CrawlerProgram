package com.mechanicalwood.PoetryAnalyse.crawler.common;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储清洗的数据
 * Author: MechanicalWood
 * Data: 2019/3/19 16:39
 */
@ToString
public class DataSet {

    /**
     * data是把DOM解析、清洗之后存储的数据
     * 以下格式：
     * 标题：
     * 作者：
     * 正文：
     */

    private Map<String, Object> data = new HashMap<>();

    public void putData(String key, Object value){
        this.data.put(key, value);
    }

    public Object getData(String key){
        return this.data.get(key);
    }

    public Map<String, Object> getData(){
        //return this.data;  //如果返回原有的data，会不安全
        return new HashMap<>(this.data);
    }
}

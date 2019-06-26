package com.mechanicalwood.PoetryAnalyse.Analyse.entity;
import lombok.Data;
/**
 * Author: MechanicalWood
 * Data: 2019/4/26 19:33
 */
@Data
public class Poetry {
    /**
     * 标题、作者、朝代、正文
     */
    private String title;
    private String dynasty;
    private String author;
    private String content;
}

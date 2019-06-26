package com.mechanicalwood.PoetryAnalyse.Analyse.service.impl;

import com.mechanicalwood.PoetryAnalyse.Analyse.dao.AnalyzeDao;
import com.mechanicalwood.PoetryAnalyse.Analyse.entity.Poetry;
import com.mechanicalwood.PoetryAnalyse.Analyse.model.AuthorCount;
import com.mechanicalwood.PoetryAnalyse.Analyse.model.WordCount;
import com.mechanicalwood.PoetryAnalyse.Analyse.service.AnalyzeService;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.util.*;


/**
 * Author: MechanicalWood
 * Data: 2019/5/14 12:22
 */
public class AnalyzeServiceImpl implements AnalyzeService {

    private final AnalyzeDao analyzeDao;

    public AnalyzeServiceImpl(AnalyzeDao analyzeDao) {
        this.analyzeDao = analyzeDao;
    }

    @Override
    public List<AuthorCount> analyzeAuthorCount() {
        /**
         * 此处结果可进行排序
         * 1）DAO层SQL排序
         * 2）Service层进行数据排序
         */
        //return analyzeDao.analyzeAuthorCount();

        /**
         * 业务层排序
         */
        List<AuthorCount> authorCounts = analyzeDao.analyzeAuthorCount();

        Collections.sort(authorCounts, new Comparator<AuthorCount>() {
            @Override
            public int compare(AuthorCount o1, AuthorCount o2) {
                return o1.getCount() - o2.getCount();
            }
        });
        return authorCounts;
    }

    @Override
    public List<WordCount> analyzeWordCloud() {
        //1）查询所有数据
        //2）取出title content
        //3）分词
        //4）统计 k-词 v-词频

        Map<String, Integer> map = new HashMap<>();
        List<Poetry> poetries = analyzeDao.queryAllPoetry();

        for (Poetry poetry : poetries){
            List<Term> terms = new ArrayList<>();
            String title = poetry.getTitle();
            String content = poetry.getContent();
            terms.addAll(NlpAnalysis.parse(title).getTerms());
            terms.addAll(NlpAnalysis.parse(content).getTerms());
            //ArrayList是并发进行修改的，不能用for循环就行过滤和删除，所以需要使用迭代器
            Iterator<Term> iterator = terms.iterator();
            while (iterator.hasNext()){
                Term term = iterator.next();
                //词性过滤
                if (term.getNatureStr() == null || term.getNatureStr().equals("w")){
                    iterator.remove();
                    //迭代器移除后要进行continue
                    continue;
                }
                //长度过滤
                if (term.getName().length() < 2){
                    iterator.remove();
                    continue;
                }
                //统计
                String realName = term.getRealName();
                Integer count = 0;
                if (map.containsKey(realName)){
                    count = map.get(realName) + 1;
                }else {
                    count = 1;
                }
                map.put(realName, count);
            }
        }
        //将Map转化为List
        List<WordCount> wordCounts = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()){
            WordCount wordCount = new WordCount();
            wordCount.setCount(entry.getValue());
            wordCount.setWord(entry.getKey());
            //添加到集合中去
            wordCounts.add(wordCount);
        }
        return wordCounts;
    }

//    public static void main(String[] args) {
//        Result result = NlpAnalysis.parse("我是一个人，一个大好人");
//        List<Term> terms = result.getTerms();
//        for (Term term : terms){
//            System.out.println(term);
//        }
//
//    }
}

package com.mechanicalwood.PoetryAnalyse.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.mechanicalwood.PoetryAnalyse.crawler.common.Page;
import com.mechanicalwood.PoetryAnalyse.crawler.pipeline.ConsolePipeline;
import com.mechanicalwood.PoetryAnalyse.crawler.pipeline.Pipeline;
import com.mechanicalwood.PoetryAnalyse.crawler.prase.DataPagePrase;
import com.mechanicalwood.PoetryAnalyse.crawler.prase.DocumentParse;
import com.mechanicalwood.PoetryAnalyse.crawler.prase.Parse;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: MechanicalWood
 * Data: 2019/3/17 16:38
 */
public class Crawler {
    /**
     * 放置文档页面
     */
    private final Queue<Page> docQueue = new LinkedBlockingQueue<>();

    /**
     * 放置详情页面(处理完成，数据在dataSet中)
     */
    private final Queue<Page> detailQueue = new LinkedBlockingQueue<>();
    /**
     * 采集器
     */
    private final WebClient webClient;
    /**
     * 所有的解析器
     */
    private final List<Parse> parseList = new LinkedList<>();
    /**
     * 所有的清洗器
     */
    private final List<Pipeline> pipelineList = new LinkedList<>();
    /**
     * 线程调度器
     */
    private final ExecutorService executorService;

    public Crawler(){
        this.webClient = new WebClient();
        this.webClient.getOptions().setJavaScriptEnabled(false);
        //线程执行器
        this.executorService = Executors.newFixedThreadPool(8, new ThreadFactory() {
            private final AtomicInteger id = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("Crawler-Thread-" + id.getAndIncrement());
                return thread;
            }
        });
    }


    public void start(){
        /**
         * 爬取
         * 解析
         * 清洗
         */
//        this.parse();
//        this.pipeline();
        this.executorService.submit(new Runnable() {
            @Override
            public void run() {
                parse();
            }
        });

        this.executorService.submit(new Runnable() {
            @Override
            public void run() {
                pipeline();
            }
        });

    }

    private void parse(){
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final Page page = this.docQueue.poll();
            if (page == null){
                continue;
            }
            //未采集只有base path detail，采集后又hemlpage
            this.executorService.submit(new Runnable() {
                @Override
                public void run() {

                    try {
                        //采集
                        HtmlPage htmlPage = Crawler.this.webClient.getPage(page.getUrl());
                        page.setHtmlPage(htmlPage);

                        for (Parse parse : Crawler.this.parseList){
                            parse.parse(page);
                        }



                        if (page.isDetail()){
                            Crawler.this.detailQueue.add(page);

                        }else{
                            Iterator<Page> iterator = page.getSubPage().iterator();
                            while (iterator.hasNext()){
//                    this.detailQueue.add(iterator.next());

                                Page subPage = iterator.next();
                                Crawler.this.docQueue.add(subPage);
                                //System.out.println(page);

                                iterator.remove();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


        }
    }

    private void pipeline(){
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final Page page = this.detailQueue.poll();
            if (page == null){
                continue;
            }
            this.executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (Pipeline pipeline : Crawler.this.pipelineList){
                        pipeline.pipeline(page);
                    }
                }
            });



        }
    }

    public void addPage(Page page){
        this.docQueue.add(page);
    }
    public void addParsse(Parse parse){
        this.parseList.add(parse);
    }
    public void addPipeline(Pipeline pipeline){
        this.pipelineList.add(pipeline);
    }

    /**
     * 停止爬虫
     */
    public void stop(){
        if (this.executorService != null && !this.executorService.isShutdown()){
            this.executorService.shutdown();
        }
    }


    public static void main(String[] args) throws IOException {
        final Page page = new Page("https://so.gushiwen.org", "/gushi/tangshi.aspx", false
        );
        Crawler crawler = new Crawler();
        crawler.addParsse(new DocumentParse());
        crawler.addParsse(new DataPagePrase());
        crawler.addPipeline(new ConsolePipeline());
        crawler.addPage(page);
        crawler.start();

//        page.setBase("https://so.gushiwen.org");
//        //详情页面测试
//        page.setPath("/shiwenv_45c396367f59.aspx");
//        page.setDetail(true);
//
//
//        //非详情页面测试
//        page.setPath("/gushi/tangshi.aspx");
//        page.setDetail(false);

//        WebClient webClient = new WebClient(BrowserVersion.CHROME);
//        webClient.getOptions().setJavaScriptEnabled(false);
//        HtmlPage htmlPage = webClient.getPage(page.getUrl());
//        page.setHtmlPage(htmlPage);


//        Queue<Parse> detailPageList = new LinkedBlockingQueue<>();

//        List<Parse> parseList = new LinkedList<>();
//        parseList.add(new DocumentParse());
//        parseList.add(new DataPagePrase());
//
//        parseList.forEach(parse -> {
//            parse.parse(page);
//            if (!page.isDetail()){
//                page.getSubPage();
//            }
//        });
//        System.out.println(page.getSubPage());
//
//        List<Pipeline> pipelineList = new LinkedList<>();
//        pipelineList.add(new ConsolePipeline());
//
//        Parse parse = new DataPagePrase();
//        parse.parse(page);


//        Pipeline pipeline = new ConsolePipeline();
//        pipeline.pipeline(page);

//        Parse parse = new DocumentParse();
//        parse.parse(page);

    }
}

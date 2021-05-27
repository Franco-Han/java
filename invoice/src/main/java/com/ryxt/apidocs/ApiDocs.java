package com.ryxt.apidocs;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import io.github.yedaxia.apidocs.plugin.markdown.MarkdownDocPlugin;

/**
* @Description: 自动生成接口文档
* @Author: uenpeng
* @Date: 2020/11/3
*/
public class ApiDocs {
    public static  void main(String[] args)
    {
        //开始API的初始化工作
        DocsConfig config= new DocsConfig();
        // 项目根目录
        config.setProjectPath("E:\\work\\项目资料\\赵毅\\发票机器人\\项目\\invoice");
        // 项目名称
        config.setProjectName("RYXT-发票自动查验系统");
        // 声明该API的版本
        config.setApiVersion("V1.0");
        // 生成API 文档所在目录
        config.setDocsPath("E:\\work\\项目资料\\赵毅\\发票机器人\\项目\\invoice\\src\\main\\resources\\docs");
        // 配置自动生成
        config.setAutoGenerate(Boolean.TRUE);

        config.addPlugin(new MarkdownDocPlugin());
        // 执行生成文档
        Docs.buildHtmlDocs(config);

    }

}

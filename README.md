# dataService
![](https://gitee.com/ZhuGeZiFang/dataService/badge/star.svg)
![](https://gitee.com/ZhuGeZiFang/dataService/badge/fork.svg?theme=gvp)
![](https://img.shields.io/github/stars/zhugezifang/dataService.svg?logo=GitHub)
![](https://img.shields.io/github/forks/zhugezifang/dataService.svg?logo=GitHub)
![](https://img.shields.io/github/watchers/zhugezifang/dataService.svg?logo=GitHub)
![](https://img.shields.io/github/license/zhugezifang/dataService.svg)
![](https://img.shields.io/github/v/release/zhugezifang/dataService?label=latest&style=flat-square)

[![EN doc](https://img.shields.io/badge/document-English-blue.svg)](README.md)
[![CN doc](https://img.shields.io/badge/文档-中文版-blue.svg)](README-CN.md)

#### 介绍
dataService 旨在提供全面的数据服务及共享能力，统一管理面向内外部的API服务。 

能够将数据表快速生成数据API，或将已有API快速注册至本平台进行统一管理与发布。 

dataService可实现API服务一键发布，实现低成本、易上手、安全稳定的数据共享与开放，接口开发者只需关注API本身查询逻辑

#### 功能介绍
(1)低代码配置，只需要写少量sql 即可实现api服务

(2)实现如下三个统一：

a.数据服务统一化：接口不同QPS和RT，不同的接口服务(HTTP、RPC、文件传输等)，即：OneAPI

b.存储解析统一化，一套语言支持多种数据存储接入，即：OneSQL

c.数据模型统一化，支持多种数据源接入, 即:OneModel


#### 软件架构

技术栈:

后端：Spring boot + Mybatis

数据库:MySQL

#### 系统流程图


#### 系统功能演示

#### 系统运行
系统运行环境要求：

java jdk8

mysql 5.7.36

运行配置

(1)将sql目录中的sql文件进行运行在数据库，创建库和表

(2)下载发布好的jar(https://github.com/zhugezifang/dataService/releases) 或者自己构建jar

(3)修改数据库配置信息 application.yml

(4)运行 java -jar -Dspring.config.location=application.yml dataService.jar (application.yml和jar在同一目录下)

(5)访问 http://127.0.0.1/ 即可 (账号:admin 密码:admin123)

#### 技术交流
![image](https://user-images.githubusercontent.com/28300167/207255900-152d6834-9602-4ada-91ca-ad9906d89bf8.png)


#### 致谢
感谢ruoyi 提供前端服务
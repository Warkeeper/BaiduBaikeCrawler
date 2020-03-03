# BaiduBaikeCrawler
This project is a crawler which trying to get all lemmas of Baidu Baike.
The author has downloaded about 100,000 lemmas in one and a half hour.
This project uses https://github.com/qq1367212627/XDX03065 for reference and improves its performance.

该爬虫项目以爬取百度百科所有词条为目标所建立。
作者用了大概一个半小时爬了10万条（占满了10M的电信带宽），由于数据库放在了硬盘空间紧缺的C盘，所以在10万条停止不爬了，理论上能够爬完所有条目。
该项目参考了github用户qq1367212627的项目，在其版本之上进行了一定程度的修改，添加了反爬虫策略。包括http请求头混淆和代理池。

该项目目前较大的缺陷在于内存占用会不断增加。
其原因也很好理解：如果在网络带宽占用保持一致的情况下，你每爬一个词条，需要保存多个词条的url地址在内存中等待爬取，
由于获取网页内容时间远小于数据持久化于数据库中的时间，所以会一直堆积着待获取网页的url，导致内存占用越来越大。
（PS：url去重算法用了bloomfilter算法，插入和查询时间复杂度为常数，空间复杂度也为常数，所以该部分不随任务增加而增加内存占用）
该缺陷可以通过将待爬取url队列转存至硬盘之类的方法解决，不过经过测算大概32GB系统内存就能够爬完百度百科，毕竟只有前期会内存增加，到后期基本要爬完了占用内存就会被卸下。

该项目代理池功能基于data5u提供的代理服务，所以如果要开启代理池功能请到data5u开个会员，把proxypool.java里的订单号换成自己的就ok。
但经过测试，截止至2018年1月，百度百科未针对ip进行反爬虫。（http请求头混淆就好了，不混淆大概爬2分钟就会被denied）

项目配置启动方法请见“代码使用说明.doc”

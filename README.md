# Wechatxposed
微信二维码收付款监听
极限：手机需要root，如果不进行手机root，可以安装 VirtualXposed ：https://github.com/android-hacker/VirtualXposed/releases  （直接下载apk安装即可， 使用方式有详细介绍，该项目的源码不可运行）
太极：
https://htcui.com/22563.html （使用方式里面有详细介绍）

基于微信7.0.10实现二维码收款监听 （极限：采集不到支付用户的信息）
实现逻辑思路主要是：在信息插入数据库的insert方法作为hook点， 读取每条数据，数据包括聊天信息、二维码收付款、红包、新闻、页面跳转等，直接监听二维码收付款进行处理，但是此方试获取不到付款用户信息（注意：不同微信版本类型有区别，需要进行具体版本做处理）
Eg: 此种方式不需要研究微信版本源码，只需要监听查看不同微信版本消息返回类型做兼容区分即可
参考文档与工程：
1.https://github.com/coder-pig/CPWechatXposed  （Xposed使用的详细工程， 可作为工程参考）
2.https://blog.csdn.net/xiao_nian/article/details/80533513 （讲述如何分析微信源码， 以及二维码收款监听另一种思路， 不适合新版本， 可以参考）
3.https://blog.csdn.net/LiFangHui_/article/details/89355803 （获取二维码收款监听， 监听类型不适应最新版本， 可做参考）
4.https://blog.csdn.net/xiao_nian/article/details/79391417 （红包监听， 内有详细的源码分析流程， 可以参考内部源码分析流程）
5.https://github.com/ElderDrivers/EdXposed （Android高版本xposed不可以用代替框架，xposed失效参考框架）
6.https://github.com/wuxiaosu/XposedWechatHelper （可参考工程）

## 基于netty的聊天软件
### 1、技术

后端：springboot + mybatis + netty框架。

前端：im组件、html、css、js。

推荐编译软件：前端（HBuilder）、后端（Idea）

数据库：mysql。

测试模拟器：夜神模拟器。

### 2、建立夜神模拟器和HBuilder之间的连接

（1）下载“夜神模拟器”，https://www.yeshen.com，安装比较简单，默认安装即可。

（2）安装完成后，模拟器默认会以平板模式开启，如果需更改为手机模式，点击“设置”图标，选择“高级设置”，分辨率设置为“手机版”。点击“保存设置”，模拟器重启后即可。

![img](https://img-cdn-qiniu.dcloud.net.cn/uploads/article/20170817/d1af123a032a8288dd8031f4b1f6b997.png)

（3）HBuilder和模拟器端口连接设置，看附件图

（4） win + r （Windows）输入cmd进入dos窗口，cd 到夜神模拟器的安装目录bin下， 如：cd D:\Program Files (x86)\Nox\bin，然后输入命令：nox_adb connect 127.0.0.1:62001 进行连接 ,接下来使用命令：nox_adb devices 查看连接信息，切换目录到HBuilder的tools目录下:cd D:\HBuilder.7.5.0.windows\HBuilder\tool，输入命令：adb connect 127.0.0.1:62001，输入命令：adb devices 查看连接信息。

![img](https://img-cdn-qiniu.dcloud.net.cn/uploads/article/20170817/60fa36168ffcd357b08b52529d80d30c.png)

（5）接下来，端口设置好后，点击Hbuilder栏目上面运行，下面会有一个选择手机运行，就可以看到对应的端口运行，点击运行已经创建好的移动APP吧！

![img](https://img-cdn-qiniu.dcloud.net.cn/uploads/article/20170817/c291330451f9aa2555a40b4222b01ffc.png)

> 注意：如果要开启多个夜神模拟器，需要先开启夜神多开器，然后在依次启动两个端口。如果发现运行夜神模拟器电脑蓝屏，可以参考这个解决办法：<https://wnag.com.cn/1031.html>。

![1593760020882](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1593760020882.png)

### 3、启动程序

服务端启动程序：启动类Application。Hbuilder再启动。

在前端程序common.js中，这两个服务地址改成电脑的ip。

![1593760257548](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1593760257548.png)
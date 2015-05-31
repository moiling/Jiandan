## 考核完成情况如下：

- APK下载

  > 链接：http://pan.baidu.com/s/1hqozvVm 密码：1k5w

- 兼容

  > API14-API22
  
- Requirements
  
  > compileSdkVersion 21
  
  > buildToolsVersion 21.1.2
  
  > 这个就没有改了……不要怪我！
  
- Dependencies
  
  > appcompat-v7 
  
  > cardview-v7
  
  > 这两个应该没问题吧！

 > 以下演示没特别说明均为小米4 MIUI V6 的效果

### 必做题
- 顶部actionbar或者toolbar放APP图标，APP名字

  > 使用了Toolbar，效果如图所示
  
  > ![image](https://github.com/Saijiu/JIANDAN/blob/master/art/1.png)<br>

- 使用ViewPager＋Fragment，完成3个主体界面
	
	> 整体： 
	
	> ![image](https://github.com/Saijiu/JIANDAN/blob/master/art/2.gif)<br>
	
	> 妹子图（用了分段加载，每次加载十张，自动换页，因为图片加载和文本加载异步了，所以滑太快有事会出现问题。对图片进行了压缩，点击放大的时候是原图）：

	> ![image](https://github.com/Saijiu/JIANDAN/blob/master/art/3.gif)<br>
	
	> 段子：
	
	> ![image](https://github.com/Saijiu/JIANDAN/blob/master/art/4.gif)<br>
	
	> 妹子图的点击：（这里全屏时状态栏变色是MIUIV6特有的功能！其他ROM效果为背景加一层黑色阴影）
	
	> ![image](https://github.com/Saijiu/JIANDAN/blob/master/art/5.gif)<br>
	
	> 段子的点击：
	
	> ![image](https://github.com/Saijiu/JIANDAN/blob/master/art/6.gif)<br>
	
	
- APP更新功能：

	> ![image](https://github.com/Saijiu/JIANDAN/blob/master/art/7.gif)<br>


### 选做题
 
 
- 数据库题：[答案在这里](https://github.com/Saijiu/JIANDAN/blob/master/%E6%95%B0%E6%8D%AE%E5%BA%93.md)

- 在妹子图打开后查看大图的页面：  


	- 长按选择保存到某个文件夹下：

	
	  > 如图：

       > ![image](https://github.com/Saijiu/JIANDAN/blob/master/art/8.gif)<br>
       
       > ![image](https://github.com/Saijiu/JIANDAN/blob/master/art/9.png)<br>
	  
	- 长按选择分享：
	
	 > 如图：

       > ![image](https://github.com/Saijiu/JIANDAN/blob/master/art/10.gif)<br>
       

### 自由发挥

- 第三个Fragment（运用了数据库和传感器）：

  > 在这里做了一个用来抽签的东西
  
  > 在这里创建抽签类型，点击跳转创建这个抽签类型的签
  
  > 然后通过摇一摇随机抽签
  
  > 效果如图：
  
  > ![image](https://github.com/Saijiu/JIANDAN/blob/master/art/11.gif)<br>

- 一些现在的APP都有的东西：

 - 登陆：
  
   > 完全没有新花招，和以前一样，有记住密码和自动登陆功能（**这次加键盘中回车键的响应**）
  
    > ![image](https://github.com/Saijiu/JIANDAN/blob/master/art/12.gif)<br>
  
 - 抽屉：
  
   > 可以切换头像（拍照或者从相册取，这里就不做拍照演示了），保存签名
  
    > ![image](https://github.com/Saijiu/JIANDAN/blob/master/art/13.gif)<br>
  
 - 更换主题色：
 
   > 实在太累了……有一些隐藏的按钮啥子没变色就放过我吧！
  
    > ![image](https://github.com/Saijiu/JIANDAN/blob/master/art/14.gif)<br>
  
 - 刷新按钮：
 
   > 感觉临时学习下拉刷新来不及了，就加了一个浮动按钮刷新，可以根据手势隐藏/出现
  
    > ![image](https://github.com/Saijiu/JIANDAN/blob/master/art/15.gif)<br>
  
 - 匹配中英文……好吧，我是没什么值得说的了

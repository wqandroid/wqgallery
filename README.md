#微信样式相册选择器
=====

####下面是效果图

![Image text](https://coding.net/u/wangqiong/p/wqgallery/git/raw/master/app/screenshort/wqgallert1.gif)

![Image text](https://coding.net/u/wangqiong/p/wqgallery/git/raw/master/app/screenshort/Screenshot_2015-03-31-18-35.png)




[ ![Download](https://api.bintray.com/packages/wqandroid/maven/Photogallery/images/download.svg) ](https://bintray.com/wqandroid/maven/Photogallery/_latestVersion)

##怎么使用?

====
#####第一步 在项目的 build.gradle 添加dependencies
    
    dependencies {
       compile 'wq.photo:photogallery:2.0.0'
    }
#####第二步 调用相册选择器
    Intent intent=new Intent(MainActivity.this, MediaChoseActivity.class);
    //chose_mode选择模式 0单选 1多选
    intent.putExtra("chose_mode",0);
    //最多支持选择多少张
    intent.putExtra("max_chose_count",6);
    //是否显示actionbar
    intent.putExtra("isneedactionbar",false);
    //是否显示需要第一个是图片相机按钮
    intent.putExtra("isNeedfcamera",false);
    //是否剪裁图片(只有单选模式才有剪裁)
    intent.putExtra("crop",true);
    startActivityForResult(intent, REQUEST_IMAGE);

      
      
   
#####第三步 在onActivityResult中获取选择的图片路径列表

    ArrayList<String>paths=data.getStringArrayListExtra("data");





###Version 2.0.0
     1.0.1 新增单选截图模式
     1.0.2 修改一些bug,以及样式
     2.0.0 修改之前遗留的一些bug,加载图片换成了glide


###Thanks
  * [Glide](https://github.com/bumptech/glide) A powerful image downloading and caching library for Android

##Developed By
#####wqandroid@gmail.com
####sinaweibo *[ios_android技术宅拯救世界](http://weibo.com/2407182217/profile?rightmod=1&wvr=6&mod=personinfo) 
##License

    Copyright 2013 Andreas Stuetz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

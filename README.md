#微信样式相册选择器
=====

####下面是效果图

![Image text](https://github.com/wqandroid/wqgallery/raw/dev/app/screenshort/wqgallert.gif)

[![IMAGE ALT TEXT HERE](https://github.com/wqandroid/wqgallery/raw/dev/app/screenshort/Screenshot_20161.png)](https://youtu.be/uZE2kVCwJxg)

[![](https://jitpack.io/v/wqandroid/wqgallery.svg)](https://jitpack.io/#wqandroid/wqgallery)

##怎么使用?

====
#####第一步 在项目的 build.gradle 添加dependencies
    allprojects {
    		repositories {
    			...
    			maven { url "https://jitpack.io" }
    		}
    	}

    dependencies {
   	        compile 'com.github.wqandroid:wqgallery:3.0.0'
   	}

#####第二步 调用相册选择器

     //图片剪裁的一些设置
     UCrop.Options options = new UCrop.Options();
     //图片生成格式
     options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
     //图片压缩比
     options.setCompressionQuality(seekBar.getProgress());

     new  PickConfig.Builder(MainActivity.this)
                .maxPickSize(9)//最多选择几张
                .isneedcamera(true)//是否需要第一项是相机
                .spanCount(4)//一行显示几张照片
                .actionBarcolor(Color.parseColor("#E91E63"))//设置toolbar的颜色
                .statusBarcolor(Color.parseColor("#D81B60")) //设置状态栏的颜色(5.0以上)
                .isneedcrop(false)//受否需要剪裁
                .setUropOptions(options) //设置剪裁参数
                .isSqureCrop(true) //是否是正方形格式剪裁
                .pickMode(PickConfig.MODE_MULTIP_PICK)//单选还是多选
                .build();

      
      
   
#####第三步 在onActivityResult中获取选择的图片路径列表

     if(resultCode==RESULT_OK && requestCode == PickConfig.PICK_REQUEST_CODE){
            //在data中返回 选择的图片列表
            ArrayList<String>paths=data.getStringArrayListExtra("data");
     }





###Version 3.0.0
     1.0.1 新增单选截图模式
     1.0.2 修改一些bug,以及样式
     2.0.0 修改之前遗留的一些bug,加载图片换成了glide
     2.1.0 修改了配置选择的代码 支持自定义一行显示几张
     2.2.0 修改没有数据的异常
     3.0.0 修改Actionbar问题,支持Crop

###Thanks
  * [Glide](https://github.com/bumptech/glide) A powerful image downloading and caching library for Android
  * [uCrop ](https://github.com/Yalantis/uCrop) 感谢uCrop 作者,当前库的剪裁采用uCrop

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

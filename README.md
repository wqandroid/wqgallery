#android微信样式相册选择器



![Image text](https://github.com/wqandroid/wqgallery/blob/master/app/screenshort/wqgallert1.gif)

![Image text](https://github.com/wqandroid/wqgallery/blob/master/app/screenshort/Screenshot_2015-03-31-18-35-29.png)

![Image text](https://github.com/wqandroid/wqgallery/blob/master/app/screenshort/Screenshot_2015-03-31-18-35-34.png)
##怎么使用?

下载工程及mode compile project(':photogallery')
####1.支持单选模式
#####2.支持多选模式
####下面是调用选择器        
      
    Intent intent=new Intent(MainActivity.this, MediaChoseActivity.class);
    //chose_mode选择模式 0单选 1多选
    intent.putExtra("chose_mode",0);
    //最多支持选择多少张
    intent.putExtra("max_chose_count",6);
    //是否剪裁图片(只有单选模式才有剪裁)
    intent.putExtra("crop",true);
    //输出剪裁图片的宽度
    intent.putExtra("crop_image_w",720);
    //输出剪裁图片的高度
    intent.putExtra("crop_image_h",720);
    startActivityForResult(intent, REQUEST_IMAGE);

####在onActivityResult中获取选择的图片路径列表

    ArrayList<String>paths=data.getStringArrayListExtra("data");
    





###当前版本1.0.1
     下个版本能够在gradle引入photogallery库
      1.0.1 新增单选截图模式

##Developed By
#####wqandroid@gmail.com
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

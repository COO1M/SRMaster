# SR大师

本项目基于[Real-ESRGAN](https://github.com/xinntao/Real-ESRGAN)，将pth模型文件转为ptl部署到安卓运行。

模型转换请参考Real-ESRGAN源码和[Pytorch Mobile](https://pytorch.org/mobile/home/)，在此不再赘述，这里只对本项目进行阐述。

### 使用说明

1. 模型运行在本地，因此对手机性能要求较高。最低要求：手机64位(arm64-v8a)、系统Android10及以上。若达不到该要求，也提供向下兼容包，可兼容32位(armeabi-v7a)
   、Android5及以上手机，但非常不推荐折磨手机。

2. 大图片会非常耗费CPU和内存，为了不影响你的手机正在运行的其他服务，限制了图片若长或宽超过1000像素，即会按原比例将图片缩小到最长边为1000像素。因此大图片会有较大的压缩再进行增强，效果肯定还不如原图。

   例如导入1000 * 2000的图片，若不进行限制，即为将1000 * 2000=200万像素扩展到4000 * 8000=3200万像素。限制后即为将500 * 1000=50万像素扩展到2000 * 4000=800万像素。

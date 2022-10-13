# SR大师

AI超分辨率的应用实现，在算力要求较高、Android碎片化严重、工具链包体积较大的情况下，Android端一般调用部署到服务器的AI实现。本项目即为一次Android端本地部署AI图片超分辨率的实现。

模型算法请移步[Real-ESRGAN](https://github.com/xinntao/Real-ESRGAN)，我只对其模型结构部分有所阅读，精力有限。

模型转换请参考[Real-ESRGAN](https://github.com/xinntao/Real-ESRGAN) 源码和[Pytorch Mobile](https://pytorch.org/mobile/home/) 文档，在此不再赘述，这里只对本项目进行阐述。

## 软件使用

### 功能说明

**选取：** 选择要增强的图片。建议50万像素(=长x宽)以下，超过则会先压缩再增强，手机性能有限。

**运行：** 开始AI增强，请等待几秒或几十秒不等。速度取决于手机性能和图片复杂度，效果取决于图片细节留存度。

**保存：** 保存到“根目录/Pictures/SRMaster”文件夹。

**超级模式：** 取消压缩图片，取消调度策略，满载运行！运行时会占用巨量运存和CPU，非近年旗舰机型慎用。右上角第二颗按钮。

**对比：** 切换预览增强前后的图片。右上角第三颗按钮。  

### 设备要求

**最低：** Android 出厂5.0，CPU armeabi-v7a(32位)

**推荐：** Android 出厂9.0，CPU arm64-v8a(64位)

## 技术选型

### 算法

**ESRGAN：** 效果还OK，但没有后续维护支持，并且在Android端资源占用太高了，因此我在测试完效果后即排除。

**Real-CUGAN：** 动漫风格为主，排除。

**Real-ESRGAN：** 有维护支持、动漫/照片都行、且3.0大幅减小了模型体积，基本满足我的需求。

> 只是测试是否满足本项目的需求，并非对以上研究成果进行指点！

> 其余如waifu2x、SRMD、RealSR可自行搜索参考。基准测试可参考https://videoprocessing.ai/benchmarks/video-upscalers.html。

### 部署

**Tensorflow Lite：** 对Android适配性最好，能够方便地使用CPU、GPU、NNAPI，并且对部署方法、文档非常熟悉。但Pytorch->ONNX->Tensorflow->Tensorflow Lite后，效率欠佳。排除。

**ncnn：** 部署复杂，且需要对自己的模型完成前置cmake代码。暂排除，有时间细品。

**ONNX Runtime：** 在Android端可使用NNAPI，但别人测试Real-ESRGAN的部署效率欠佳。暂排除，有时间细品。

**Pytorch Mobile：** pth直接转ptl，转换和部署文档简洁清晰，支持自适应图片尺寸。但暂时只支持CPU，不过效率也还行，基本满足。

> 注意！Pytorch Android部署有一个问题：正式打包使用minifyEnabled=true后，编译时Proguard会将PytorchAndroid以及其中的依赖混淆掉，导致报错，请添加`-keep class org.pytorch.** {*;}`、` -keep class com.facebook.** {*;}`到Proguard rules，目前我已提交Pr。

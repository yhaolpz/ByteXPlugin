## 背景

ByteX 与 Jetpack StartUp 有异曲同工之妙。

Startup 针对多个三方库各自使用 ContentProvider 初始化导致拖慢启动速度的问题，提供了一个 ContentProvider 来集中运行所有依赖项的初始化；ByteX 针对多个功能插件各自进行 transform 导致拖慢编译速度的问题，提供了一个宿主插件 transform，集中处理所有的 transform。

ByteX 对 Transform 及 ASM 相关 API 做了封装，大大节省了插件开发的工作量，我们无需处理 class/jar 的 IO 操作，只需关注想要进行的 hook 逻辑即可。

所以，通过性能及开发成本两个维度的考量，基于 ByteX 开发一些有意义的插件，是一个不错的选择...

## 基于 ByteX 开发的插件

- [trace-plugin](trace/README.md)（方法插桩，可自由定制插桩代码，不只是耗时打印）

## 鸣谢

- [ByteX](https://github.com/bytedance/ByteX)
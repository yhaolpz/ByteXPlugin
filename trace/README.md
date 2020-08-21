## trace-plugin 方法插桩插件


### 使用姿势

很简单，仅 @TraceClass、@TraceMethod 两个注解而已。

#### @TraceClass 为类注解，可配置：

```java
    Class methodTrace() default TimeTrace.class; //指定方法插桩实现类
    boolean traceAllMethod() default false; //是否要追踪此类中所有方法
    boolean traceInnerMethod() default false; //是否要追踪方法内部调用到的方法
```

#### @TraceMethod 为方法注解，可配置：

```java
    boolean trace() default true; //是否要追踪此方法
    boolean traceInnerMethod() default false; //是否要追踪方法内部调用到的方法
```

举个例子，Test 类中有 m1()、m2()、m3() 三个方法：

```java
public class Test{
    public static void m1() {
        m2();
        OtherClass.m4();
    }
    public static void m2() {
    }
    public static void m3() {
    }
}
```

- 1.追踪 m1() 耗时：

```java
@TraceClass
public class Test{
    @TraceMethod
    public static void m1() {...
```

- 2.追踪类中所有方法耗时：

```java
@TraceClass(traceAllMethod = true)
public class Test{...}
```

- 3.追踪类中所有方法耗时，但不包括 m1()：

```java
@TraceClass(traceAllMethod = true)
public class Test{
    @TraceMethod(trace = false)
    public static void m1() {...
```

- 4.若要追踪 m1() 方法内部调用到的方法，即 m2()、OtherClass.m4() 的耗时

```java
@TraceClass
public class Test{
    @TraceMethod(trace = false,traceInnerMethod = true)
    public static void m1() {...
```

- 5.自定义追踪插桩处理

```java
//继承自 IMethodTrace 方法实现自己的插桩处理，例如 systrace 追踪处理：
public class CustomSysTrace implements IMethodTrace {
    @Override
    public void onMethodEnter(String className, String methodName, String methodDesc, String outerMethod) {
        TraceCompat.beginSection(className + "#" + methodName);
    }
    @Override
    public void onMethodEnd(String className, String methodName, String methodDesc, String outerMethod) {
        TraceCompat.endSection();
    }
}

//在类注解中指定即可
@TraceClass(methodTrace = CustomSysTrace.class)
public class Test{...}
```

### 集成接入

- 配置工程 gradle

```
buildscript {
    ext {
        bytexPluginVersion = "0.1.8" //插件版本
    }
    repositories {
        ...
        //插件地址
        jcenter()
        maven { url "https://raw.githubusercontent.com/yhaolpz/ByteXPlugin/release/gradle_plugins" }
    }
    dependencies {
        classpath "com.bytedance.android.byteX:base-plugin:$bytexPluginVersion"
        classpath "com.wyh.plugin:trace-plugin:$bytexPluginVersion"
    }
}
```

- 配置应用 gradle

```
apply plugin: 'bytex'
ByteX {
    enable true
    enableInDebug true
    logLevel "DEBUG"
}

apply plugin: 'trace-plugin'
TracePlugin{
    enable true
    enableInDebug true
    logLevel "DEBUG"
}

dependencies {
    ...
    //依赖注解库
    implementation "com.wyh.plugin:trace-lib:${bytexPluginVersion}"
}
```

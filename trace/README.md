## trace-plugin 方法插桩插件


### 使用姿势

很简单，仅 @TraceClass、@TraceMethod 两个注解，搞懂这俩就行了

#### @TraceClass 为类注解，表示要追踪此类中的方法，可配置 3 个属性：

```
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface TraceClass {

    /**
     * 指定的方法插桩实现类，需继承自 {@link IMethodTrace} 接口，默认为 TimeTrace 耗时打印插桩
     */
    Class methodTrace() default TimeTrace.class;

    /**
     * 是否要追踪此类中所有方法
     */
    boolean traceAllMethod() default false;

    /**
     * 是否要追踪方法内部调用到的方法
     */
    boolean traceInnerMethod() default false;
}
```

#### @TraceMethod 为方法注解，可配置 2 个属性：

```
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface TraceMethod {

    /**
     * 是否要追踪此方法
     */
    boolean trace() default true;

    /**
     * 是否要追踪方法内部调用到的方法
     */
    boolean traceInnerMethod() default false;
}
```

举个例子，如下 Test 类中有 m1()、m2()、m3() 三个方法：

```
public class Test{

    public static void m1() {
        m2();
        m3();
        OtherClass.m4();
        OtherClass.m5();
    }

    public static void m2() {

    }

    public static void m3() {

    }
}
```

- 若要追踪类中 m1() 的耗时

Test 添加类注解 @TraceClass；m1() 方法添加注解 @TraceMethod

- 若要追踪类中所有方法，即 m1()、m2()、m3() 的耗时

Test 添加类注解 @TraceClass(traceAllMethod = true)

- 若要追踪类中所有方法但不包括 m1()，即 m2()、m3() 的耗时

Test 添加类注解 @TraceClass(traceAllMethod = true)；m1() 方法添加注解 @TraceMethod(trace = false)

- 若要追踪 m1() 方法内部调用到的方法，即 m2()、m3()、OtherClass.m4()、OtherClass.m5() 的耗时

Test 类添加注解 @TraceClass；m1() 方法添加注解 @TraceMethod(trace = false,traceInnerMethod = true)

- 若要使用自定义的追踪插桩处理追踪 m1() 方法

继承自 IMethodTrace 方法实现自己的插桩处理，例如 CustomSysTrace；
Test 添加类注解 @TraceClass(methodTrace = CustomSysTrace.class)；m1() 方法添加注解 @TraceMethod


PS：自定义插桩处理功能可以非常方便的结合 systrace 工具使用，比如：

```
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

package com.integration.compiler;

import com.google.auto.service.AutoService;
import com.integration.arouter_annotation.ARouter;
import com.integration.arouter_annotation.RouterBean;
import com.integration.compiler.utils.ProcessorConfig;
import com.integration.compiler.utils.ProcessorUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 *
 */
//环境的版本
@SupportedSourceVersion(SourceVersion.RELEASE_7)
//所使用的注解
@SupportedAnnotationTypes("com.integration.arouter_annotation.ARouter")
//启动注解服务
@AutoService(Processor.class)
//接收来自安卓工程的参数
@SupportedOptions("student")
public class ARouterProcessor extends AbstractProcessor {

    //操作element的工具类 （类 函数 属性）
    private Elements mElementTool;
    //type类信息的工具 包含操作typeMirror的工具方法
    private Types mTypesTool;
    //用来打印日志和相关信息
    private Messager mMessager;

    //文件生成器 类 资源，最终生成的文件 由filer来完成
    private Filer mFiler;

    //各个模块传递过来的模块名 例如：app order personal
    private String options;
    /**
     各个模块传递过来的目录 用于统一存放 apt生成的文件
     */
    private String aptPackage;

    /**
     * 缓存 path key是组名，value是class对象
     */
    private Map<String, List<RouterBean>> mAllPathMap = new HashMap<>();

    /**
     * 缓存
     */
    private Map<String, String> mAllGroupMap = new HashMap<>();

    /**
     * 初始化工作
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        mElementTool = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();

        String student = processingEnvironment.getOptions().get("student");
        mMessager.printMessage(Diagnostic.Kind.NOTE, "init----------------------" + student);


    }

    /**
     * 如果没有在任何地方使用该注解，这里是不会打印的
     *
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        // 有地方使用了注解，就会打印
        mMessager.printMessage(Diagnostic.Kind.NOTE, "-------------------process : wang run test");

        /**
         模块一
         package com.example.helloworld;

         public final class HelloWorld {

         public static void main(String[] args) {
         System.out.println("Hello, JavaPoet!");
         }
         }
         */
        //set集合判空
        if (set.isEmpty()) {
            return false;
        }
        /**
         *  通过Element工具类，获取Activity，Callback类型
         */
        TypeElement activityType = mElementTool.getTypeElement(ProcessorConfig.ACTIVITY_PACKAGE);
        // 显示类信息（获取被注解的节点，类节点）这也叫自描述 Mirror
        TypeMirror typeMirror = activityType.asType();
        //获取被ARouter注解的类节点信息
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(ARouter.class);
        for (Element element :
                elements) {
            // 获取类信息的顺序
            // 1  方法
            /*MethodSpec mainMethod = MethodSpec.methodBuilder("main")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(void.class)
                    .addParameter(String[].class, "args")
                    //增加main方法里面的内容
                    .addStatement("$T.out.println($S)", System.class,"Hello JavaPoet !")
                    .build();

            //2 类
            TypeSpec wangTest = TypeSpec.classBuilder("WangTest")
                    .addMethod(mainMethod)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .build();

            //3 包
            JavaFile packageFile = JavaFile.builder("com.wang.test", wangTest).build();

            //生成文件
            try {
                packageFile.writeTo(mFiler);

            }catch (IOException e) {
                e.printStackTrace();
                mMessager.printMessage(Diagnostic.Kind.NOTE,"生成Test文件时失败，异常: "+e.getMessage() );
            }*/


            /**
             * 一个简单的路由类
             * 模板 二
             */
            /**
             模板：
             public class MainActivity3$$$$$$$$$ARouter {

             public static Class findTargetClass(String path) {
             return path.equals("/app/MainActivity3") ? MainActivity3.class : null;
             }

             }
             */
            //获取包信息
            String packageName = mElementTool.getPackageOf(element).getQualifiedName().toString();

            //获取简单类名
            String className = element.getSimpleName().toString();
            //目标类名
            String finalClassName = className + "$$$$$$$$$ARouter";
            mMessager.printMessage(Diagnostic.Kind.NOTE,
                    "process----被@ARouter注解的类有 ：" + className);

            ARouter aRouter = element.getAnnotation(ARouter.class);
            // 1  方法
            MethodSpec findTargetClass = MethodSpec.methodBuilder("findTargetClass")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    //返回类型
                    .returns(Class.class)
                    //参数
                    .addParameter(String.class, "path")
                    // 方法里面的内容 return path.equals("/app/MainActivity3") ? MainActivity3.class : null;
                    .addStatement("return path.equals($S) ? $T.class : null",
                            aRouter.path(), ClassName.get((Type) element))
                    .build();

            //2 类
            TypeSpec myClass = TypeSpec.classBuilder(finalClassName)
                    .addMethod(findTargetClass)
                    .addModifiers(Modifier.PUBLIC)
                    .build();
            //3 包
            JavaFile packagef = JavaFile.builder(packageName, myClass).build();

            try {
                packagef.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
                mMessager.printMessage(Diagnostic.Kind.NOTE, "生成"
                        + finalClassName + "文件时失败，异常：" + e.getMessage());

            }
            /**
             * 在循环里面，对 “路由对象” 进行封装
             */
            RouterBean routerBean = new RouterBean.Builder()
                    .addGroup(aRouter.group())
                    .addPath(aRouter.path())
                    .addElement(element)
                    .build();
            // ARouter注解的类 必须继承 Activity
            TypeMirror elementMirror = element.asType();
            // Main2Activity的具体详情 例如：继承了 Activity
            // activityMirror  android.app.Activity描述信息
            if (mTypesTool.isSameType(elementMirror, typeMirror)) {
                // 最终证明是 Activity
                routerBean.setTypeEnum(RouterBean.TypeEnum.ACTIVITY);
            }else {
                // 不匹配抛出异常，这里谨慎使用！考虑维护问题
            }

            if (checkRouterPath(routerBean)) {
                mMessager.printMessage(Diagnostic.Kind.NOTE,
                        "RouterBean Check Success:" + routerBean.toString());
                // 赋值 mAllPathMap 集合里面去
                List<RouterBean> routerBeans = mAllPathMap.get(routerBean.getGroup());

                // 如果从Map中找不到key为：bean.getGroup()的数据，就新建List集合再添加进Map
                // 仓库一 没有东西
                if (ProcessorUtils.isEmpty(routerBeans)) {
                    routerBeans = new ArrayList<>();
                    routerBeans.add(routerBean);
                    // 加入仓库一
                    mAllPathMap.put(routerBean.getGroup(), routerBeans);
                } else {
                    routerBeans.add(routerBean);
                }
            }else {
                // ERROR 编译期发生异常
                mMessager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解未按规范配置，如：/app/MainActivity");
            }
        }


        //false表示不干活了； true表示干完了
        return true;
    }
    /**
     * 校验@ARouter注解的值，如果group未填写就从必填项path中截取数据
     * @param bean 路由详细信息，最终实体封装类
     */
    public final boolean checkRouterPath(RouterBean bean) {
        String group = bean.getGroup();
        String path = bean.getPath();

        //校验 @ARouter注解中的path值，必须要以 / 开头（模仿阿里Arouter规范）
        if (ProcessorUtils.isEmpty(path) || !path.startsWith("/")) {
            mMessager.printMessage(Diagnostic.Kind.ERROR,
                    "@ARouter注解中的path值，必须要以 / 开头");
            return false;
        }
        // 比如开发者代码为：path = "/MainActivity"，最后一个 / 符号必然在字符串第1位
        if (path.lastIndexOf("/") == 0) {
            mMessager.printMessage(Diagnostic.Kind.ERROR,
                    "@ARouter注解未按规范配置，如：/app/MainActivity");
            return false;
        }
        // @ARouter注解中的path值，必须要以 / 开头（模仿阿里Arouter规范）
        String finalGroup = path.substring(1, path.indexOf("/", 1));

        // @ARouter注解中的group有赋值情况
        if (!ProcessorUtils.isEmpty(group) && !group.equals(options)) {
            // 架构师定义规范，让开发者遵循
            mMessager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解中的group值必须和子模块名一致！");
            return false;
        } else {
            bean.setGroup(finalGroup);
        }
// 如果真的返回ture   RouterBean.group  xxxxx 赋值成功 没有问题
        return true;
    }
}

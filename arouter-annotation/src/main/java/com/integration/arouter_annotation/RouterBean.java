package com.integration.arouter_annotation;

import javax.lang.model.element.Element;

public class RouterBean {


    public enum TypeEnum {
        ACTIVITY
    }
    private TypeEnum typeEnum; // 枚举类型：Activity
    private Element element; // 类节点 JavaPoet学习的时候，可以拿到很多的信息
    private Class<?> myClass; // 被注解的 Class对象 例如： MainActivity.class  Main2Activity.class
    private String path; // 路由地址  例如：/app/MainActivity
    private String group; // 路由组  例如：app  order  personal

    // TODO 以下是一组 Get 方法
    public TypeEnum getTypeEnum() {
        return typeEnum;
    }

    public Element getElement() {
        return element;
    }

    public Class<?> getMyClass() {
        return myClass;
    }

    public String getPath() {
        return path;
    }

    public String getGroup() {
        return group;
    }

    // TODO 以下是一组 Set 方法
    public void setTypeEnum(TypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public void setMyClass(Class<?> myClass) {
        this.myClass = myClass;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    private RouterBean(TypeEnum typeEnum, /*Element element,*/ Class<?> myClass, String path, String group) {
        this.typeEnum = typeEnum;
//        this.element = element;
        this.myClass = myClass;
        this.path = path;
        this.group = group;
    }

    public static RouterBean create(TypeEnum typeEnum,  Class<?> myClass, String path, String groupT) {
        return new RouterBean(typeEnum, myClass, path, groupT);
    }


    public RouterBean(Builder builder) {
        this.typeEnum = builder.type;
        this.element = builder.element;
        this.myClass = builder.clazz;
        this.path = builder.path;
        this.group = builder.group;
    }

    /**
     * 建造者模式
     */
    public static class Builder {
        // 枚举类型：Activity
        private TypeEnum type;
        // 类节点
        private Element element;
        // 注解使用的类对象
        private Class<?> clazz;
        // 路由地址
        private String path;
        // 路由组
        private String group;

        public Builder addType(TypeEnum typeEnum) {
            type = typeEnum;
            return this;
        }

        public Builder addElement(Element element) {
            this.element = element;
            return this;
        }

        public Builder addClazz(Class<?> clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder addPath(String path) {
            this.path = path;
            return this;
        }

        public Builder addGroup(String group) {
            this.group = group;
            return this;
        }

        public RouterBean build() {
            return new RouterBean(this);
        }
    }

    @Override
    public String toString() {
        return "RouterBean{" +
                " path='" + path + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}

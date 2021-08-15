package com.integration.arouter_api;

import com.integration.arouter_annotation.RouterBean;

import java.util.Map;

/**
 * @author Wang
 * @version 1.0
 * @date 2021/8/12 - 1:14
 * @Description com.integration.arouter_api
 */
/**
 *  其实就是 路由组 Group 对应的 ---- 详细Path加载数据接口 ARouterPath
 *  例如：order分组 对应 ---- 有那些类需要加载（Order_MainActivity  Order_MainActivity2 ...）
 *
 *
 *  TODO
 *    key:   /app/MainActivity1
 *    value:  RouterBean(MainActivity1.class)
 *
 */
public interface ARouterPath {

    /**
     * 例如：order分组下有这些信息，personal分组下有这些信息
     * 例如："order" --- ARouterPath的实现类 -->（APT生成出来的 ARouter$$Path$$order）
     *
     * @return key:"order/app/personal"      value:系列的order组下面所有的（path---class）
     */
    Map<String, RouterBean> getPathMap();
}

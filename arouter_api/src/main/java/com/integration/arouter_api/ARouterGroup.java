package com.integration.arouter_api;

import java.util.Map;

/**
 * @author Wang
 * @version 1.0
 * @date 2021/8/12 - 1:14
 * @Description com.integration.arouter_api
 */
/**
 * Group分组的领头 带头大哥
 *
 * TODO
 *  *    key:   app
 *  *    value:  ARouterPath
 */
public interface ARouterGroup {

    /**
     * 例如：order分组下有这些信息，personal分组下有这些信息
     * 例如："order" --- ARouterPath的实现类 -->（APT生成出来的 ARouter$$Path$$order）
     *
     * @return  key:"order/app/personal"      value:系列的order组下面所有的（path---class）
     */
    Map<String, Class<? extends ARouterPath>> getGroupMap();
}

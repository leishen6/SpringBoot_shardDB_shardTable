package com.lyl.utils;

import com.lyl.constant.SystemConstant;

/**
 * @PACKAGE_NAME: com.lyl.utils
 * @ClassName: ShardingUtils
 * @Description: 分库分表的工具类
 * @Date: 2020-06-19 22:01
 **/
public class ShardingUtils {


    /**
     * 根据取模后的值获取虚拟节点对应的物理节点
     *
     * @param mod 取模数据
     * @return 物理节点
     */
    public static Integer getPhysicNodeByVisualNode(int mod, String virtualNodeCountRang) {
        String[] rangArr = virtualNodeCountRang.split(SystemConstant._DEFAULT_SEPARATOR_COMMA);
        Integer physicCount = null;
        for (int i = 0; i < rangArr.length; i++) {
            String[] tmpArr = rangArr[i].split("-");
            if ((mod >= Integer.parseInt(tmpArr[0])) && (mod <= Integer.parseInt(tmpArr[1]))) {
                physicCount = i;
                break;
            }
        }

        return physicCount;
    }


    /**
     *  通过位运算获取一个数的绝对值, 位运算比较快
     * @param value 正数或负数
     * @return
     */
    public static Integer getAbsoluteValue(Integer value){
        Integer absoluteValue = 0;
        if (value != null){
            absoluteValue = (value + (value >> 31)) ^ (value >> 31);
        }
        return absoluteValue;
    }


}

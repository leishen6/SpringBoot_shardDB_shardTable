package com.lyl.algorithm;

import cn.hutool.setting.dialect.Props;
import com.lyl.constant.SystemConstant;
import com.lyl.utils.ShardingUtils;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * @PACKAGE_NAME: com.lyl.algorithm
 * @ClassName: MyPreciseDBShardingAlgorithm
 * @Description:  自定义数据库的精确分片算法，根据用户名进行分片
 * @Date: 2020-06-18 17:28
 **/
public class MyPreciseDBShardingAlgorithm  implements PreciseShardingAlgorithm<String> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 分库虚拟节点数量
    public static int virtualNodeCount;

    // 分库虚拟节点范围
    public static String virtualNodeCountRang;


    public MyPreciseDBShardingAlgorithm(){

    };


    @Override
    public String doSharding(Collection<String> dbNames, PreciseShardingValue<String> preciseShardingValue) {

        Props props = new Props("application-shardingJDBC.properties");
        // 初始化分库的虚拟节点数量和范围
        virtualNodeCount = props.getInt(SystemConstant.SHARDING_DATASOURCE_VIRTUAL_NODE_COUNT);
        virtualNodeCountRang = props.getStr(SystemConstant.SHARDING_DATASOURCE_VIRTUAL_NODE_COUNT_RANG);

        // 根据用户名的hash值对《virtualNodeCount》进行取余后，得到余数，余数一定在0，《virtualNodeCount》之间的
        Integer mod = preciseShardingValue.getValue().hashCode() % virtualNodeCount;

        // 由于获取的字符串的hash值可能存在负数，所以需要需要取其绝对值
        mod = ShardingUtils.getAbsoluteValue(mod);

        // 虚拟节点范围映射到实际物理节点
        Integer shardingValue = ShardingUtils.getPhysicNodeByVisualNode(mod, virtualNodeCountRang);

        for (String each : dbNames) {
            // 将余数与配置的实际数据库名进行匹配
            if (each.endsWith(String.valueOf(shardingValue))) {
                logger.info("logic DB : "+ each);
                return each;
            }
        }

        throw new UnsupportedOperationException();
    }
}
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
 * @ClassName: MyPreciseTableShardingAlgorithm
 * @Description: 自定义表的精确分片算法，根据用户名进行分片
 * @Date: 2020-06-18 17:11
 **/
public class MyPreciseTableShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 分表虚拟节点数量
     */
    public static int virtualNodeCount;

    /**
     * 分表虚拟节点范围
     */
    public static String virtualNodeCountRang;


    public MyPreciseTableShardingAlgorithm() {

    }


    /**
     * 数据分片
     *
     * @param tableNames           实际表集合; t_user0、t_user1、t_user2
     * @param preciseShardingValue 分片键 name 用户名值
     * @return
     */
    @Override
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<String> preciseShardingValue) {

        Props props = new Props("application-shardingJDBC.properties");
        // 初始化分表的虚拟节点数量和范围
        virtualNodeCount = props.getInt(SystemConstant.SHARDING_TABLE_VIRTUAL_NODE_COUNT);
        virtualNodeCountRang = props.getStr(SystemConstant.SHARDING_TABLE_VIRTUAL_NODE_COUNT_RANG);


        // 根据用户名的hash值对《virtualNodeCount》进行取余后，得到余数，余数一定在0，《virtualNodeCount》之间的
        Integer mod = preciseShardingValue.getValue().hashCode() % virtualNodeCount;

        // 由于获取的字符串的hash值可能存在负数，所以需要需要取其绝对值
        mod = ShardingUtils.getAbsoluteValue(mod);

        // 虚拟节点范围映射到实际物理节点
        Integer shardingValue = ShardingUtils.getPhysicNodeByVisualNode(mod, virtualNodeCountRang);

        for (String each : tableNames) {
            // 将余数与配置的实际表名进行匹配
            if (each.endsWith(String.valueOf(shardingValue))) {
                logger.info("logic table : {}", each);
                return each;
            }
        }

        throw new UnsupportedOperationException();
    }
}

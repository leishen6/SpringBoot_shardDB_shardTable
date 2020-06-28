package com.lyl.constant;


/**
 * @PACKAGE_NAME: com.lyl.constant
 * @ClassName: SystemConstant
 * @Description:  常量类，存放使用的常量
 * @Date: 2020-06-18 14:05
 **/
public class SystemConstant {

    /** Swagger2 扫描controller包的路径 **/
    public static final String BASE_PACKAGE = "com.lyl.controller";

    /** findAll 全部查询数据的redis的key **/
    public static final String USER_FINDUSERALL = "User_findUserAll";

    /** 分库的虚拟节点范围 **/
    public static final String SHARDING_DATASOURCE_VIRTUAL_NODE_COUNT_RANG = "sharding.datasource.virtual.node.count.rang";

    /** 分表的虚拟节点范围 **/
    public static final String SHARDING_TABLE_VIRTUAL_NODE_COUNT_RANG = "sharding.table.virtual.node.count.rang";

    /** 分库的虚拟节点数量 **/
    public static final String SHARDING_DATASOURCE_VIRTUAL_NODE_COUNT = "sharding.datasource.virtual.node.count";

    /** 分表的虚拟节点数量 **/
    public static final String SHARDING_TABLE_VIRTUAL_NODE_COUNT = "sharding.table.virtual.node.count";

    /** 分割符 **/
    public static final String _DEFAULT_SEPARATOR_COMMA = ",";

    /** 是否启用redis, 默认不启用 **/
    public static final String DEFAULT_IF_DISABLE_REDIS = "default_if_disable_redis";

    /** redis 分布式锁的过期时间 **/
    public static final int DISTRIBUTEDLOCK_EXPIRETIME = 1000;


}

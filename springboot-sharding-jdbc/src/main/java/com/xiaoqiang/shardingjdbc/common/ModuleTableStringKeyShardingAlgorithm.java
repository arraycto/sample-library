package com.xiaoqiang.shardingjdbc.common;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.google.common.collect.Range;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * 分表策略
 */
public final class ModuleTableStringKeyShardingAlgorithm implements SingleKeyTableShardingAlgorithm<String> {

    /**
     *
     * @param tableNames 实际物理表名
     * @param shardingValue [logicTableName="t_order", columnName="order_id", value=20]
     *        select * from t_order from t_order where order_id = 11
     *      *          └── SELECT *  FROM t_order_1 WHERE order_id = 11
     *      *  select * from t_order from t_order where order_id = 44
     *      *          └── SELECT *  FROM t_order_0 WHERE order_id = 44
     * @return
     */

    public String doEqualSharding(Collection<String> tableNames, ShardingValue<String> shardingValue) {
        for (String tableName : tableNames){
            if(tableName.substring(tableName.length()-6).equals(shardingValue.getValue())){
                return tableName;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     *         select * from t_order from t_order where order_id in (11,44)
     *      *          ├── SELECT *  FROM t_order_0 WHERE order_id IN (11,44)
     *      *          └── SELECT *  FROM t_order_1 WHERE order_id IN (11,44)
     *      *  select * from t_order from t_order where order_id in (11,13,15)
     *      *          └── SELECT *  FROM t_order_1 WHERE order_id IN (11,13,15)
     *      *  select * from t_order from t_order where order_id in (22,24,26)
     *      *          └──SELECT *  FROM t_order_0 WHERE order_id IN (22,24,26)
     * @param tableNames
     * @param shardingValue
     * @return
     */

    public Collection<String> doInSharding(Collection<String> tableNames, ShardingValue<String> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(tableNames.size());
        for (String value : shardingValue.getValues()) {
            for (String tableName : tableNames){
                if(tableName.substring(tableName.length()-7).equals(shardingValue)){
                    result.add(tableName);
                }
            }
        }
        return result;
    }

    /**
     *  select * from t_order from t_order where order_id between 10 and 20
     *      *          ├── SELECT *  FROM t_order_0 WHERE order_id BETWEEN 10 AND 20
     *      *          └── SELECT *  FROM t_order_1 WHERE order_id BETWEEN 10 AND 20
     * @param tableNames
     * @param shardingValue
     * @return
     */

    public Collection<String> doBetweenSharding(Collection<String> tableNames, ShardingValue<String> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(tableNames.size());

        Range<String> range = shardingValue.getValueRange();
//        for (Long i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
//            for (String each : tableNames) {
//                if (each.endsWith(i % 2 + "")) {
//                    result.add(each);
//                }
//            }
//        }
        return result;
    }
}

package com.freeco.sharding.config;

import io.shardingsphere.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * 精确分片算法asdasd
 */
public class OrderDBShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        Long id = preciseShardingValue.getValue();
        Long dbNum = id%2;
        for (String each : collection) {
            if (each.endsWith(String.valueOf(dbNum))) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }
}

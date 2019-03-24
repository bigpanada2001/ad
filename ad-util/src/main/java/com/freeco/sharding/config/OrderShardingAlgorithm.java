package com.freeco.sharding.config;

import io.shardingsphere.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * 精确分片算法
 */
public class OrderShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        Long id = preciseShardingValue.getValue();
//        // 获取时间（年月日）
//        Long time = id >> 22;
//        time += TimeConstant.START_UNIX_TIME;
//        String timeString = DateUtil.unixToDateString(time, TimeConstant.DATE_FORMAT_YEAR_MONTH);

        Date tmp = new Date(id);
        SimpleDateFormat sdf =new SimpleDateFormat("yyyyMM");
        String shardYM = sdf.format(tmp);
        for (String each : collection) {
            if (each.endsWith(shardYM)) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }
}

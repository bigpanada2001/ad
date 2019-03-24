package com.freeco.sharding.config;

import com.freeco.util.DateUtil;
import com.google.common.collect.Range;
import io.shardingsphere.core.api.algorithm.sharding.RangeShardingValue;
import io.shardingsphere.core.api.algorithm.sharding.standard.RangeShardingAlgorithm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static com.freeco.util.DateUtil.addMonth;

/**
 * 范围分片算法
 *
 */
public class OrderRangeShardingAlgorithm  implements RangeShardingAlgorithm<Long> {

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Long> rangeShardingValue) {
        Collection<String> collect = new ArrayList();
        Range<Long> valueRange = rangeShardingValue.getValueRange();
        Date start = new Date(valueRange.lowerEndpoint());
        Date end = new Date(valueRange.upperEndpoint());
//        validate(start, end);
        for (Date d = start; d.before(end) || d.equals(end); d = addMonth(d,1)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            String value = sdf.format(d);
            for (String each : collection) {
                if (each.endsWith(value)) {
                    collect.add(each);
                    break;
                }
            }
        }
        return collect;
    }

    private void validate(Date start, Date end) {
        Date initDate = DateUtil.parseDate("2018-08-01");
        Date nowDate = new Date();
        if (start.before(initDate)) {
            start = initDate;
        }
        if (start.after(nowDate)) {
            start = DateUtil.addDay(nowDate, -1);
        }
        if (end.before(initDate)) {
            end = DateUtil.addDay(initDate, 1);
        }
        if (end.after(nowDate)) {
            end = nowDate;
        }
    }

}

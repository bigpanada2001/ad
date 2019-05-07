package com.freeco.sqltrans;

import org.junit.Before;
import org.junit.Test;

public class SqlTransImplTest {
    SqlTransImpl sqlTrans = null;
    @Before
    public void before() {
        sqlTrans = new SqlTransImpl();
    }

    public static final String multipleAlterSql = "ALTER TABLE `app_ticket`" +
            "DROP INDEX `unique_key` ," +
            "ADD COLUMN `channel_info_id`  int(11) NOT NULL DEFAULT 0 AFTER `create_time`," +
            "ADD COLUMN `advert_place_id`  int(11) NOT NULL DEFAULT 0 AFTER `channel_info_id`," +
            "ADD UNIQUE INDEX `unique_key` (`rso`, `game_id`, `server_id`, `ver`, `os`, `channel_info_id`, `advert_place_id`) USING BTREE ;";

    public static final String multipleAlterSql2 = "ALTER TABLE `app_ticket`" +
            "DROP INDEX `unique_key` ," +
            "ADD COLUMN `channel_info_id`  int(11) NOT NULL DEFAULT 0 AFTER `create_time`," +
            "ADD COLUMN `advert_place_id`  int(11) NOT NULL DEFAULT 0 AFTER `channel_info_id`," +
            "ADD UNIQUE INDEX `unique_key` (`rso`, `game_id`, `server_id`, `ver`, `os`, `channel_info_id`, `advert_place_id`) USING BTREE, "+

            "DROP INDEX `unique_key2` " ;

    public static final String multipleAlterSql3 = "ALTER TABLE `app_ticket`" +
//            "DROP INDEX `unique_key` ," +
            "ADD COLUMN `channel_info_id`  int(11) NOT NULL DEFAULT 0 AFTER `create_time`;";

    @Test
    public void trans1() {
        sqlTrans.translate(multipleAlterSql);
    }
    @Test
    public void trans2() {
        sqlTrans.translate(multipleAlterSql2);
    }

    @Test
    public void trans3() {
        sqlTrans.translate(multipleAlterSql3);
    }
}

package com.freeco.sqltrans;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlTransImpl {
    public void translate(String sql) {

        List<SQLStatement> statements = SQLUtils.parseStatements(sql, "mysql");
        List<SQLStatement> cloneStmt = new ArrayList<>(statements.size());
        // workaround parser not support feature
        for (SQLStatement statement : statements) {
            StringBuilder sqlBuilder = new StringBuilder();
//            Map<SpecType, SpecType> mappings = SpecTypeMappings.getMappings(statement.getDbType(), targetType.name());
            MySqlToVerticaOutputVisitor visitor = new MySqlToVerticaOutputVisitor(sqlBuilder);
            statement.accept(visitor);
//            targetSql = SimpleQuotedIdentifiers.replaceAll(sqlBuilder.toString(), '`', '"');
            System.out.printf("------------:"+sqlBuilder.toString());
        }
//        return cloneStmt;
    }

}

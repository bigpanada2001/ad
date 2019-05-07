package com.freeco.sqltrans;


import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUnique;
import com.alibaba.druid.sql.dialect.mysql.ast.MysqlForeignKey;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRenameTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alibaba.druid.sql.visitor.VisitorFeature;
//import com.duowan.dbriver.sql.translator.SqlTranslatorContext;
//import com.duowan.dbriver.sql.type.SpecType;
//import com.duowan.dbriver.sql.type.SpecTypeMappings;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * mysql to vertica core module
 * <p>
 * 支持度说明<br>
 * <p>
 * 分区：对hash range list和不同函数组合的partition specification，不一定能够在vertica找到对应函数进行转换
 * 1、comment on partition由外层支持
 * 2、partition by range( field1 四则运算或其他操作符）(partition specification)...，结果为 partition by field1
 * 3、partition by range(funcx(field1))(partition specification)...，结果为 partition by field1
 * 4、partition by KEY () partitions xx,如果没有指定分区结果将不包含分区，不会获取主键作为分区
 * 5、忽略分区，对于mysql原表的分区列是时间类型的分区方式。有些日期函数不一定能够映射到，系统允许配置规则忽略DDL翻译建表时分区，
 *
 * <p>
 * using btree
 *
 * @author qmzhang
 */
public class MySqlToVerticaOutputVisitor extends MySqlOutputVisitor {

//    protected Map<SpecType, SpecType> mappings;
//
//    protected SqlTranslatorContext context;

    public MySqlToVerticaOutputVisitor(Appendable appender) {
//    public MySqlToVerticaOutputVisitor(Appendable appender, Map<SpecType, SpecType> mappings, SqlTranslatorContext context) {
        super(appender);
//        this.mappings = mappings;
//        this.context = context;
    }

    public MySqlToVerticaOutputVisitor(Appendable appender, boolean parameterized) {
        super(appender, parameterized);
    }

    /**
     * Syntax
     * CREATE TABLE [ IF NOT EXISTS ] [[db-name.]schema.]table-name {
     * ... ( Column-Definition (table)  [ , ... ] )
     * ... | [ table-constraint ( column_name, ... )]
     * ... | [ column-name-list  (create table)  ] AS [COPY] [ [ AT EPOCH LATEST ]
     * ... | [ AT TIME 'timestamp' ] ] [ / *+ direct * / ] query
     * ... | [ LIKE [[db-name.]schema.]existing-table [ {INCLUDING | EXCLUDING} PROJECTIONS ] ]
     * }
     * ... [ ORDER BY table-column [ , ... ] ]
     * ... [ ENCODED BY column-definition [ , ... ]
     * ... [ Hash-Segmentation-Clause
     * ... | UNSEGMENTED { NODE node | ALL NODES } ]
     * ... [ KSAFE [k_num] ]
     * ... [ PARTITION BY partition-clause ]
     * <p>
     * <p>
     * Column-Definition
     * column-name data-type { ... [ Column-Constraint ]
     * ... [ ENCODING encoding-type ]
     * ... [ ACCESSRANK integer ] }
     * <p>
     * Column-Constraint
     * [ CONSTRAINT constraint-name ] {
     * ...[ NULL | NOT NULL ]
     * ...| PRIMARY KEY
     * ...| REFERENCES table-name [( column [ , ... ] )]
     * ...| UNIQUE
     * ...[ DEFAULT default ]
     * ...[ AUTO_INCREMENT ]
     * ...[ IDENTITY [ ( cache ) | ( start, increment[, cache ] ) ] ]
     * }
     */
    @Override
    public boolean visit(MySqlCreateTableStatement x) {
        if (isPrettyFormat() && x.hasBeforeComment()) {
            printlnComments(x.getBeforeCommentsDirect());
        }

        print0(ucase ? "CREATE TABLE " : "table ");

        if (x.isIfNotExiists()) {
            print0(ucase ? "IF NOT EXISTS " : "if not exists ");
        }

        printTable(x.getName(), true);

        if (x.getLike() != null) {
            print0(ucase ? " LIKE " : " like ");
            x.getLike().accept(this);
        }

        printTableElements(x.getTableElementList());
//        if (!context.isSkipPartitionColumn()) {
//            SQLPartitionBy partitionBy = x.getPartitioning();
//            if (partitionBy != null) {
//                List<SQLExpr> columns = partitionBy.getColumns();
//                if (columns != null && columns.size() > 0) {
//                    println();
//                    print0(ucase ? "PARTITION BY " : "partition by ");
//                    /*
//                     * extract partition column. support function or arithmetic partition.
//                     */
//                    Set<SQLExpr> partitionsColumns = extractPartitionExprsToColumn(columns);
//                    print0(StringUtils.join(partitionsColumns, ","));
//                }
//            }
//        }

        if (x.getSelect() != null) {
            println();
            print0(ucase ? "AS" : "as");
            println();
            x.getSelect().accept(this);
        }
        return false;
    }

    private Set<SQLExpr> extractPartitionExprsToColumn(List<SQLExpr> columns) {
        Set<SQLExpr> partitionsColumns = new HashSet<>();
        for (SQLExpr expr : columns) {
            if (expr instanceof SQLBinaryOpExpr) {
                SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr) expr;
                ArrayList<SQLExpr> tmpExps = new ArrayList<>();
                if (!(binaryOpExpr.getLeft() instanceof SQLValuableExpr)) {
                    tmpExps.add(binaryOpExpr.getLeft());
                }
                if (!(binaryOpExpr.getRight() instanceof SQLValuableExpr)) {
                    tmpExps.add(binaryOpExpr.getRight());
                }
                partitionsColumns.addAll(extractPartitionExprsToColumn(tmpExps));
            } else if (expr instanceof SQLMethodInvokeExpr) {
                List<SQLExpr> exprs = ((SQLMethodInvokeExpr) expr).getParameters();
                partitionsColumns.addAll(extractPartitionExprsToColumn(exprs));
            } else {
                partitionsColumns.add(expr);
            }
        }
        return partitionsColumns;
    }

    @Override
    protected void printTableElements(List<SQLTableElement> tableElementList) {
        int size = tableElementList.size();
        if (size == 0) {
            return;
        }

        print0(" (");

        this.indentCount++;
        println();
        for (int i = 0; i < size; ++i) {
            SQLTableElement element = tableElementList.get(i);
            element.accept(this);

            // ignore key
            if (i != size - 1) {
                SQLTableElement nextElement = tableElementList.get(i + 1);
                if (nextElement instanceof MySqlKey
                        && !(nextElement instanceof MySqlPrimaryKey)
                        && !(nextElement instanceof MySqlUnique)
                        && isAllSqlKey(tableElementList.subList(i + 1, size - 1))) {
                    print(' ');
                } else {
                    print(',');
                }
            }
            /*
            if (this.isPrettyFormat() && element.hasAfterComment()) {
                print(' ');
                printlnComment(element.getAfterCommentsDirect());
            }
            */

            if (i != size - 1) {
                println();
            }
        }
        this.indentCount--;
        println();
        print(')');
    }

    protected boolean isAllSqlKey(List<SQLTableElement> afterColumns) {
        boolean allSkip = true;
        int size = afterColumns.size();
        for (int i = 0; i < size; ++i) {
            SQLTableElement nextElement = afterColumns.get(i);
            if (!(nextElement instanceof MySqlKey)
                    || nextElement instanceof MySqlPrimaryKey
                    || nextElement instanceof MySqlUnique) {
                allSkip = false;
                break;
            }
        }
        return allSkip;
    }


    @Override
    public boolean visit(SQLColumnDefinition x) {
        boolean parameterized = this.parameterized;
        this.parameterized = false;

        x.getName().accept(this);

        SQLDataType dataType = x.getDataType();
        if (dataType != null) {
            print(' ');
            dataType.accept(this);
        }

        for (SQLColumnConstraint item : x.getConstraints()) {
            print(' ');
            item.accept(this);
        }

        final SQLExpr defaultExpr = x.getDefaultExpr();
        if (defaultExpr != null) {
            print0(ucase ? " DEFAULT " : " default ");
            // timestamp value default not allow 0000-00-00 00:00:00
            // 1970-01-01 00:00:00
            if (defaultExpr instanceof SQLCharExpr) {
                String text = ((SQLCharExpr) defaultExpr).getText();
                if ("0000-00-00 00:00:00".equals(text) || "0000-00-00".equals(text)) {
                    if(dataType != null){
                        String typeName = dataType.getName();
                        if (typeName.equalsIgnoreCase(SQLDataType.Constants.TIMESTAMP)
                                || typeName.equalsIgnoreCase("datetime")
                                || typeName.equalsIgnoreCase("date")) {
                            ((SQLCharExpr) defaultExpr).setText("1970-01-01 00:00:00");
                        }
                    }
                }
            }
            defaultExpr.accept(this);
        }

       /* if (x.isAutoIncrement()) {
            print0(ucase ? " AUTO_INCREMENT" : " auto_increment");
        }*/

        /*
        column not support projection column,should use "COMMENT on xxx"
        if (x.getComment() != null) {
            print0(ucase ? " COMMENT " : " comment ");
            x.getComment().accept(this);
        }
        */

        if (x.getAsExpr() != null) {
            print0(ucase ? " AS (" : " as (");
            x.getAsExpr().accept(this);
            print(')');
        }

        this.parameterized = parameterized;
        return false;
    }

    @Override
    public boolean visit(SQLNotNullConstraint x) {
        SQLName name = x.getName();
        if (name != null) {
            print0(ucase ? "CONSTRAINT " : "constraint ");
            name.accept(this);
            print(' ');
        }

        SQLObject xParent = x.getParent();
//        if (xParent instanceof SQLColumnDefinition && context.isConvertDDLColumnStringNotNullToNull()) {
//            String dataTypeName = ((SQLColumnDefinition) xParent).getDataType().getName().toUpperCase();
//            if (dataTypeName.startsWith("CHAR")
//                    || dataTypeName.startsWith("VAR")
//                    || dataTypeName.startsWith("NVAR")) {
//
//                print0(ucase ? " NULL" : " null");
//
//                return false;
//            }
//        }

        print0(ucase ? "NOT NULL" : "not null");
        List<SQLCommentHint> hints = x.hints;
        if (hints != null) {
            print(' ');
            for (SQLCommentHint hint : hints) {
                hint.accept(this);
            }
        }
        return false;

    }

    @Override
    public boolean visit(SQLDataType x) {
        printDataType(x);
        return false;
    }

    @Override
    protected void printDataType(SQLDataType x) {
        boolean parameterized = this.parameterized;
        this.parameterized = false;

        String originalType = x.getName().toUpperCase();
        String convertTypeName = originalType;
//        SpecType mappingType = SpecTypeMappings.getSpecType(mappings, SpecType.valueOf(originalType));
//        if (mappingType != null) {
//            convertTypeName = mappingType.getTypeName();
//        }
//        print0(convertTypeName);
        if (originalType.equals("INT")
                || convertTypeName.equals("INT")
                || originalType.equals("SMALLINT")
                || originalType.equals("TINYINT")
                || originalType.equals("FLOAT")
                || originalType.equals("ENUM")
                || originalType.equals("SET")) {

        } else {
            if (x.getArguments().size() > 0) {
                print('(');
                printAndAccept(x.getArguments(), ", ");
                print(')');
            }
        }

        this.parameterized = parameterized;
    }

    @Override
    public boolean visit(SQLCheck x) {
        if (x.getName() != null) {
            print0(ucase ? "CONSTRAINT " : "constraint ");
            x.getName().accept(this);
            print(' ');
        }
        print0(ucase ? "CHECK (" : "check (");
        this.indentCount++;
        x.getExpr().accept(this);
        this.indentCount--;
        print(')');
        return false;
    }

    /**
     * to schema
     */
    @Override
    public boolean visit(SQLCreateDatabaseStatement x) {
        print0(ucase ? "CREATE SCHEMA " : "create schema ");
        if (x.isIfNotExists()) {
            print0(ucase ? "IF NOT EXISTS " : "if not exists ");
        }
        x.getName().accept(this);

        return false;
    }

    @Override
    protected void printTableSourceExpr(SQLExpr expr) {
        printTable(expr, true);
    }

    protected String getTable(SQLExpr expr, boolean first) {
        StringBuilder namex = new StringBuilder();
        if (expr instanceof SQLIdentifierExpr) {
            SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr) expr;
            final String name = identifierExpr.getName();
//            if (first && context != null) {
//                if (StringUtils.isNotBlank(context.getTargetSchema())) {
//                    namex.append(context.getTargetSchema());
//                    namex.append(".");
//                }
//            }
            namex.append(name);
        } else if (expr instanceof SQLPropertyExpr) {
            SQLPropertyExpr propertyExpr = (SQLPropertyExpr) expr;
            SQLExpr owner = propertyExpr.getOwner();
//            if (context.isConvertToDifferenceSchema()) {
//                namex.append(context.getTargetSchema());
//            } else {
                namex.append(getTable(owner, false));
//            }
            namex.append('.');
            namex.append(propertyExpr.getName());
        } else {
            throw new RuntimeException("not support");
        }
        return namex.toString();
    }

    protected void printTable(SQLExpr expr, boolean first) {
        /*if (expr instanceof SQLIdentifierExpr) {
            SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr) expr;
            final String name = identifierExpr.getName();
            if (first && context != null) {
                if (StringUtils.isNotBlank(context.getTargetSchema())) {
                    print0(context.getTargetSchema());
                    print0(".");
                }
            }
            print0(name);
        } else if (expr instanceof SQLPropertyExpr) {
            SQLPropertyExpr propertyExpr = (SQLPropertyExpr) expr;
            SQLExpr owner = propertyExpr.getOwner();
            if (context.isConvertToDifferenceSchema()) {
                print(context.getTargetSchema());
            } else {
                printTable(owner, false);
            }
            print('.');
            print0(propertyExpr.getName());
        } else {
            throw new RuntimeException("not support");
        }*/
        String x = getTable(expr, first);
        print(x);
    }

    @Override
    public boolean visit(SQLTruncateStatement x) {
        print0(ucase ? "TRUNCATE TABLE " : "truncate table ");
        List<SQLExprTableSource> tableSources = x.getTableSources();
        for (SQLExprTableSource expr : tableSources) {
            printTable(expr.getExpr(), true);
        }
        return false;
    }

    @Override
    public boolean visit(SQLDropTableStatement x) {
        print0(ucase ? "DROP " : "drop ");
        print0(ucase ? "TABLE " : "table ");

        if (x.isIfExists()) {
            print0(ucase ? "IF EXISTS " : "if exists ");
        }

        List<SQLExprTableSource> tableSources = x.getTableSources();
        for (SQLExprTableSource expr : tableSources) {
            printTable(expr.getExpr(), true);
        }

        if (x.isCascade()) {
            printCascade();
        }

        return false;
    }

    /**
     * <p>
     * vertica not support  alter ...column before column
     */
    @Override
    public boolean visit(SQLAlterTableStatement x) {
        // alter statement sql must be identifier
        String prefix = "ALTER TABLE " + getTable(x.getName(), true) + " ";
        this.indentCount++;
        int lastLength = 0;
        for (int i = 0; i < x.getItems().size(); ++i) {
            SQLAlterTableItem item = x.getItems().get(i);
            // support multiple alter statement which not allow in vertica
            // last item print index
            // mark current appender index
            print(prefix);
            item.accept(this);
            print(';');
            int currentLength = ((StringBuilder) appender).length();
            if (currentLength == lastLength + prefix.length() + 1) {
                ((StringBuilder) appender).delete(currentLength - (prefix.length() + 1), currentLength);
            }
            lastLength = ((StringBuilder) appender).length();
        }

        // xxx semicolon
        // ((StringBuilder) appender).deleteCharAt(((StringBuilder) appender).length()-1);
        this.indentCount--;

        return false;
    }

    @Override
    public boolean visit(SQLAlterTableAddColumn x) {
        print0(ucase ? "ADD COLUMN " : "add column ");

        if (x.getColumns().size() > 1) {
            print('(');
        }
        printAndAccept(x.getColumns(), ", ");

        if (x.getColumns().size() > 1) {
            print(')');
        }
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableDropColumnItem x) {
        for (int i = 0; i < x.getColumns().size(); ++i) {
            if (i != 0) {
                print0(", ");
            }
            SQLName column = x.getColumns().get(i);
            print0(ucase ? "DROP COLUMN " : "drop column ");
            column.accept(this);

            if (x.isCascade()) {
                print0(ucase ? " CASCADE" : " cascade");
            }
        }
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableDropForeignKey x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableDropPrimaryKey x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableDropKey x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableEnableKeys x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableDisableKeys x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableDisableConstraint x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableEnableConstraint x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableDropConstraint x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableRenameColumn x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterViewStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableDropIndex x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableAlterColumn x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableAddIndex x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableAddConstraint x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableRename x) {
        //ALTER TABLE [[database.]schema.]table[,…] RENAME TO new-table-name[,…]
        // see MySqlRenameTableStatement
        return false;
    }

    @Override
    public boolean visit(SQLAlterViewRenameStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableAddPartition x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableReOrganizePartition x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableDropPartition x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableRenamePartition x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableSetComment x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableSetLifecycle x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableEnableLifecycle x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableDisableLifecycle x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableTouch x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterDatabaseStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableConvertCharSet x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableCoalescePartition x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableTruncatePartition x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableDiscardPartition x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableImportPartition x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableAnalyzePartition x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableCheckPartition x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableOptimizePartition x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableRebuildPartition x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableRepairPartition x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterSequenceStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterFunctionStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTypeStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterCharacter x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableRenameIndex x) {
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableExchangePartition x) {
        return false;
    }

    @Override
    public boolean visit(SQLDropSequenceStatement x) {
        return false;
    }

    /**
     * The ALTER TABLE RENAME TO statement lets you rename one or more tables.
     * Renaming tables does not change the table OID.
     *
     * @return
     */
    @Override
    public boolean visit(MySqlRenameTableStatement x) {
        print0(ucase ? "ALTER TABLE " : "alter table rename");
        printAndAccept(x.getItems(), ", ");
        return false;
    }

    @Override
    public boolean visit(MySqlRenameTableStatement.Item x) {
        printTable(x.getName(), true);
        print0(ucase ? " RENAME TO " : " rename to ");
        printTable(x.getTo(), true);
        return false;
    }

    @Override
    public boolean visit(MySqlPrimaryKey x) {
        if (x.getName() != null) {
            print0(ucase ? "CONSTRAINT " : "constraint ");
            x.getName().accept(this);
            print(' ');
        }

        print0(ucase ? "PRIMARY KEY" : "primary key");

        print0(" (");

        for (int i = 0, size = x.getColumns().size(); i < size; ++i) {
            if (i != 0) {
                print0(", ");
            }
            x.getColumns().get(i).accept(this);
        }
        print(')');

        return false;
    }

    @Override
    public boolean visit(MySqlUnique x) {
        if (x.isHasConstaint()) {
            print0(ucase ? "CONSTRAINT " : "constraint ");
            if (x.getName() != null) {
                x.getName().accept(this);
                print(' ');
            }
        } else if (x.getName() != null) {
            print0(ucase ? "CONSTRAINT " : "constraint ");
            SQLName name = x.getName();
            if (name != null) {
                print(' ');
                name.accept(this);
            }
        }

        print0(ucase ? " UNIQUE " : "unique");

        print0(" (");
        printAndAccept(x.getColumns(), ", ");
        print(')');

        print0(" ENABLED ");

        return false;
    }

    @Override
    public boolean visit(MysqlForeignKey x) {
        return false;
    }


    /**
     * key is nothing
     */
    @Override
    public boolean visit(MySqlKey x) {
        return false;
    }

    @Override
    public boolean visit(SQLCharacterDataType x) {
        printDataType(x);

        return false;
    }


    //////////////////////////////////////////////////////////////DML

    /**
     * 只支持简单的翻译，vertica并不支持多个值的插入
     * 将单个多值的插入，翻译为多个单值的插入
     */
    @Override
    public boolean visit(MySqlInsertStatement x) {
        print0(ucase ? "INSERT INTO " : "insert ");

        SQLExprTableSource tableSource = x.getTableSource();
        printTable(tableSource.getName(), true);

        String columnsString = x.getColumnsString();
        if (columnsString != null) {
            if (!isEnabled(VisitorFeature.OutputSkipInsertColumnsString)) {
                print0(columnsString);
            }
        } else {
            List<SQLExpr> columns = x.getColumns();
            if (columns.size() > 0) {
                this.indentCount++;
                print0(" (");
                for (int i = 0, size = columns.size(); i < size; ++i) {
                    if (i != 0) {
                        if (i % 5 == 0) {
                            println();
                        }
                        print0(", ");
                    }

                    SQLExpr column = columns.get(i);
                    if (column instanceof SQLIdentifierExpr) {
                        print0(((SQLIdentifierExpr) column).getName());
                    } else {
                        printExpr(column);
                    }
                }
                print(')');
                this.indentCount--;
            }
        }

        List<SQLInsertStatement.ValuesClause> valuesList = x.getValuesList();
        if (!valuesList.isEmpty()) {
            println();
            printValuesList(valuesList);
        }

        if (x.getQuery() != null) {
            println();
            x.getQuery().accept(this);
        }
        return false;
    }

    @Override
    protected void printValuesList(List<SQLInsertStatement.ValuesClause> valuesList) {
        print0(ucase ? "VALUES " : "values ");
        if (valuesList.size() > 1) {
            this.indentCount++;
        }
        String insertPrefix = this.appender.toString();
        for (int i = 0, size = valuesList.size(); i < size; ++i) {
            if (i != 0) {
                print(';');
                println();
                print(insertPrefix);
            }

            SQLInsertStatement.ValuesClause item = valuesList.get(i);
            visit(item);
        }

        if (valuesList.size() > 1) {
            this.indentCount--;
        }
    }

    @Override
    public boolean visit(MySqlUpdateStatement x) {
        List<SQLExpr> returning = x.getReturning();
        if (returning != null && returning.size() > 0) {
            print0(ucase ? "SELECT " : "select ");
            printAndAccept(returning, ", ");
            println();
            print0(ucase ? "FROM " : "from ");
        }

        print0(ucase ? "UPDATE " : "update ");

        if (x.isLowPriority()) {
            print0(ucase ? "LOW_PRIORITY " : "low_priority ");
        }

        if (x.isIgnore()) {
            print0(ucase ? "IGNORE " : "ignore ");
        }


        if (x.getHints() != null && x.getHints().size() > 0) {
            printAndAccept(x.getHints(), " ");
            print0(" ");
        }

        if (x.isCommitOnSuccess()) {
            print0(ucase ? "COMMIT_ON_SUCCESS " : "commit_on_success ");
        }

        if (x.isRollBackOnFail()) {
            print0(ucase ? "ROLLBACK_ON_FAIL " : "rollback_on_fail ");
        }

        if (x.isQueryOnPk()) {
            print0(ucase ? "QUEUE_ON_PK " : "queue_on_pk ");
        }

        SQLExpr targetAffectRow = x.getTargetAffectRow();
        if (targetAffectRow != null) {
            print0(ucase ? "TARGET_AFFECT_ROW " : "target_affect_row ");
            printExpr(targetAffectRow);
            print(' ');
        }

        if (x.isForceAllPartitions()) {
            print0(ucase ? "FORCE ALL PARTITIONS " : "force all partitions ");
        } else {
            SQLName partition = x.getForcePartition();
            if (partition != null) {
                print0(ucase ? "FORCE PARTITION " : "force partition ");
                printExpr(partition);
                print(' ');
            }
        }

        printTableSource(x.getTableSource());

        println();
        print0(ucase ? "SET " : "set ");
        List<SQLUpdateSetItem> items = x.getItems();
        for (int i = 0, size = items.size(); i < size; ++i) {
            if (i != 0) {
                print0(", ");
            }
            SQLUpdateSetItem item = items.get(i);
            visit(item);
        }

        SQLExpr where = x.getWhere();
        if (where != null) {
            println();
            indentCount++;
            print0(ucase ? "WHERE " : "where ");
            printExpr(where);
            indentCount--;
        }

        SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            println();
            visit(orderBy);
        }

        SQLLimit limit = x.getLimit();
        if (limit != null) {
            println();
            visit(limit);
        }
        return false;
    }


    @Override
    public boolean visit(SQLDropViewStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLDropMaterializedViewStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLDropEventStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLDropIndexStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLDropLogFileGroupStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLDropServerStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLDropTypeStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLDropSynonymStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLDropTriggerStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLDropUserStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLDropFunctionStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLDropTableSpaceStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLDropProcedureStatement x) {
        return false;
    }

    @Override
    public boolean visit(SQLErrorLoggingClause x) {
        return false;
    }
}


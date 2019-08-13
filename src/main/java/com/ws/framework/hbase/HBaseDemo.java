package com.ws.framework.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * @Description:
 * @Date: 2019/8/13 0013 11:18
 * HBase api
 */
public class HBaseDemo {
    //表名
    public static final String TABLE_NAME = "hot_shop";
    //列族
    public static final String FAMILY_NAME = "info";
    //列名
    public static final String COLUMN_NAME = "describe";

    public static Configuration conf;
    private Connection connection;
    private Admin admin;

    static {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.property.port", "2181");
        conf.set("hbase.zookeeper.quorum", "server-1,server-2,server-3");
        conf.set("hbase.master", "server-1");
        conf.set("hbase.master.port", "16000");
    }

    @Before
    public void before() throws Exception {
        //旧版本的HBaseAdmin不建议使用了
        connection = ConnectionFactory.createConnection(conf);
        admin = connection.getAdmin();
    }

    @Test
    public void createTable() {
        try {
            //表
            TableName tableName = TableName.valueOf(TABLE_NAME);

            boolean exists = admin.tableExists(tableName);

            if (exists) {
                System.out.println("该表已经存在 -> 删除重新创建!");
                admin.disableTable(tableName);
                admin.deleteTable(tableName);
            }

            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
            //列族
            HColumnDescriptor info = new HColumnDescriptor(FAMILY_NAME);
            tableDescriptor.addFamily(info);
            //列族
            HColumnDescriptor describe = new HColumnDescriptor(COLUMN_NAME);
            tableDescriptor.addFamily(describe);

            admin.createTable(tableDescriptor);
            admin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pushRow() {
        try {

            TableName hot_shop = TableName.valueOf(TABLE_NAME);

            Table table = connection.getTable(hot_shop);

            List<Put> putList = new ArrayList<>(10);
            Random random = new SecureRandom();
            IntStream.range(0, 10).forEach(p -> {
                Put put = new Put(String.valueOf(random.nextInt(1000000000)).getBytes());
                put.addColumn(FAMILY_NAME.getBytes(), COLUMN_NAME.getBytes(), ("Hello " + p).getBytes());
                putList.add(put);
            });

            table.put(putList);
            table.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getRow() {

        try {
            TableName hotShop = TableName.valueOf(TABLE_NAME);

            Table table = connection.getTable(hotShop);
            //指定rowKey
            Get get = new Get(String.valueOf(170051776).getBytes());

            Result result = table.get(get);

            List<Cell> cells = result.listCells();
            System.out.println("rowKey -> " + new String(result.getRow()));
            cells.stream().forEach(p -> System.out.println(CellUtil.toString(p, true)));
            table.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void scanTable() {

        try {
            TableName hotShop = TableName.valueOf(TABLE_NAME);
            Table table = connection.getTable(hotShop);
            Scan scan = new Scan();
            scan.addColumn(FAMILY_NAME.getBytes(), COLUMN_NAME.getBytes());
            //过滤器
            Filter filter = new ValueFilter(CompareFilter.CompareOp.LESS, new BinaryComparator("Hello 6".getBytes()));
            scan.setFilter(filter);

            ResultScanner scanner = table.getScanner(scan);
            Result result;
            while ((result = scanner.next()) != null) {
                byte[] row = result.getRow();
                byte[] bytes = result.getValue(FAMILY_NAME.getBytes(), COLUMN_NAME.getBytes());
                System.out.println("row -> " + Bytes.toString(row) + " value -> " + Bytes.toString(bytes));
            }

            table.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteRow() {

        try {
            TableName tableName = TableName.valueOf(TABLE_NAME);

            Table table = connection.getTable(tableName);
            //指定删除rowKey
            Delete delete = new Delete(String.valueOf(142431551).getBytes());
            //只删除某一列数据
            delete.addColumn(FAMILY_NAME.getBytes(), "name".getBytes());
            table.delete(delete);
            table.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

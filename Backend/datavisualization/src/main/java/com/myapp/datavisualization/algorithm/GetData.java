package com.myapp.datavisualization.algorithm;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GetData {

    public static List<String> GetDataFromDataBase(){

        //jdbc.url=jdbc:mysql://localhost:3306/database
        String url = "jdbc:db2://steen.informatik.rwth-aachen.de:50020/mobsos";
        //查找的表名
//    String table = "mobsos.MONITORING";

        //spark 数据库的操作
        // 增加数据库的用户名(user)密码(password),指定test数据库的驱动(driver)
//        Properties connectionProperties = new Properties();
//        connectionProperties.put("user","stdb2v95");
//        connectionProperties.put("password","stnV95DB");
//        connectionProperties.put("driver","com.ibm.db2.jcc.DB2Driver");
//
//        //SparkJdbc读取Postgresql的products表内容
//        System.out.println("读取test数据库中的user_test表内容");
//        // 读取表中所有数据
//        Dataset jdbcDF = sqlContext.read().jdbc(url,table,connectionProperties).select("SESSION_ID","METHOD_NAME");
//        //groupBy Session_ID
////        jdbcDF.groupBy("SESSION_ID");
////        jdbcDF.select("METHOD_NAME").show();
//
//        //drop null
//       Dataset noNullData=jdbcDF.filter(jdbcDF.col("METHOD_NAME").isNotNull());
//       noNullData.show();

        // 用java实现数据库的操作
        Connection connection = null;
        Statement stmt=null;
        ResultSet rs=null;
        List<String> MethodNode=new ArrayList<>();

        try {
            //Load class into memory
            Class.forName("com.ibm.db2.jcc.DB2Driver");
            //Establish connection
            connection = DriverManager.getConnection(url, "stdb2v95","stnV95DB");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                System.out.println("Connected successfully.");
                try {
                    stmt = connection.createStatement();

                    //sql从数据库里获取数据
                    rs = stmt.executeQuery("select SESSION_ID,replace(replace(xml2clob(xmlagg(xmlelement(NAME a, METHOD_NAME||','))),'<A>',''),'</A>',' ') FROM\n" +
                            "mobsos.MONITORING where METHOD_NAME is not null and METHOD_NAME!='instantiateContext' and METHOD_NAME!='continueConnection' and METHOD_NAME!='testConnection' GROUP BY SESSION_ID");

                    String s;
                    while (rs.next()) {

                        s=rs.getString("2");
                        MethodNode.add(s.substring(0,s.length()-1));


                    }

                }catch(SQLException e){
                    e.printStackTrace();
                }

            }

        }
//
//       System.out.println(MethodNode+"\n");
        //list的形式返回数据
        return MethodNode;
    }
}

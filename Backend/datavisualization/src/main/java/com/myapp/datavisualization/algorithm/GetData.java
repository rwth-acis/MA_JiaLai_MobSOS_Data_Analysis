package com.myapp.datavisualization.algorithm;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GetData {


    static String user;
    static String password;
    static String url;
    static String driver;
    static String query;

    GetData(String url, String password, String user, String driver,String query){
        this.url=url;
        this.password=password;
        this.user=user;
        this.driver=driver;
        this.query=query;

    }

    public static List<String> GetDataFromDataBase(){

        //jdbc.url=jdbc:mysql://localhost:3306/database
//        String url = "jdbc:db2://steen.informatik.rwth-aachen.de:50020/mobsos";

        // 用java实现数据库的操作
        Connection connection = null;
        Statement stmt=null;
        ResultSet rs=null;
        List<String> MethodNode=new ArrayList<>();

        try {
            //Load class into memory
            Class.forName("com.ibm.db2.jcc.DB2Driver");
            //Establish connection
            connection = DriverManager.getConnection(url, user,password);

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
                    rs = stmt.executeQuery(query);

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

        //list的形式返回数据
        return MethodNode;
    }
}

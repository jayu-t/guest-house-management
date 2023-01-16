
package test.test2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DBTest {
    
    public static void main(String args[])
    {
        insertRoomData("Non AC","free",2000);
    }
    
    public static void insertRoomData(String type,String status,double price){
        Connection con;
        Statement stmt;
        PreparedStatement pstmt;
        try
        {
            int rno;
            System.out.println(type+" "+status+" "+price);
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/guesthouse1","root","");
            System.out.println("Connected");
            stmt=con.createStatement();
            pstmt=con.prepareStatement("insert into room (type,status,price) values(?,?,?)");
            pstmt.setString(1, type);
            pstmt.setString(2, status);
            pstmt.setDouble(3,price);
            pstmt.executeUpdate();
            System.out.println("Record insered");
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
                System.out.println(e);
        }
    }
}

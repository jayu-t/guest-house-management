/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guesthouse;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Jayesh
 */
public class GuestHouse {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        GuestHouse h=new GuestHouse();
        h.createDB();
        HomeScreen home=new HomeScreen();
        home.setVisible(true);
    }

    public void createDB() throws ClassNotFoundException, SQLException
    {
        Connection con=this.connect();
        Statement stmt=null;
        int i=0;

        //CREATE TABLES
        if(i==1)
        {
            try {
                //CREATE TABLE GUEST
                stmt.executeUpdate("create table guest(gno int PRIMARY KEY AUTO_INCREMENT, address varchar(256), contact varchar(12), dob Date, gender varchar(6), rno int, check_in_date Date, check_out_date Date, status varchar(20))");
                System.out.println("Guest Table created");
            }
            catch(Exception e)
            {
                System.out.println(e);
            }

            try {
                //CREATE TABLE ROOM
                stmt.executeUpdate("create table room(rno int PRIMARY KEY AUTO_INCREMENT, type varchar(6), status varchar(4), price double)");
                System.out.println("Room Table created");
            }
            catch(Exception e)
            {
                System.out.println(e);
            }

            try {
                //CREATE TABLE BILL
                stmt.executeUpdate("create table bill(bno int, gno int, rno int, bill_amt double)");
                System.out.println("Table created");
            }
            catch(Exception e)
            {
                System.out.println(e);
            }

            try {
                //CREATE TABLE Employee
                stmt.executeUpdate("create table employee(name varchar(256),address varchar(256), contact varchar(12), dob Date, gender varchar(6), edj varchar(256), date_of_joining Date, salary double)");
                System.out.println("Employee created");
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
        }
        stmt.close();
        con.close();
    }


    public Connection connect() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Driver loaded");
        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/guesthouse","root","");
        System.out.println("Connected");
        return(con);
    }

    public void insertRoomData(String type,double price){
        Connection con;
        Statement stmt;
        PreparedStatement pstmt;
        String status="free";
        try
        {
            System.out.println(type+" "+price);
            con=this.connect();
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

    public void updateRoom(int rno,String type,double price){
        Connection con;
        Statement stmt;
        PreparedStatement pstmt;
        try
        {
            System.out.println(rno);
            con=this.connect();
            stmt=con.createStatement();
            pstmt=con.prepareStatement("update room set status='free',type=?,price=? where rno=?");
            pstmt.setString(1, type);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, rno);
            pstmt.executeUpdate();
            System.out.println("Record Updated");
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    void removeRoom(int rno){
        Connection con;
        Statement stmt;
        PreparedStatement pstmt;
        try
        {
            System.out.println(rno);
            con=this.connect();
            stmt=con.createStatement();
            pstmt=con.prepareStatement("delete from room where rno=?");
            pstmt.setInt(1, rno);
            pstmt.executeUpdate();
            System.out.println("Record Deleted");
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    void checkAvailability(String type,String cid,String cod){
        Connection con;
        Statement stmt;
        PreparedStatement pstmt;
        ResultSet rs;
        try{
            con=this.connect();
            stmt=con.createStatement();
            pstmt=con.prepareStatement("select * from room where status='free' and type=? ");
            pstmt.setString(1,type);
            rs=pstmt.executeQuery();
            int i=0;
            while(rs.next()){
                i++;
                int rno=rs.getInt(1);
                int yn=JOptionPane.showConfirmDialog(null, "Room available. You want to book room","Booking",JOptionPane.YES_NO_OPTION);
                if(yn==0) {
                    bookRoom(rno,cid,cod);
                    break;
                }
                else {
                    /*HomeScreen home=new HomeScreen();
                    home.setVisible(true);
                    CheckAvailability ca=new CheckAvailability();
                    ca.setVisible(false);
                    break;*/
                    return;
                }
            }
            if(i==0) {
                JOptionPane.showMessageDialog(null, "Soory! Room not available", "Room Info", JOptionPane.INFORMATION_MESSAGE);
                /*HomeScreen home=new HomeScreen();
                home.setVisible(true);*/
            }

        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    void bookRoom(int rno,String cid,String cod) {
        Guest gf=new Guest(rno,cid,cod);
        gf.setVisible(true);
        /*Guest guest=new Guest();
        guest.getGuest(rno,cid,cod);*/
    }

    public void insertGuestData(String name,String address,String contact,String dob,String gender,int rno,String cid,String cod){
        Connection con;
        Statement stmt;
        PreparedStatement pstmt;
        try
        {
            String status="in";
            System.out.println(name+" "+address+" "+contact+" "+dob+" "+gender+" "+rno+" "+cid+" "+cod+" "+status);
            con=connect();
            stmt=con.createStatement();
            pstmt=con.prepareStatement("insert into guest(name,address,contact,dob,gender,rno,check_in_date,check_out_date,status) values(?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3,contact);
            pstmt.setString(4,dob);
            pstmt.setString(5,gender);
            pstmt.setInt(6,rno);
            pstmt.setString(7,cid);
            pstmt.setString(8,cod);
            pstmt.setString(9,status);
            pstmt.executeUpdate();
            System.out.println("Record insered");
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        updateRoomDataAfterBooking(rno);
    }

    public boolean guestIsExist(int gno) throws ClassNotFoundException, SQLException
    {
        Connection con;
        Statement stmt;
        PreparedStatement pstmt;
        ResultSet rs;
        int i=0;
        con=this.connect();
        pstmt=con.prepareStatement("select * from guest where gno=? and status='in' ");
        pstmt.setInt(1,gno);
        rs=pstmt.executeQuery();
        while(rs.next())
        {
            i++;
        }
        if(i!=0)
            return(true);
        else
            return(false);
    }

    public ArrayList showGuest(int gno)
    {
        Connection con;
        Statement stmt;
        PreparedStatement pstmt;
        ResultSet rs,rs1;
        try{
            con=this.connect();
            stmt=con.createStatement();
            pstmt=con.prepareStatement("select * from guest where gno=?");
            pstmt.setInt(1,gno);
            rs=pstmt.executeQuery();
            int i=0;
            while(rs.next()){
                i++;
                String name=rs.getString(2);
                String address=rs.getString(3);
                String contact=rs.getString(4);
                Date dob=rs.getDate(5);
                String gender=rs.getString(6);

                ArrayList guest=new ArrayList(5);
                guest.add(name);
                guest.add(address);
                guest.add(contact);
                guest.add(dob);
                guest.add(gender);
                return(guest);
            }
            if(i==0) {
                JOptionPane.showMessageDialog(null, "Soory! No guest found. pleaze check your guest ID", "Room Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    public void updateGuestRecord(int gno,String name,String address,String contact,String dob,String gender){
        Connection con;
        Statement stmt;
        PreparedStatement pstmt;
        try
        {
            System.out.println(gno+" "+name+" "+address+" "+contact+" "+dob+" "+gender);
            con=this.connect();
            stmt=con.createStatement();
            pstmt=con.prepareStatement("update guest set name=?,address=?,contact=?,dob=?,gender=? where gno=?");
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3,contact);
            pstmt.setString(4,dob);
            pstmt.setString(5,gender);
            pstmt.setInt(6, gno);
            pstmt.executeUpdate();
            System.out.println("Record Updated");
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public void generateBill(int gno) {
        Connection con;
        Statement stmt;
        PreparedStatement pstmt;
        ResultSet rs,rs1;
        try{
            con=this.connect();
            stmt=con.createStatement();
            pstmt=con.prepareStatement("select * from guest where gno=?");
            pstmt.setInt(1,gno);
            rs=pstmt.executeQuery();
            int i=0;
            while(rs.next()){
                i++;
                String name=rs.getString(2);
                String address=rs.getString(3);
                String contact=rs.getString(4);
                //String dob=rs.getString(5);
                //String gender=rs.getString(6);
                int rno=rs.getInt(7);
                String cid=rs.getString(8);
                String cod=rs.getString(9);
                double price=0;
                String type=null;

                pstmt=con.prepareStatement("select * from room where rno=?");
                pstmt.setInt(1,rno);
                rs1=pstmt.executeQuery();
                while(rs1.next()) {
                    type=rs1.getString(2);
                    price=rs1.getDouble(4);
                }

                MainBill home=new MainBill(gno,name,address,contact,rno,type,cid,cod,price);
                home.setVisible(true);
            }
            if(i==0) {
                JOptionPane.showMessageDialog(null, "Soory! No guest found. pleaze check your guest ID", "Room Info", JOptionPane.INFORMATION_MESSAGE);

            }

        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public int cancelBooking(int gno){
        Connection con;
        Statement stmt;
        PreparedStatement pstmt;
        int n=0;
        try
        {
            System.out.println(gno);
            con=this.connect();
            stmt=con.createStatement();
            pstmt=con.prepareStatement("select * from guest where gno=? ");
            pstmt.setInt(1, gno);
            ResultSet rs=pstmt.executeQuery();
            int rno=0;
            while(rs.next()) {
                n++;
                rno=rs.getInt(7);
            }
            if(rno!=0) {
                pstmt=con.prepareStatement("update room set status='free' where rno=?");
                pstmt.setInt(1, rno);
                pstmt.executeUpdate();
                System.out.println("Room Record Upadated");
            }
            pstmt=con.prepareStatement("update guest set status='out' where gno=?");
            pstmt.setInt(1, gno);
            pstmt.executeUpdate();
            System.out.println("Guest Record updated after cancel booking");
            System.out.println("Booking Cancel");
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return n;
    }

    public void updateRoomDataAfterBooking(int rno){
        Connection con;
        Statement stmt;
        PreparedStatement pstmt;
        try
        {
            System.out.println(rno);
            con=this.connect();
            stmt=con.createStatement();
            pstmt=con.prepareStatement("update room set status='book' where rno=?");
            pstmt.setInt(1, rno);
            pstmt.executeUpdate();
            System.out.println("Room record updated after booking");
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public int findNumDays(String a,String b) {
        //String a="2017-11-01 00:00:00";
        String y=a.substring(0, 4);
        int yy=Integer.parseInt(y);
        System.out.println("year "+yy);

        String m=a.substring(5, 7);
        int mm=Integer.parseInt(m);
        System.out.println("Month "+mm);

        String d=a.substring(8, 10);
        int dd=Integer.parseInt(d);
        System.out.println("Date "+dd);

        //String b="2017-12-03 00:00:00";
        String cy=b.substring(0, 4);
        int cyy=Integer.parseInt(cy);
        System.out.println("Check out year "+cyy);

        String cm=b.substring(5, 7);
        int cmm=Integer.parseInt(cm);
        System.out.println("Check out Month "+cmm);

        String cd=b.substring(8, 10);
        int cdd=Integer.parseInt(cd);
        System.out.println("Check out Date "+cdd);

        int i=0;
        if(yy==cyy) {
            System.out.println("Year same");
            if(mm==cmm) {
                System.out.println("Month same");
                if(dd!=cdd) {
                    System.out.println("Different day");
                    while(dd!=cdd) {
                        i++;
                        dd++;
                    }
                }
                else {
                    System.out.println("Day same");
                    i++;
                }
            }
            else {

            }
        }
        System.out.println("Days="+i);
        return i;
    }

    void insertBillRecord(int gno,int rno,double bill) {
        Connection con;
        Statement stmt;
        PreparedStatement pstmt;
        try
        {
            System.out.println(gno+" "+rno+" "+bill);
            con=this.connect();
            stmt=con.createStatement();
            pstmt=con.prepareStatement("insert into bill(gno,rno,bill_amt) values(?,?,?)");
            pstmt.setInt(1, gno);
            pstmt.setInt(2, rno);
            pstmt.setDouble(3, bill);
            pstmt.executeUpdate();
            System.out.println("Record insered");
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        freeRoomAfterCheckOut(rno);
        updateGuestAfterCheckOut(gno);
    }

    void freeRoomAfterCheckOut(int rno) {
        Connection con;
        Statement stmt;
        PreparedStatement pstmt;
        try
        {
            System.out.println(rno);
            con=this.connect();
            stmt=con.createStatement();
            pstmt=con.prepareStatement("update room set status='free' where rno=?");
            pstmt.setInt(1, rno);
            pstmt.executeUpdate();
            System.out.println("Room Free");
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    void updateGuestAfterCheckOut(int gno) {
        Connection con;
        Statement stmt;
        PreparedStatement pstmt;
        try
        {
            System.out.println(gno);
            con=this.connect();
            stmt=con.createStatement();
            pstmt=con.prepareStatement("update guest set status='out' where gno=?");
            pstmt.setInt(1, gno);
            pstmt.executeUpdate();
            System.out.println("Guest Record Updated");
            System.out.println("Check out successfully");
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public void insertEmployeeData(String name,String address,String contact,String dob,String gender,String edj,String dateOfJoining,double salary){
        Connection con;
        Statement stmt;
        PreparedStatement pstmt;
        try
        {
            System.out.println(name+" "+address+" "+contact+" "+dob+" "+gender+" "+edj+" "+dateOfJoining+" "+salary);
            con=connect();
            stmt=con.createStatement();
            pstmt=con.prepareStatement("insert into employee(name,address,contact,dob,gender,edj,date_of_joining,salary) values(?,?,?,?,?,?,?,?)");
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3,contact);
            pstmt.setString(4,dob);
            pstmt.setString(5,gender);
            pstmt.setString(6,edj);
            pstmt.setString(7,dateOfJoining);
            pstmt.setDouble(8,salary);
            pstmt.executeUpdate();
            System.out.println("Record insered");
            JOptionPane.showMessageDialog(null, "Your registration successfully completed", "Succed", JOptionPane.INFORMATION_MESSAGE);
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
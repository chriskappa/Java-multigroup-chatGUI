/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Xristakos
 */

public class db {
    

    
    
  public  Connection getConnection(){
      try {
          return  DriverManager.getConnection("jdbc:mysql://root@localhost/chatdb");
      } catch (SQLException e) {
          System.out.println("Couldnt Connect to DB");
      }
    return null;
  }
  
   public ResultSet getUser(String userName,String userPassword){
    try {
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT * FROM users WHERE userName=? AND userPassword=?";
        Connection con = getConnection();
        ps = con.prepareStatement(query);
        ps.setString(1, userName);
        ps.setString(2, userPassword);
        return rs = ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("Database Failure");
        }
        return null;
    }



}


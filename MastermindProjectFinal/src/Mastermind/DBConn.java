/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mastermind;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 To connect to the database mydb, user=myuser and password=mypass
 */
public class DBConn {

    public Connection DBConnect() throws SQLException {
        //String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        //String connectionURL = "jdbc:derby:C:\\Users\\Elev\\AppData\\Roaming\\NetBeans\\Derby\\mydb;create=true;user=myuser;password=mypass";
        String connectionURL = "jdbc:derby:mydb;create=true;user=myuser;password=mypass";
        
        return DriverManager.getConnection(connectionURL);
    }
}

package com.globokas.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author pvasquez
 */
public class SqlConection {

    // TODO Auto-generated method stub
    public Connection SQLServerConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("No encuentra driver");
            cnfe.printStackTrace();
            System.exit(1);
        }
        Connection c = null;

        try {

            String ipDataBase = ConfigApp.getValue("SQL_IP");
            String databaseNombre = ConfigApp.getValue("SQL_BD");
            String userDatabase = ConfigApp.getValue("SQL_USER");
//            String passDatabase = ConfigApp.getValue("SQL_PASSWORD");
            String passDatabase = "PassGestion$";

            c = DriverManager.getConnection("jdbc:sqlserver://" + ipDataBase + ";databaseName=" + databaseNombre + ";user=" + userDatabase + ";password=" + passDatabase + ";");

        } catch (SQLException se) {
            System.out.println("No conecto!");
            se.printStackTrace();
            System.exit(1);
        }

        if (c != null) {
//            System.out.println("Conecto!");
        } else {
            System.out.println("No conecto!");
        }
        return c;

    }

}

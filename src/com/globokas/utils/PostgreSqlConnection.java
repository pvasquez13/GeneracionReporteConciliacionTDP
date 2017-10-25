/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globokas.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 *
 * @author pvasquez
 */
public class PostgreSqlConnection {

    private static final Logger logger = Logger.getLogger(PostgreSqlConnection.class);

    public static Connection SQLPostgresConnection() throws SQLException {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException cnfe) {
            logger.trace("Couldn't find the driver!");
            logger.trace("Let'f print a stack trace, and exit.");
            cnfe.printStackTrace();
            System.exit(1);
        }

        logger.trace(
                "Registered the driver ok, so let'f make a connection.");

        Connection c = null;

        try {
 
            String ip = ConfigApp.getValue("BANCA_IP");
            String database = ConfigApp.getValue("BANCA_BD");
            String userDatabase = ConfigApp.getValue("BANCA_USER");
            String passwordDatabase = ConfigApp.getValue("BANCA_PSW");

            c = DriverManager.getConnection("jdbc:postgresql://"+ip+"/"+database, userDatabase, passwordDatabase);

        } catch (SQLException se) {
            logger.trace("Couldn't connect: print out a stack trace and exit.");
            se.printStackTrace();
            System.exit(1);
        }

        if (c != null) {
            logger.trace("We connected to the database! Postgres");
        } else {
            logger.trace("We should never get here.");
        }
        return c;

    }

    public static void close(ResultSet rst, Statement stm, Connection cnn) {
        close(rst);
        close(stm);
        close(cnn);
    }

    public static void close(ResultSet rst) {
        try {
            if (rst != null) {
                rst.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void close(Statement stm) {
        try {
            if (stm != null) {
                stm.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void close(Connection cnn) {
        try {
            if (cnn != null) {
                cnn.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}

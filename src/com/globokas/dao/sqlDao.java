/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globokas.dao;

import com.globokas.bean.LogTransaccional;
import com.globokas.utils.SqlConection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pvasquez
 */
public class sqlDao {

    public List<LogTransaccional> getLogTransaccionalSQL(String fechaHoy, String fechaAyer) {

        List<LogTransaccional> transaccionesList = new ArrayList<LogTransaccional>();
        StringBuffer sql = new StringBuffer();
        ResultSet rs = null;
        Connection conn = null;

        try {
            sql.append("select log_fecope as fecha"
                + " ,log_codcom+SUBSTRING(log_codtie,2,2)+log_codter as terminal"
                + " ,log_opeakt as numeroOperacion"
                + " ,log_resope as respuestaOperacion"
                + " from log_transaccional_PAYSAFE where log_fecope >='" + fechaAyer + "'"
                + " AND log_fecope <= '" + fechaHoy + "'"
                + " AND log_resope = '0' "
                + "  AND log_destra LIKE 'Pago%Recaudo%' AND log_codtra NOT LIKE 'KX%' ");

        System.out.println("SQL="+sql);

            SqlConection c = new SqlConection();
            conn = c.SQLServerConnection();
            PreparedStatement ps;
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                LogTransaccional logTransaccion = new LogTransaccional();
                logTransaccion.setFecha(rs.getString("fecha"));
                logTransaccion.setTerminal(rs.getString("terminal"));
                logTransaccion.setNumeroOperacion(rs.getString("numeroOperacion"));
                logTransaccion.setEstadoOperacionInt(rs.getInt("respuestaOperacion"));
                transaccionesList.add(logTransaccion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return transaccionesList;

    }

}

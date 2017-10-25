package com.globokas.dao;

import com.globokas.bean.PagoTramaBean;
import com.globokas.utils.OraConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author pvasquez
 */
public class GknIsoDao {
    
    private static final Logger logger = Logger.getLogger(GknIsoDao.class);

    public List<PagoTramaBean> getPagoTrama(String fechaHoy, String fechaAyer) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<PagoTramaBean> pagoTramaList = new ArrayList<PagoTramaBean>();

        String sql = "SELECT TERMINAL, TRACE, FECHA, TRAMATCC FROM PAGO_TRAMA"
                + " WHERE TIPO='RS'"
                + " AND SUBSTR(TRAMATCC,105,8) = '"+fechaHoy +"'"
                + " AND FECHA>='" + fechaAyer + "'";

        System.out.println("ORACLESQL="+sql);

        try {

            conn = OraConnection.OracleConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                PagoTramaBean bean = new PagoTramaBean();
                bean.setTerminal(rs.getString("TERMINAL"));
                bean.setTrace(rs.getString("TRACE"));
                bean.setFecha(rs.getString("FECHA"));
                bean.setTramatcc(rs.getString("TRAMATCC"));
                pagoTramaList.add(bean);
            }
        } catch (Exception e) {
            logger.info("Error al traer PAGO_TRAMA",e);
        } finally {
            OraConnection.close(rs, stmt, conn);
        }

        return pagoTramaList;
    }

}

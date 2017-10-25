/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.globokas.bean;

/**
 *
 * @author pvasquez
 */
public class LogTransaccional {
    
    private String fecha;
    private String terminal;
    private String numeroOperacion;
    private Integer estadoOperacionInt;
    private String trama;

    public LogTransaccional() {
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getNumeroOperacion() {
        return numeroOperacion;
    }

    public void setNumeroOperacion(String numeroOperacion) {
        this.numeroOperacion = numeroOperacion;
    }

    public Integer getEstadoOperacionInt() {
        return estadoOperacionInt;
    }

    public void setEstadoOperacionInt(Integer estadoOperacionInt) {
        this.estadoOperacionInt = estadoOperacionInt;
    }

    public String getTrama() {
        return trama;
    }

    public void setTrama(String trama) {
        this.trama = trama;
    }
    
}

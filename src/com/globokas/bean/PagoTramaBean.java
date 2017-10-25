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
public class PagoTramaBean {

    private String terminal;
    private String trace;
    private String fecha;
    private String tramatcc;

    public PagoTramaBean() {
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTramatcc() {
        return tramatcc;
    }

    public void setTramatcc(String tramatcc) {
        this.tramatcc = tramatcc;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globokas.enums;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pvasquez
 * TERMINALES A LOS QUE SE LE EXONERA LA COMISION
 */
public enum EnumTerminal {

    T0001("983001"),
    T0002("983002"),
    T0003("983003"),
    T0004("983004"),
    T0005("983007"),
    T0006("983008"),
    T0007("983009"),
    T0008("983010"),
    T0009("983011"),
    T0010("983012"),
    T0011("983013"),
    T0012("983014"),
    T0013("983015"),
    T0014("983016"),
    T0015("983017"),
    T0016("983018"),
    T0017("983019"),
    T0018("983020"),
    T0019("983021"),
    T0020("983022");

    public static final boolean PROCESA_TODOS = false;

    private String codigo;
    public static final Map<String, EnumTerminal> mapTerminal;

    static {
        mapTerminal = new HashMap<String, EnumTerminal>();

        for (EnumTerminal terminal : EnumTerminal.values()) {
            mapTerminal.put(terminal.getCodigo(), terminal);
        }
    }

    public static EnumTerminal obtenerPorCodigo(String codigo) {
        EnumTerminal enumTerminal = mapTerminal.get(codigo);
        return enumTerminal;
    }

    private EnumTerminal(String terminal) {
        setCodigo(terminal);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}

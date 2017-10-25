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
 */
public enum EnumTerminal {

    T0001("9830");
//    T0002("00840101"),
//    T0003("00500101");

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

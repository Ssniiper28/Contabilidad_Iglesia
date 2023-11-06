/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.rapture.controlgastos.models;

/**
 *
 * @author edgar
 */
public enum Categoria{
    EGRESOS,OFRENDAS_GENERALES,OFRENDAS_ESPECIALES,DIEZMOS,TALENTOS,PRIMICIAS;

    @Override
    public String toString() {
        switch (this) {
            case EGRESOS:
                return "Egresos";
            case OFRENDAS_GENERALES:
                return "Ofrendas Generales";
            case OFRENDAS_ESPECIALES:
                return "Ofrendas Especiales";
            case DIEZMOS:
                return "Diezmos";
            case TALENTOS:
                return "Talentos";
            case PRIMICIAS:
                return "Primicias";
            default:
                return null;
        }
    }
}

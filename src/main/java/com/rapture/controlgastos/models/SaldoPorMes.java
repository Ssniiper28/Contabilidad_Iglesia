/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.rapture.controlgastos.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author edgar
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaldoPorMes {
    private Integer mes;
    private Double ingresos;
    private Double egresos;
}

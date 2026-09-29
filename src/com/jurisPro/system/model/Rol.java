/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jurisPro.system.model;

/**
 *
 * @author informatica
 */


public enum Rol {
    ADMINISTRADOR("Administrador"),
    ABOGADO("Abogado"),
    CLIENTE("Cliente / Empresa");

    private final String nombre;

    Rol(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

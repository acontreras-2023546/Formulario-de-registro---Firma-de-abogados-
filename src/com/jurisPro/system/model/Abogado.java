package com.jurisPro.system.model;

import java.util.Objects;

public class Abogado {

    private String idAbogado;
    private String name;
    private String lastname;
    private String especiality;
    private String telephone;
    private String idSocio;
    private String username;
    private String password;
    private String idUsuario;

    // Constructor vacío
    public Abogado() {
    }

    // Constructor básico
    public Abogado(String idAbogado, String name, String lastname,
            String telephone, String password) {

        this.idAbogado = idAbogado;
        this.name = name;
        this.lastname = lastname;
        this.telephone = telephone;
        this.password = password;
    }

    // Constructor con Id_socio
    public Abogado(String idAbogado, String name, String lastname,
            String telephone, String password, String idSocio) {

        this.idAbogado = idAbogado;
        this.name = name;
        this.lastname = lastname;
        this.telephone = telephone;
        this.password = password;
        this.idSocio = idSocio;
    }

    // Constructor completo anterior
    public Abogado(String idAbogado, String name, String lastname,
            String telephone, String password, String idSocio,
            String idUsuario) {

        this.idAbogado = idAbogado;
        this.name = name;
        this.lastname = lastname;
        this.telephone = telephone;
        this.password = password;
        this.idSocio = idSocio;
        this.idUsuario = idUsuario;
    }

    // Constructor completo nuevo
    public Abogado(String idAbogado, String name, String lastname,
            String especiality, String telephone, String idSocio,
            String username, String password, String idUsuario) {

        this.idAbogado = idAbogado;
        this.name = name;
        this.lastname = lastname;
        this.especiality = especiality;
        this.telephone = telephone;
        this.idSocio = idSocio;
        this.username = username;
        this.password = password;
        this.idUsuario = idUsuario;
    }

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public String getIdAbogado() {
        return idAbogado;
    }

    public void setIdAbogado(String idAbogado) {
        this.idAbogado = idAbogado;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEspeciality() {
        return especiality;
    }

    public void setEspeciality(String especiality) {
        this.especiality = especiality;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(String idSocio) {
        this.idSocio = idSocio;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    // =========================
    // EQUALS Y HASHCODE
    // =========================

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Abogado abogado = (Abogado) o;

        return Objects.equals(idAbogado, abogado.idAbogado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAbogado);
    }

    // =========================
    // TOSTRING
    // =========================

    @Override
    public String toString() {
        String nombre = name != null ? name.trim() : "";
        String apellido = lastname != null ? lastname.trim() : "";

        return (nombre + " " + apellido).trim();
    }
}
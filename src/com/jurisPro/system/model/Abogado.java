package com.jurisPro.system.model;

import java.util.Objects;

public class Abogado {

    private String idAbogado;
    private String name;
    private String lastname;
    private String telephone;
    private String password;
    private String idSocio;


    // Constructores
    public Abogado() {
    }

    public Abogado(String idAbogado, String name, String lastname, String telephone,String username, String password) {
        this.idAbogado = idAbogado;
        this.name = name;
        this.lastname = lastname;
        this.telephone = telephone;
        this.username = username;
        this.password = password;
    }

    public Abogado(String idAbogado, String name, String lastname, String telephone,String username, String password, String idSocio) {
        this.idAbogado = idAbogado;
        this.name = name;
        this.lastname = lastname;
        this.telephone = telephone;
        this.username = username;
        this.password = password;
        this.idSocio = idSocio;
    }

    public Abogado(String idAbogado, String name, String lastname, String telephone, String password, String idSocio, String idUsuario) {
        this.idAbogado = idAbogado;
        this.name = name;
        this.lastname = lastname;
        this.telephone = telephone;
        this.password = password;
        this.idSocio = idSocio;
        this.idUsuario = idUsuario;
    }

    // Getters y Setters
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(String idSocio) {
        this.idSocio = idSocio;
    }

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

    @Override
    public String toString() {
        return getName() != null ? getName().trim() : "";
    }
}

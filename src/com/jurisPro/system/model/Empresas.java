package com.jurisPro.system.model;

public class Empresas {

    private String idEmpresa;
    private String nombre;
    private String telefono;
    private String direccion;
    private String password;
    private Abogado abogado;

    public Empresas() {
    }

    public Empresas(String idEmpresa, String nombre, String telefono, String direccion, String password, Abogado abogado) {
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.password = password;
        this.abogado = abogado;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getId() {
        return idEmpresa;
    }

    public void setId(String id) {
        this.idEmpresa = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Abogado getAbogado() {
        return abogado;
    }

    public void setAbogado(Abogado abogado) {
        this.abogado = abogado;
    }
}

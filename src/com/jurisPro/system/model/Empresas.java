package com.jurispro.system.model;

public class Empresas {
<<<<<<< HEAD

    private String idEmpresa;
    private String nombre;
    private String telefono;
    private String direccion;
    private String password;
=======
    private String idEmpresa;
    private String nombre;
>>>>>>> 2d1c65d (fix: arreglo base de datos)
    private Abogado abogado;

    public Empresas() {
    }

<<<<<<< HEAD
    public Empresas(String idEmpresa, String nombre, String telefono, String direccion, String password, Abogado abogado) {
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.password = password;
=======
    public Empresas(String idEmpresa, String nombre, Abogado abogado) {
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
>>>>>>> 2d1c65d (fix: arreglo base de datos)
        this.abogado = abogado;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

<<<<<<< HEAD
    public String getId() {
        return idEmpresa;
    }

    public void setId(String id) {
        this.idEmpresa = id;
    }

=======
>>>>>>> 2d1c65d (fix: arreglo base de datos)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

<<<<<<< HEAD
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

=======
>>>>>>> 2d1c65d (fix: arreglo base de datos)
    public Abogado getAbogado() {
        return abogado;
    }

    public void setAbogado(Abogado abogado) {
        this.abogado = abogado;
    }
<<<<<<< HEAD
}
=======

    @Override
    public String toString() {
        return nombre;
    }
}
>>>>>>> 2d1c65d (fix: arreglo base de datos)

package com.jurisPro.system.model;

public class Cliente {

    private String dpi;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private String nit;
    private Abogado abogado;


    public Cliente() {
    }


        this.dpi = dpi;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.direccion = direccion;

    }

    public String getDpi() { return dpi; }
    public void setDpi(String dpi) { this.dpi = dpi; }



    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Abogado getAbogado() { return abogado; }
    public void setAbogado(Abogado abogado) { this.abogado = abogado; }

    public String getIdCaso() { return idCaso; }
    public void setIdCaso(String idCaso) { this.idCaso = idCaso; }


}

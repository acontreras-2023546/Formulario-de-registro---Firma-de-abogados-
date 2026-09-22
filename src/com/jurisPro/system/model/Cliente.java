package com.jurisPro.system.model;

public class Cliente {

    private String dpi;
    private String nit;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private String password;
    private Abogado abogado;
    private String idCaso;
    private String idUsuario;

    public Cliente() {
    }

    public Cliente(String dpi, String nit, String nombre, String apellido,
                   String telefono, String direccion, String idCaso,
                   String idUsuario) {
        this.dpi = dpi;
        this.nit = nit;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.direccion = direccion;
        this.idCaso = idCaso;
        this.idUsuario = idUsuario;
    }

    public String getDpi() { return dpi; }
    public void setDpi(String dpi) { this.dpi = dpi; }

    public String getId() { return dpi; }
    public void setId(String id) { this.dpi = id; }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Abogado getAbogado() { return abogado; }
    public void setAbogado(Abogado abogado) { this.abogado = abogado; }

    public String getIdCaso() { return idCaso; }
    public void setIdCaso(String idCaso) { this.idCaso = idCaso; }

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
}

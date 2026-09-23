package com.jurisPro.system.model;

public class Cliente {

    private String dpi;
    private String nit;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;

    // Abogado encargado del cliente
    private Abogado abogado;

    // Se mantienen por compatibilidad con otras partes
    // del proyecto que todavía los puedan utilizar.
    private String password;
    private String username;
    private String idCaso;
    private String idUsuario;

    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public Cliente() {
    }

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Cliente(
            String dpi,
            String nit,
            String nombre,
            String apellido,
            String telefono,
            String direccion) {

        this.dpi = dpi;
        this.nit = nit;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getId() {
        return dpi;
    }

    public void setId(String id) {
        this.dpi = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Abogado getAbogado() {
        return abogado;
    }

    public void setAbogado(Abogado abogado) {
        this.abogado = abogado;
    }

    public String getIdCaso() {
        return idCaso;
    }

    public void setIdCaso(String idCaso) {
        this.idCaso = idCaso;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    // =========================================================
    // TOSTRING
    // =========================================================

    @Override
    public String toString() {

        String nombreCompleto =
                (nombre != null ? nombre : "")
                + " "
                + (apellido != null ? apellido : "");

        return nombreCompleto.trim();
    }
}
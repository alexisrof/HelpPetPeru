package com.example.helppetperu.Class;

public class Pet {
    String id;
    String nombre;
    String nacimiento;
    String detalle;
    String tipoMascota;
    String origenMascota;
    String estadoMascota;
    String imgUrl;
    String propietario;

    public Pet() {
    }

    public Pet(String id,String nombre, String nacimiento, String detalle, String tipoMascota, String origenMascota, String estadoMascota, String imgUrl, String propietario) {
        this.id = id;
        this.nombre = nombre;
        this.nacimiento = nacimiento;
        this.detalle = detalle;
        this.tipoMascota = tipoMascota;
        this.origenMascota = origenMascota;
        this.estadoMascota = estadoMascota;
        this.imgUrl = imgUrl;
        this.propietario = propietario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(String nacimiento) {
        this.nacimiento = nacimiento;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getTipoMascota() {
        return tipoMascota;
    }

    public void setTipoMascota(String tipoMascota) {
        this.tipoMascota = tipoMascota;
    }

    public String getOrigenMascota() {
        return origenMascota;
    }

    public void setOrigenMascota(String origenMascota) {
        this.origenMascota = origenMascota;
    }

    public String getEstadoMascota() {
        return estadoMascota;
    }

    public void setEstadoMascota(String estadoMascota) {
        this.estadoMascota = estadoMascota;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }
}

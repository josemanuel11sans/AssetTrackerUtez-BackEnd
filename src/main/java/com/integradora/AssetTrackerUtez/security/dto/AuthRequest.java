package com.integradora.AssetTrackerUtez.security.dto;

public class AuthRequest {
    private String correo;
    private String contrasena;

    public AuthRequest() {
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }


    public String getPassword() {
        return contrasena;
    }

    public void setPassword(String contrasena) {
        this.contrasena = contrasena;
    }
}

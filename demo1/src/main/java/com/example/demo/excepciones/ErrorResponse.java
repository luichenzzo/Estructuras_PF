package com.example.demo.excepciones;

import java.time.LocalDateTime;

/**
 * Clase para encapsular la respuesta de error en formato JSON.
 * Proporciona información detallada sobre errores ocurridos en la aplicación.
 */
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String mensaje;
    private String path;

    /**
     * Constructor por defecto que inicializa el timestamp con la hora actual.
     */
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor completo para crear una respuesta de error.
     *
     * @param status  Código de estado HTTP
     * @param error   Tipo de error
     * @param mensaje Mensaje descriptivo del error
     * @param path    Ruta donde ocurrió el error
     */
    public ErrorResponse(int status, String error, String mensaje, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.mensaje = mensaje;
        this.path = path;
    }

    // Getters y Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

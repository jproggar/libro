package com.example.api.service;

public interface IConvierteDatos {
    <T> T convertirDatosJsonAJava(String json , Class<T> clase);
}

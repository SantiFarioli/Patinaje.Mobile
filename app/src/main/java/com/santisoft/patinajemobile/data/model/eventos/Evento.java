package com.santisoft.patinajemobile.data.model.eventos;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Evento implements Serializable {

    // 👇 ¡AGREGÁ ESTO! Es la clave para que funcione el click
    @SerializedName("id")
    public int id;

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("fechaInicio")
    public String fechaInicio; // ISO string (ej: "2025-10-05T09:00:00")

    @SerializedName("lugar")
    public String lugar; // puede venir null

    @SerializedName("fechaLimiteInscripcion")
    public String fechaLimiteInscripcion; // puede venir null

    @SerializedName("latitud")
    public Double latitud;

    @SerializedName("longitud")
    public Double longitud;

    @SerializedName("descripcion")
    public String descripcion;

    // Campo local para UI (no viene del backend, pero se usa para visualizar)
    public String direccion;
}
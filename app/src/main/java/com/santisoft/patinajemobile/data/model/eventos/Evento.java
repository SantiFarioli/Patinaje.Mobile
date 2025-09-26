package com.santisoft.patinajemobile.data.model.eventos;

import com.google.gson.annotations.SerializedName;

public class Evento {
    @SerializedName("nombre")
    public String nombre;

    @SerializedName("fechaInicio")
    public String fechaInicio;   // ISO string (ej: "2025-10-05T09:00:00")

    @SerializedName("lugar")
    public String lugar;         // puede venir null

    @SerializedName("fechaLimiteInscripcion")
    public String fechaLimiteInscripcion; // puede venir null
}

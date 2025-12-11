package com.santisoft.patinajemobile.data.model.torneos;

import com.google.gson.annotations.SerializedName;

public class Torneo {
    @SerializedName("torneoId") public int torneoId;
    @SerializedName("nombre") public String nombre;
    @SerializedName("lugar") public String lugar;             // Nuevo
    @SerializedName("fechaInicio") public String fechaInicio; // Nuevo
    @SerializedName("organizador") public String organizador; // Nuevo

    // IMPORTANTE: Mantener este toString() para que el Spinner de Evaluaciones siga funcionando bien
    @Override
    public String toString() {
        return nombre;
    }
}
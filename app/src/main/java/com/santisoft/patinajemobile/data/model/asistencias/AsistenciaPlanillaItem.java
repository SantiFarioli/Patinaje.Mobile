package com.santisoft.patinajemobile.data.model.asistencias;

import com.google.gson.annotations.SerializedName;

public class AsistenciaPlanillaItem {
    @SerializedName("patinadorId") public int patinadorId;
    @SerializedName("nombre") public String nombre;
    @SerializedName("apellido") public String apellido;
    @SerializedName("categoria") public String categoria;
    @SerializedName("presente") public boolean presente;

    // Constructor vacío para Gson
    public AsistenciaPlanillaItem() {}
}
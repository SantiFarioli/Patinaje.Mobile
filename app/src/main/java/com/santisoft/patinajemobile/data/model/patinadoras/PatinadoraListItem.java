package com.santisoft.patinajemobile.data.model.patinadoras;

import com.google.gson.annotations.SerializedName;

public class PatinadoraListItem {
    @SerializedName("patinadorId") public int patinadorId;
    @SerializedName("nombre") public String nombre;
    @SerializedName("apellido") public String apellido;
    @SerializedName("categoria") public String categoria;
    @SerializedName("activo") public boolean activo;
    @SerializedName("fotoUrl") public String fotoUrl;

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}

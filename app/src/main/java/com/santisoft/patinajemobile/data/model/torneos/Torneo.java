package com.santisoft.patinajemobile.data.model.torneos;

import com.google.gson.annotations.SerializedName;

public class Torneo {
    @SerializedName("torneoId")
    public int torneoId;

    @SerializedName("nombre")
    public String nombre;

    @Override
    public String toString() {
        return nombre;
    }
}
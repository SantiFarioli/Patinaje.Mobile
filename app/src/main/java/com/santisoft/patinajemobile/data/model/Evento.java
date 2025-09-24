package com.santisoft.patinajemobile.data.model;

import com.google.gson.annotations.SerializedName;

public class Evento {
    @SerializedName("titulo") public String titulo;
    @SerializedName("fecha")  public String fecha;   // formateada por el backend
    @SerializedName("lugar")  public String lugar;   // puede venir null
    @SerializedName("inscripcionHasta") public String inscripcionHasta; // puede venir null
}

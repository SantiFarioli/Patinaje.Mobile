package com.santisoft.patinajemobile.data.model.asistencias;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RegistrarAsistenciaRequest {
    @SerializedName("fecha") public String fecha; // Formato ISO "2025-10-23T00:00:00"
    @SerializedName("detalles") public List<Detalle> detalles;

    public RegistrarAsistenciaRequest(String fecha, List<Detalle> detalles) {
        this.fecha = fecha;
        this.detalles = detalles;
    }

    public static class Detalle {
        @SerializedName("patinadorId") public int patinadorId;
        @SerializedName("presente") public boolean presente;

        public Detalle(int patinadorId, boolean presente) {
            this.patinadorId = patinadorId;
            this.presente = presente;
        }
    }
}
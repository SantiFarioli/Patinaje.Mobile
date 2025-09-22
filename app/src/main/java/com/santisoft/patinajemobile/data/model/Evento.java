package com.santisoft.patinajemobile.data.model;

public class Evento {
    public String titulo;
    public String fecha;      // ej. "Mar 24, 18:00"
    public String lugar;      // opcional
    public Evento(String t, String f, String l){ titulo=t; fecha=f; lugar=l; }
}

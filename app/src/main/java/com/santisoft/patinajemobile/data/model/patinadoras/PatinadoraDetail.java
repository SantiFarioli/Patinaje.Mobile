package com.santisoft.patinajemobile.data.model.patinadoras;

import com.google.gson.annotations.SerializedName;

public class PatinadoraDetail {
    @SerializedName("patinadorId") public int patinadorId;
    @SerializedName("nombre") public String nombre;
    @SerializedName("apellido") public String apellido;
    @SerializedName("fechaNacimiento") public String fechaNacimiento;
    @SerializedName("categoria") public String categoria;
    @SerializedName("activo") public boolean activo;
    @SerializedName("fichaMedica") public String fichaMedica;
    @SerializedName("asisteGimnasio") public boolean asisteGimnasio;
    @SerializedName("asisteNutricionista") public boolean asisteNutricionista;
    @SerializedName("asistePsicologo") public boolean asistePsicologo;
    @SerializedName("profesorId") public int profesorId;
    @SerializedName("profesorNombre") public String profesorNombre;
    @SerializedName("clubId") public Integer clubId;
    @SerializedName("clubNombre") public String clubNombre;
}

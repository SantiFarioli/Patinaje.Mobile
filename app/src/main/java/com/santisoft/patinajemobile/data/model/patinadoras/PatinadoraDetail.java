package com.santisoft.patinajemobile.data.model.patinadoras;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PatinadoraDetail {
    @SerializedName("patinadorId") public int patinadorId;
    @SerializedName("nombre") public String nombre;
    @SerializedName("apellido") public String apellido;
    @SerializedName("fechaNacimiento") public String fechaNacimiento;
    @SerializedName("categoria") public String categoria;
    @SerializedName("activo") public boolean activo;

    @SerializedName("dni") public String dni;
    @SerializedName("domicilio") public String domicilio;
    @SerializedName("fotoUrl") public String fotoUrl;

    @SerializedName("fichaMedica") public String fichaMedica;
    @SerializedName("asisteGimnasio") public boolean asisteGimnasio;
    @SerializedName("asisteNutricionista") public boolean asisteNutricionista;
    @SerializedName("asistePsicologo") public boolean asistePsicologo;
    @SerializedName("profesorId") public int profesorId;
    @SerializedName("profesorNombre") public String profesorNombre;
    @SerializedName("clubId") public Integer clubId;
    @SerializedName("clubNombre") public String clubNombre;

    @SerializedName("tutores")
    public List<TutorDto> tutores;

    public static class TutorDto {
        @SerializedName("tutorId") public int tutorId;
        @SerializedName("nombre") public String nombre;
        @SerializedName("apellido") public String apellido;
        @SerializedName("dni") public String dni;
        @SerializedName("domicilio") public String domicilio;
        @SerializedName("telefono") public String telefono;
        @SerializedName("email") public String email;
        @SerializedName("relacion") public String relacion;
    }

    @SerializedName("asistencias")
    public List<AsistenciaDto> asistencias;

    public static class AsistenciaDto {
        @SerializedName("asistenciaId") public int asistenciaId;
        @SerializedName("fechaClase") public String fechaClase;  // viene como "yyyy-MM-dd"
        @SerializedName("presente") public boolean presente;
    }

    @SerializedName("pagos")
    public List<PagoDto> pagos;

    public static class PagoDto {
        @SerializedName("pagoId") public int pagoId;
        @SerializedName("concepto") public String concepto;
        @SerializedName("monto") public double monto;
        @SerializedName("estado") public String estado;
        @SerializedName("fechaVencimiento") public String fechaVencimiento; // yyyy-MM-dd
        @SerializedName("fechaPago") public String fechaPago; // puede ser null
        @SerializedName("tipo") public String tipo; // opcional (Cuota, Torneo, etc.)
        @SerializedName("comprobanteUrl") public String comprobanteUrl; // opcional
    }



    @SerializedName("evaluaciones")
    public List<EvaluacionDto> evaluaciones;

    public static class EvaluacionDto {
        @SerializedName("evaluacionId") public int evaluacionId; // O evaluacionTorneoId
        @SerializedName("nombreTorneo") public String nombreTorneo; // Necesitamos esto del backend
        @SerializedName("fecha") public String fecha;
        @SerializedName("archivoPdfUrl") public String archivoPdfUrl; // 👈 CRÍTICO
        @SerializedName("observaciones") public String observaciones;
    }




}

package com.santisoft.patinajemobile.ui.common;

import com.santisoft.patinajemobile.R;

public class ImageMapper {

    public static int getTorneoImage(String lugar) {
        if (lugar == null)
            return R.drawable.poli_ejemplo;

        // Normalizamos para búsqueda flexible
        String normalized = lugar.toLowerCase().trim();

        if (normalized.contains("polideportivo")) {
            return R.drawable.polideportivo; // Foto estadio grande
        } else if (normalized.contains("club unión") || normalized.contains("union")) {
            return R.drawable.poli_club_union; // Foto techado/parquet
        } else if (normalized.contains("san luis") || normalized.contains("ave fénix")
                || normalized.contains("fenix")) {
            return R.drawable.poli_san_luis; // Foto aire libre/velódromo
        } else if (normalized.contains("norte")) {
            return R.drawable.poli_club_norte;
        } else if (normalized.contains("pista central")) {
            return R.drawable.poli_pista_central;
        } else if (normalized.contains("villa mercedes")) {
            return R.drawable.poli_villa_mercedes;
        } else {
            return R.drawable.poli_ejemplo; // Fallback
        }
    }

    public static void asignarDireccion(com.santisoft.patinajemobile.data.model.eventos.Evento e) {
        if (e == null || e.lugar == null)
            return;

        String lugar = e.lugar; // Case sensitive check or normalized if preferred, keeping original logic
                                // structure but ensuring all cases

        if (lugar.contains("Club Unión")) {
            e.direccion = "9 de Julio 3749, Mar del Plata";
        } else if (lugar.contains("Islas Malvinas") || lugar.contains("Mar del Plata")
                || lugar.contains("Polideportivo")) {
            e.direccion = "Av. Juan B. Justo 3525, Mar del Plata";
        } else if (lugar.contains("San Luis") || lugar.contains("Pista Central")) {
            e.direccion = "Av. del Fundador s/n, San Luis";
        } else if (lugar.contains("Ave Fénix")) {
            e.direccion = "Av. del Viento Chorrillero Km 7.5, Juana Koslay";
        } else if (lugar.contains("Villa Mercedes") || lugar.contains("Palacio")) {
            e.direccion = "General Paz e Intendente Alric, Villa Mercedes";
        } else if (lugar.contains("Club Norte") || lugar.contains("Rosario")) {
            e.direccion = "Av. Pellegrini 4200, Rosario";
        } else {
            e.direccion = "Dirección a confirmar";
        }
    }
}

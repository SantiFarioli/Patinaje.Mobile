package com.santisoft.patinajemobile.data.model.pagos;

import com.google.gson.annotations.SerializedName;

public class PagoPendiente {
    @SerializedName("pagoId") public int pagoId;
    @SerializedName("patinadoraNombre") public String patinadoraNombre;
    @SerializedName("concepto") public String concepto;
    @SerializedName("monto") public double monto;
    @SerializedName("fechaVencimiento") public String fechaVencimiento;
}

package com.santisoft.patinajemobile.data.model.dashboard;

import com.google.gson.annotations.SerializedName;

public class DashboardSummary {
    @SerializedName("totalPatinadoras")
    public int totalPatinadoras;

    @SerializedName("totalEventosProximos")
    public int totalEventosProximos;

    @SerializedName("totalPagosPendientes")
    public int totalPagosPendientes;
}

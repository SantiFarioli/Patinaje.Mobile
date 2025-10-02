package com.santisoft.patinajemobile.ui.patinadoras;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AsistenciasAdapter extends RecyclerView.Adapter<AsistenciasAdapter.VH> {

    private final List<PatinadoraDetail.AsistenciaDto> data;

    public AsistenciasAdapter(List<PatinadoraDetail.AsistenciaDto> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_asistencia, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        PatinadoraDetail.AsistenciaDto a = data.get(position);

        // Parsear fecha yyyy-MM-dd → dd/MM/yyyy
        String fechaFormateada = a.fechaClase;
        try {
            SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            fechaFormateada = sdfOut.format(sdfIn.parse(a.fechaClase));
        } catch (ParseException ignored) {}

        holder.tvFecha.setText(fechaFormateada);

        // Estado con colores y fondo
        if (a.presente) {
            holder.tvEstado.setText("✅ Presente");
            holder.tvEstado.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.success));
            holder.card.setCardBackgroundColor(Color.parseColor("#E8F5E9")); // verde suave
        } else {
            holder.tvEstado.setText("❌ Ausente");
            holder.tvEstado.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.error));
            holder.card.setCardBackgroundColor(Color.parseColor("#FFEBEE")); // rojo suave
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        MaterialCardView card;
        TextView tvFecha, tvEstado;

        VH(@NonNull View itemView) {
            super(itemView);
            card = (MaterialCardView) itemView;
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}

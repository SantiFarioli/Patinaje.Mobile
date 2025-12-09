package com.santisoft.patinajemobile.ui.patinadoras;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraDetail;

import java.util.List;

public class EvaluacionesAdapter extends RecyclerView.Adapter<EvaluacionesAdapter.VH> {

    private final List<PatinadoraDetail.EvaluacionDto> data;

    public EvaluacionesAdapter(List<PatinadoraDetail.EvaluacionDto> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evaluacion, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        PatinadoraDetail.EvaluacionDto item = data.get(position);

        holder.tvTorneo.setText(item.nombreTorneo != null ? item.nombreTorneo : "Evaluación Técnica");
        holder.tvFecha.setText(item.fecha != null ? item.fecha.substring(0, 10) : "");
        holder.tvObservaciones.setText(item.observaciones != null ? item.observaciones : "Sin observaciones");

        if (item.archivoPdfUrl != null && !item.archivoPdfUrl.isEmpty()) {
            holder.btnVerPdf.setVisibility(View.VISIBLE);
            holder.btnVerPdf.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(item.archivoPdfUrl), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    v.getContext().startActivity(intent);
                } catch (Exception e) {
                    // Si no tiene visor de PDF, abrimos en navegador
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.archivoPdfUrl));
                    v.getContext().startActivity(browserIntent);
                }
            });
        } else {
            holder.btnVerPdf.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return data != null ? data.size() : 0; }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTorneo, tvFecha, tvObservaciones;
        Button btnVerPdf;

        VH(@NonNull View itemView) {
            super(itemView);
            tvTorneo = itemView.findViewById(R.id.tvTorneo);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvObservaciones = itemView.findViewById(R.id.tvObservaciones);
            btnVerPdf = itemView.findViewById(R.id.btnVerPdf);
        }
    }
}
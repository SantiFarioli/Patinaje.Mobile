package com.santisoft.patinajemobile.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.santisoft.patinajemobile.data.model.Evento;
import com.santisoft.patinajemobile.databinding.ItemEventoBinding;

import java.util.ArrayList;
import java.util.List;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.VH> {
    private final List<Evento> data = new ArrayList<>();

    public void submit(List<Evento> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEventoBinding vb = ItemEventoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new VH(vb);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Evento e = data.get(pos);

        // Título
        h.vb.tvTitulo.setText(e.nombre);

        // Fecha inicio (formateada por backend o en ISO string)
        h.vb.tvFecha.setText(e.fechaInicio != null ? e.fechaInicio : "");

        // Lugar
        h.vb.tvLugar.setText(e.lugar != null ? e.lugar : "");

        // Fecha límite de inscripción (opcional)
        if (e.fechaLimiteInscripcion != null && !e.fechaLimiteInscripcion.isEmpty()) {
            h.vb.tvDeadline.setText("Inscripción hasta: " + e.fechaLimiteInscripcion);
            h.vb.tvDeadline.setVisibility(View.VISIBLE);
        } else {
            h.vb.tvDeadline.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemEventoBinding vb;
        VH(ItemEventoBinding vb) {
            super(vb.getRoot());
            this.vb = vb;
        }
    }
}

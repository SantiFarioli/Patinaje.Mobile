package com.santisoft.patinajemobile.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.santisoft.patinajemobile.data.model.eventos.Evento;
import com.santisoft.patinajemobile.databinding.ItemEventoBinding;

import java.util.ArrayList;
import java.util.List;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.VH> {
    private final List<Evento> data = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Evento item);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void submit(List<Evento> list) {
        data.clear();
        if (list != null)
            data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEventoBinding vb = ItemEventoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(vb);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Evento e = data.get(pos);

        // Título
        h.vb.tvTitulo.setText(e.nombre);

        // Fecha inicio
        h.vb.tvFecha.setText(e.fechaInicio != null ? e.fechaInicio : "");

        // Lugar
        h.vb.tvLugar.setText(e.lugar != null ? e.lugar : "");

        // Cargar imagen aleatoria (determinista por nombre) con Glide
        String seed = e.nombre != null ? e.nombre.replaceAll("\\s+", "") : "default";
        String imageUrl = "https://picsum.photos/seed/" + seed + "/500/300";

        com.bumptech.glide.Glide.with(h.itemView.getContext())
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery) // Placeholder básico
                .error(android.R.drawable.ic_delete) // Icono error básico
                .into(h.vb.ivLugar);

        // Fecha límite
        if (e.fechaLimiteInscripcion != null && !e.fechaLimiteInscripcion.isEmpty()) {
            h.vb.tvDeadline.setText("Inscripción hasta: " + e.fechaLimiteInscripcion);
            h.vb.tvDeadline.setVisibility(View.VISIBLE);
        } else {
            h.vb.tvDeadline.setVisibility(View.GONE);
        }

        // Click Listener
        h.vb.btnVerDetalle.setOnClickListener(v -> {
            if (listener != null)
                listener.onItemClick(e);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemEventoBinding vb;

        VH(ItemEventoBinding vb) {
            super(vb.getRoot());
            this.vb = vb;
        }
    }
}

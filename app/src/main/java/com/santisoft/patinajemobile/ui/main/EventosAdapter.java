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

        // Subtítulo (Fecha + Lugar) -> Compacto
        String fecha = e.fechaInicio != null ? e.fechaInicio.substring(0, 10) : ""; // Simplificar fecha
        String lugar = e.lugar != null ? e.lugar : "Sin lugar";
        h.vb.tvSubtitulo.setText("📅 " + fecha + " • 📍 " + lugar);

        // Cargar imagen aleatoria (determinista por nombre) con Glide
        String seed = e.nombre != null ? e.nombre.replaceAll("\\s+", "") : "default";
        String imageUrl = "https://picsum.photos/seed/" + seed + "/300/300"; // Más chica (300x300)

        com.bumptech.glide.Glide.with(h.itemView.getContext())
                .load(imageUrl)
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(h.vb.ivLugar);

        // Aviso Delegada (Placeholder) - Lógica futura
        // h.vb.layDelegadaNotice.setVisibility(View.GONE);

        // Fecha límite - Alerta
        // TODO: Comprobar fecha real vs hoy
        h.vb.tvDeadline.setVisibility(View.GONE);

        // Click en toda la card
        h.itemView.setOnClickListener(v -> {
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

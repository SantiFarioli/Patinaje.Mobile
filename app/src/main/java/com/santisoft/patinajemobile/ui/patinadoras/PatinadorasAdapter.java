package com.santisoft.patinajemobile.ui.patinadoras;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraListItem;
import com.santisoft.patinajemobile.databinding.ItemPatinadoraBinding;

import java.util.ArrayList;
import java.util.List;

public class PatinadorasAdapter extends RecyclerView.Adapter<PatinadorasAdapter.VH> {
    private final List<PatinadoraListItem> data = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(PatinadoraListItem patinadora);
    }

    public PatinadorasAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void submit(List<PatinadoraListItem> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPatinadoraBinding vb = ItemPatinadoraBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new VH(vb);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        PatinadoraListItem p = data.get(position);

        holder.vb.tvNombre.setText(p.nombre + " " + p.apellido);
        holder.vb.tvCategoria.setText("Categoría: " + p.categoria);
        holder.vb.tvEstado.setText(p.activo ? "Activa" : "Inactiva");

        // Cargar foto con Glide si existe
        if (p.fotoUrl != null && !p.fotoUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(p.fotoUrl)
                    .placeholder(R.drawable.ic_person)
                    .circleCrop()
                    .into(holder.vb.imgFotoMini);
        } else {
            holder.vb.imgFotoMini.setImageResource(R.drawable.ic_person);
        }

        holder.itemView.setOnClickListener(v -> listener.onClick(p));
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemPatinadoraBinding vb;
        VH(ItemPatinadoraBinding vb) {
            super(vb.getRoot());
            this.vb = vb;
        }
    }

}

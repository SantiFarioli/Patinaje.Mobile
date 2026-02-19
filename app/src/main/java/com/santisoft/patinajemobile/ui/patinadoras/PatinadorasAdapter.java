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

        // 👇 Buscamos la foto local basándonos en el NOMBRE de la patinadora
        String nombreFoto = "";
        if (p.nombre != null) {
            // Pasamos a minúsculas, sacamos acentos (ej: Sofía -> sofia) y espacios
            String nombreSeguro = p.nombre.toLowerCase()
                    .replace("í", "i")
                    .replace(" ", "_")
                    .trim();
            nombreFoto = "foto_" + nombreSeguro;
        }

        // Buscamos el ID dinámicamente en la carpeta drawable
        int resId = holder.itemView.getContext().getResources().getIdentifier(
                nombreFoto,
                "drawable",
                holder.itemView.getContext().getPackageName()
        );

        if (resId != 0) {
            // Encontró la foto local (ej: foto_camila.jpg)
            Glide.with(holder.itemView.getContext())
                    .load(resId)
                    .placeholder(R.drawable.ic_person)
                    .circleCrop()
                    .into(holder.vb.imgFotoMini);
        } else {
            // No encontró el archivo, ponemos default
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.ic_person)
                    .circleCrop()
                    .into(holder.vb.imgFotoMini);
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
package com.santisoft.patinajemobile.ui.torneos;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraListItem;
import com.santisoft.patinajemobile.databinding.ItemSeleccionAtletaBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeleccionAtletasAdapter extends RecyclerView.Adapter<SeleccionAtletasAdapter.VH> {

    private final List<PatinadoraListItem> data = new ArrayList<>();
    private final Set<Integer> selectedIds = new HashSet<>();

    public void submit(List<PatinadoraListItem> list) {
        data.clear();
        if (list != null)
            data.addAll(list);
        notifyDataSetChanged();
    }

    public List<PatinadoraListItem> getSelectedItems() {
        List<PatinadoraListItem> selected = new ArrayList<>();
        for (PatinadoraListItem p : data) {
            if (selectedIds.contains(p.patinadorId)) {
                selected.add(p);
            }
        }
        return selected;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSeleccionAtletaBinding vb = ItemSeleccionAtletaBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(vb);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        PatinadoraListItem p = data.get(pos);

        h.vb.tvNombre.setText(p.nombre + " " + p.apellido);
        h.vb.tvCategoria.setText("Categoría: " + p.categoria);

        // Checkbox state
        h.vb.cbSeleccion.setOnCheckedChangeListener(null); // Evitar disparos al reciclar
        h.vb.cbSeleccion.setChecked(selectedIds.contains(p.patinadorId));

        h.vb.cbSeleccion.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                selectedIds.add(p.patinadorId);
            else
                selectedIds.remove(p.patinadorId);
        });

        // Click en toda la row togglea el checkbox
        h.itemView.setOnClickListener(v -> h.vb.cbSeleccion.toggle());

        // Foto
        if (p.fotoUrl != null && !p.fotoUrl.isEmpty()) {
            Glide.with(h.itemView.getContext())
                    .load(p.fotoUrl)
                    .placeholder(R.drawable.ic_person)
                    .circleCrop()
                    .into(h.vb.imgFotoMini);
        } else {
            h.vb.imgFotoMini.setImageResource(R.drawable.ic_person);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemSeleccionAtletaBinding vb;

        VH(ItemSeleccionAtletaBinding vb) {
            super(vb.getRoot());
            this.vb = vb;
        }
    }
}

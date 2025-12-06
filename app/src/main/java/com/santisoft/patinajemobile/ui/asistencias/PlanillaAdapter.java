package com.santisoft.patinajemobile.ui.asistencias;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.santisoft.patinajemobile.data.model.asistencias.AsistenciaPlanillaItem;
import com.santisoft.patinajemobile.databinding.ItemAsistenciaPlanillaBinding;
import java.util.ArrayList;
import java.util.List;

public class PlanillaAdapter extends RecyclerView.Adapter<PlanillaAdapter.VH> {

    private final List<AsistenciaPlanillaItem> data = new ArrayList<>();

    public void submit(List<AsistenciaPlanillaItem> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    public List<AsistenciaPlanillaItem> getData() {
        return data;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAsistenciaPlanillaBinding b = ItemAsistenciaPlanillaBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new VH(b);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        AsistenciaPlanillaItem item = data.get(position);

        holder.b.tvNombre.setText(item.nombre + " " + item.apellido);
        holder.b.tvCategoria.setText(item.categoria);

        // Evitar que el listener se dispare al reciclar la vista
        holder.b.cbPresente.setOnCheckedChangeListener(null);
        holder.b.cbPresente.setChecked(item.presente);

        // Guardar el cambio en el objeto
        holder.b.cbPresente.setOnCheckedChangeListener((v, isChecked) -> {
            item.presente = isChecked;
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemAsistenciaPlanillaBinding b;
        VH(ItemAsistenciaPlanillaBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
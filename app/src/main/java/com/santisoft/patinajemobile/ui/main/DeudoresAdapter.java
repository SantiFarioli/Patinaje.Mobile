package com.santisoft.patinajemobile.ui.main;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.data.model.pagos.PagoPendiente;
import com.santisoft.patinajemobile.databinding.ItemPagoBinding; // 👈 MAGIA: Se genera solo desde item_pago.xml

import java.util.ArrayList;
import java.util.List;

public class DeudoresAdapter extends RecyclerView.Adapter<DeudoresAdapter.VH> {

    private final List<PagoPendiente> data = new ArrayList<>();

    public void submit(List<PagoPendiente> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Usamos Binding en lugar de inflate directo
        ItemPagoBinding binding = ItemPagoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        PagoPendiente p = data.get(pos);
        Context context = h.itemView.getContext();

        // Accedemos directo a las vistas por su ID (sin findViewById)
        h.binding.tvConcepto.setText("👤 " + p.patinadoraNombre);

        String fecha = (p.fechaVencimiento != null && p.fechaVencimiento.length() >= 10)
                ? p.fechaVencimiento.substring(0, 10)
                : "";
        h.binding.tvVencimiento.setText(p.concepto + " • Vence: " + fecha);

        h.binding.tvMonto.setText("$ " + (int) p.monto);

        h.binding.chipEstadoPago.setText("Pendiente");

        // Color correcto con ColorStateList
        int colorReal = ContextCompat.getColor(context, R.color.warning);
        h.binding.chipEstadoPago.setChipBackgroundColor(ColorStateList.valueOf(colorReal));
        h.binding.chipEstadoPago.setTextColor(ContextCompat.getColor(context, android.R.color.white));
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        // Guardamos el Binding, no los TextViews sueltos
        final ItemPagoBinding binding;

        VH(ItemPagoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
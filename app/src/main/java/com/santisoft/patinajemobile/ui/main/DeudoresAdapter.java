package com.santisoft.patinajemobile.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.data.model.pagos.PagoPendiente;
import com.santisoft.patinajemobile.databinding.ItemPagoBinding;

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
        ItemPagoBinding binding = ItemPagoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        PagoPendiente p = data.get(pos);
        Context context = h.itemView.getContext();

        // 1. Nombre de la patinadora
        h.binding.tvConcepto.setText("👤 " + p.patinadoraNombre);

        // 2. Formateo de fecha de vencimiento
        String fecha = (p.fechaVencimiento != null && p.fechaVencimiento.length() >= 10)
                ? p.fechaVencimiento.substring(0, 10)
                : "";
        h.binding.tvVencimiento.setText(p.concepto + " • Vence: " + fecha);

        // 3. Monto
        h.binding.tvMonto.setText("$ " + (int) p.monto);

        // 4. ESTADO (Corregido: usamos tvEstado en lugar de chipEstadoPago)
        h.binding.tvEstado.setText("Pendiente");

        // Seteamos el color de texto para que resalte (usando el color warning que ya tenés)
        h.binding.tvEstado.setTextColor(ContextCompat.getColor(context, R.color.warning));
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemPagoBinding binding;

        VH(ItemPagoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
package com.santisoft.patinajemobile.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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
        // Usamos el layout item_pago.xml (estilo Timeline)
        ItemPagoBinding binding = ItemPagoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        PagoPendiente p = data.get(pos);
        Context context = h.itemView.getContext();

        // 1. Icono: Cruz Roja (Deuda)
        h.binding.ivStatusIcon.setImageResource(R.drawable.ic_close);
        h.binding.ivStatusIcon.setColorFilter(ContextCompat.getColor(context, R.color.md_error));

        // 2. Línea conectora: Oculta (porque son personas distintas)
        h.binding.viewConnectorLine.setVisibility(View.GONE);

        // 3. Textos
        // Nombre -> Título
        h.binding.tvConcepto.setText(p.patinadoraNombre);

        // Vencimiento -> Detalle
        String fecha = (p.fechaVencimiento != null && p.fechaVencimiento.length() >= 10)
                ? p.fechaVencimiento.substring(0, 10)
                : "";
        h.binding.tvDetalle.setText(p.concepto + " • Vence: " + fecha);
        h.binding.tvDetalle.setTextColor(ContextCompat.getColor(context, R.color.gray));

        // 4. Monto (Rojo)
        h.binding.tvMonto.setText("$ " + (int) p.monto);
        h.binding.tvMonto.setTextColor(ContextCompat.getColor(context, R.color.md_error));
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
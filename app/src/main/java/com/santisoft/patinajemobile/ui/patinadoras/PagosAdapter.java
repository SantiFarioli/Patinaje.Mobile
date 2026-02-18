package com.santisoft.patinajemobile.ui.patinadoras;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraDetail;

import java.util.List;

public class PagosAdapter extends RecyclerView.Adapter<PagosAdapter.VH> {

    private final List<PatinadoraDetail.PagoDto> data;

    public PagosAdapter(List<PatinadoraDetail.PagoDto> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Asegurate de que el XML se llame item_pago.xml
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pago, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        PatinadoraDetail.PagoDto p = data.get(position);
        Context ctx = holder.itemView.getContext();

        // 1. Concepto (Mes)
        holder.tvConcepto.setText(p.concepto);

        // 2. Monto
        holder.tvMonto.setText(String.format("$%,.0f", p.monto));

        // 3. Lógica de Estado (Timeline)
        boolean isPagado = "pagado".equalsIgnoreCase(p.estado);

        if (isPagado) {
            // -- PAGADO (Verde) --
            // Usá ic_check_circle o ic_check según tus drawables
            holder.ivStatusIcon.setImageResource(R.drawable.ic_check_circle);
            holder.ivStatusIcon.setColorFilter(ContextCompat.getColor(ctx, R.color.success));

            holder.tvMonto.setTextColor(ContextCompat.getColor(ctx, R.color.success));

            String fechaPago = (p.fechaPago != null && p.fechaPago.length() >= 10)
                    ? p.fechaPago.substring(0, 10)
                    : "";
            holder.tvDetalle.setText("Pagado el " + fechaPago);
            holder.tvDetalle.setTextColor(ContextCompat.getColor(ctx, R.color.gray)); // Gris suave
        } else {
            // -- PENDIENTE (Rojo) --
            // Usá ic_close, ic_error o similar
            holder.ivStatusIcon.setImageResource(R.drawable.ic_close);
            holder.ivStatusIcon.setColorFilter(ContextCompat.getColor(ctx, R.color.md_error));

            holder.tvMonto.setTextColor(ContextCompat.getColor(ctx, R.color.md_error));

            String fechaVenc = (p.fechaVencimiento != null && p.fechaVencimiento.length() >= 10)
                    ? p.fechaVencimiento.substring(0, 10)
                    : "";
            holder.tvDetalle.setText("Vence: " + fechaVenc);
            holder.tvDetalle.setTextColor(ContextCompat.getColor(ctx, R.color.md_error));
        }

        // 4. Línea Conectora (Ocultar en el último elemento)
        if (position == data.size() - 1) {
            holder.viewConnectorLine.setVisibility(View.INVISIBLE);
        } else {
            holder.viewConnectorLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        // IDs del nuevo layout item_pago.xml
        ImageView ivStatusIcon;
        View viewConnectorLine;
        TextView tvConcepto, tvDetalle, tvMonto;

        VH(@NonNull View itemView) {
            super(itemView);
            ivStatusIcon = itemView.findViewById(R.id.ivStatusIcon);
            viewConnectorLine = itemView.findViewById(R.id.viewConnectorLine);
            tvConcepto = itemView.findViewById(R.id.tvConcepto);
            tvDetalle = itemView.findViewById(R.id.tvDetalle);
            tvMonto = itemView.findViewById(R.id.tvMonto);
        }
    }
}
package com.santisoft.patinajemobile.ui.patinadoras;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraDetail;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PagosAdapter extends RecyclerView.Adapter<PagosAdapter.VH> {

    private final List<PatinadoraDetail.PagoDto> data;
    private final SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    public PagosAdapter(List<PatinadoraDetail.PagoDto> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pago, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        PatinadoraDetail.PagoDto p = data.get(position);

        holder.tvConcepto.setText("🧾 " + p.concepto);
        holder.tvMonto.setText(String.format("💵 $%,.0f", p.monto));
        holder.tvVencimiento.setText("📅 Vence: " + p.fechaVencimiento.substring(0, 10));

        // Estado visual según pago
        switch (p.estado.toLowerCase()) {
            case "pagado":
                holder.tvEstado.setText("🟢 Pagado el " + (p.fechaPago != null ? p.fechaPago.substring(0, 10) : ""));
                holder.tvEstado.setTextColor(holder.tvEstado.getContext().getColor(R.color.success));
                break;
            case "pendiente":
                holder.tvEstado.setText("🟡 Pendiente");
                holder.tvEstado.setTextColor(holder.tvEstado.getContext().getColor(R.color.warning));
                break;
            default:
                holder.tvEstado.setText("🔴 Vencido");
                holder.tvEstado.setTextColor(holder.tvEstado.getContext().getColor(R.color.md_error));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvConcepto, tvMonto, tvVencimiento, tvEstado;
        VH(@NonNull View itemView) {
            super(itemView);
            tvConcepto = itemView.findViewById(R.id.tvConcepto);
            tvMonto = itemView.findViewById(R.id.tvMonto);
            tvVencimiento = itemView.findViewById(R.id.tvVencimiento);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}

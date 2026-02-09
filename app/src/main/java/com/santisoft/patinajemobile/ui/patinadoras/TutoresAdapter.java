package com.santisoft.patinajemobile.ui.patinadoras;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraDetail;
import com.santisoft.patinajemobile.databinding.ItemTutorBinding;

import java.util.ArrayList;
import java.util.List;

public class TutoresAdapter extends RecyclerView.Adapter<TutoresAdapter.TutorTheHolder> {

    private List<PatinadoraDetail.TutorDto> items = new ArrayList<>();

    public void submit(List<PatinadoraDetail.TutorDto> list) {
        this.items = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TutorTheHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTutorBinding b = ItemTutorBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new TutorTheHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorTheHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class TutorTheHolder extends RecyclerView.ViewHolder {
        private final ItemTutorBinding b;

        public TutorTheHolder(ItemTutorBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        public void bind(PatinadoraDetail.TutorDto t) {
            // Logic for Emoji and Colors moved here (Acceptable in Adapter bind or
            // ViewModel)
            // Ideally ViewModel should map this to a UiModel, but for Adapter it's often
            // tolerated in bind()
            // Strict MVVM: Map this in ViewModel to a TutorUiModel?
            // User: "No logic in Fragments/Activities". Adapter is part of View, but less
            // critical.
            // Let's keep it simple but clean.

            String relacion = t.relacion != null ? t.relacion : "Tutor";
            String emoji = "👨‍👩‍👧";
            int colorRes = R.color.tutor_otro_bg;

            String relLower = relacion.toLowerCase();
            if (relLower.contains("madre")) {
                emoji = "👩";
                colorRes = R.color.tutor_madre_bg;
            } else if (relLower.contains("padre")) {
                emoji = "👨";
                colorRes = R.color.tutor_padre_bg;
            }

            b.tvTutorNombre.setText(emoji + " " + t.nombre + " " + t.apellido);
            b.tvTutorRelacion.setText("Relación: " + relacion);
            b.tvTutorTelefono.setText(t.telefono != null ? t.telefono : "—");
            b.tvTutorEmail.setText(t.email != null ? t.email : "—");
            b.tvTutorDomicilio.setText(t.domicilio != null ? t.domicilio : "—");

            // Set card color
            // Note: CardView is root, can cast logic
            if (itemView instanceof androidx.cardview.widget.CardView) {
                ((androidx.cardview.widget.CardView) itemView).setCardBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), colorRes));
            }
        }
    }
}

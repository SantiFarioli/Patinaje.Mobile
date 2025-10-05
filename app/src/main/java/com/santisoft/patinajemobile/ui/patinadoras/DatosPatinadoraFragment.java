package com.santisoft.patinajemobile.ui.patinadoras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraDetail;
import com.santisoft.patinajemobile.databinding.FragmentDatosPatinadoraBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatosPatinadoraFragment extends Fragment {

    private FragmentDatosPatinadoraBinding binding;
    private DetallePatinadoraViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDatosPatinadoraBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(DetallePatinadoraViewModel.class);

        viewModel.getPatinador().observe(getViewLifecycleOwner(), p -> {
            if (p == null) return;

            // 🔹 Limpiar contenedores antes de volver a cargar
            binding.containerDatos.removeAllViews();
            binding.containerTutores.removeAllViews();

            // ==== Datos básicos (cada uno en card con color suave) ====
            setDatoCard(binding.containerDatos, "🆔 DNI", safe(p.dni), R.color.bg_item_purple);
            setDatoCard(binding.containerDatos, "🏠 Domicilio", safe(p.domicilio), R.color.bg_item_green);
            setDatoCard(binding.containerDatos, "🎂 Nacimiento", getNacimientoConEdad(p.fechaNacimiento), R.color.bg_item_orange);
            setDatoCard(binding.containerDatos, "🩺 Ficha médica", safe(p.fichaMedica), R.color.bg_item_blue);
            setDatoCard(binding.containerDatos, "⚡ Estado", p.activo ? "Activa" : "Inactiva", R.color.bg_item_yellow);

            // ==== Tutores ====
            if (p.tutores != null && !p.tutores.isEmpty()) {
                for (PatinadoraDetail.TutorDto t : p.tutores) {
                    // Emoji según relación
                    String emoji;
                    if (t.relacion != null) {
                        String rel = t.relacion.toLowerCase();
                        if (rel.contains("madre")) emoji = "👩";
                        else if (rel.contains("padre")) emoji = "👨";
                        else emoji = "👨‍👩‍👧";
                    } else {
                        emoji = "👨‍👩‍👧";
                    }

                    // === CardView del tutor ===
                    CardView card = new CardView(requireContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    int margin = (int) getResources().getDimension(R.dimen.space_8);
                    params.setMargins(0, 0, 0, margin);
                    card.setLayoutParams(params);
                    card.setRadius(16f);
                    card.setCardElevation(4f);

                    // 👉 Color según relación
                    if (t.relacion != null) {
                        String rel = t.relacion.toLowerCase();
                        if (rel.contains("madre")) {
                            card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.tutor_madre_bg));
                        } else if (rel.contains("padre")) {
                            card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.tutor_padre_bg));
                        } else {
                            card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.tutor_otro_bg));
                        }
                    } else {
                        card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.tutor_otro_bg));
                    }

                    // Contenedor interno
                    LinearLayout inner = new LinearLayout(requireContext());
                    inner.setOrientation(LinearLayout.VERTICAL);
                    inner.setPadding(margin, margin, margin, margin);

                    // Nombre
                    TextView tvNombre = new TextView(requireContext());
                    tvNombre.setText(emoji + " " + t.nombre + " " + t.apellido);
                    tvNombre.setTextAppearance(R.style.Text_Subtitle);
                    inner.addView(tvNombre);

                    // Relación
                    TextView tvRelacion = new TextView(requireContext());
                    tvRelacion.setText("Relación: " + safe(t.relacion));
                    tvRelacion.setTextAppearance(R.style.Text_Body);
                    inner.addView(tvRelacion);

                    // Teléfono
                    TextView tvTel = new TextView(requireContext());
                    tvTel.setText("📞 " + safe(t.telefono));
                    tvTel.setTextAppearance(R.style.Text_Body);
                    inner.addView(tvTel);

                    // Email
                    TextView tvEmail = new TextView(requireContext());
                    tvEmail.setText("✉️ " + safe(t.email));
                    tvEmail.setTextAppearance(R.style.Text_Body);
                    inner.addView(tvEmail);

                    // Domicilio
                    TextView tvDom = new TextView(requireContext());
                    tvDom.setText("🏠 " + safe(t.domicilio));
                    tvDom.setTextAppearance(R.style.Text_Body);
                    inner.addView(tvDom);

                    card.addView(inner);
                    binding.containerTutores.addView(card);
                }
            } else {
                TextView tv = new TextView(requireContext());
                tv.setText("No hay tutores registrados");
                tv.setTextAppearance(R.style.EmptyState);
                binding.containerTutores.addView(tv);
            }
        });

        // ==== Botón volver ====
        binding.btnVolver.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack()
        );

        return binding.getRoot();
    }

    // === Helper para los datos de la patinadora ===
    private void setDatoCard(LinearLayout parent, String titulo, String valor, int colorRes) {
        CardView card = new CardView(requireContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int margin = (int) getResources().getDimension(R.dimen.space_8);
        params.setMargins(0, 0, 0, margin);
        card.setLayoutParams(params);
        card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), colorRes));
        card.setRadius(16f);
        card.setCardElevation(2f);

        LinearLayout inner = new LinearLayout(requireContext());
        inner.setOrientation(LinearLayout.HORIZONTAL);
        inner.setPadding(margin, margin, margin, margin);

        TextView tvTitulo = new TextView(requireContext());
        tvTitulo.setText(titulo + ": ");
        tvTitulo.setTextAppearance(R.style.Text_Body);
        tvTitulo.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_on_surface));

        TextView tvValor = new TextView(requireContext());
        tvValor.setText(valor);
        tvValor.setTextAppearance(R.style.Text_Body);
        tvValor.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_on_surface));

        inner.addView(tvTitulo);
        inner.addView(tvValor);

        card.addView(inner);
        parent.addView(card);
    }

    private String getNacimientoConEdad(String fecha) {
        try {
            String f = fecha.substring(0, 10);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Calendar nac = Calendar.getInstance();
            nac.setTime(sdf.parse(f));
            Calendar hoy = Calendar.getInstance();
            int edad = hoy.get(Calendar.YEAR) - nac.get(Calendar.YEAR);
            if (hoy.get(Calendar.DAY_OF_YEAR) < nac.get(Calendar.DAY_OF_YEAR)) {
                edad--;
            }
            return f + " (" + edad + " años)";
        } catch (Exception e) {
            return safe(fecha);
        }
    }

    private String safe(Object o) {
        return o == null ? "—" : String.valueOf(o);
    }
}

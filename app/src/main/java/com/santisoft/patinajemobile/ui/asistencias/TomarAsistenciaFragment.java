package com.santisoft.patinajemobile.ui.asistencias;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.santisoft.patinajemobile.databinding.FragmentTomarAsistenciaBinding;
import com.santisoft.patinajemobile.util.DialogUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TomarAsistenciaFragment extends Fragment {

    private FragmentTomarAsistenciaBinding binding;
    private AsistenciasViewModel viewModel;
    private PlanillaAdapter adapter;
    private Calendar calendario = Calendar.getInstance();
    private SweetAlertDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTomarAsistenciaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AsistenciasViewModel.class);
        adapter = new PlanillaAdapter();

        binding.recyclerPlanilla.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerPlanilla.setAdapter(adapter);

        // Actualizar texto fecha inicial
        actualizarFechaUI();

        // Eventos
        binding.btnFecha.setOnClickListener(v -> mostrarDatePicker());
        binding.btnGuardar.setOnClickListener(v -> viewModel.guardarCambios(adapter.getData()));

        // Observadores
        viewModel.getPlanilla().observe(getViewLifecycleOwner(), lista -> {
            adapter.submit(lista);
        });

        viewModel.getDialogEvent().observe(getViewLifecycleOwner(), event -> {
            if (event == null) return;

            switch (event.getType()) {
                case LOADING:
                    if (pDialog != null && pDialog.isShowing()) pDialog.dismiss();
                    pDialog = DialogUtils.showLoading(requireContext(), event.getTitle());
                    break;

                case HIDE_LOADING:
                    if (pDialog != null) pDialog.dismissWithAnimation();
                    break;

                case SUCCESS:
                    if (pDialog != null) pDialog.dismiss();

                    // Mostramos el éxito
                    DialogUtils.showSuccess(requireContext(), event.getTitle(), event.getMessage());

                    // ⏱️ Esperamos 1.5 seg y volvemos al inicio automáticamente
                    new android.os.Handler().postDelayed(() -> {
                        if (isAdded()) {
                            // ✅ FORMA CORRECTA Y MODERNA:
                            androidx.navigation.fragment.NavHostFragment.findNavController(this).popBackStack();
                        }
                    }, 1500);
                    break;

                case ERROR:
                    if (pDialog != null) pDialog.dismiss();
                    DialogUtils.showError(requireContext(), event.getTitle(), event.getMessage());
                    break;
            }
        });

        // Cargar datos iniciales
        viewModel.cargarPlanilla(calendario.getTime());
    }

    private void mostrarDatePicker() {
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, month);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarFechaUI();

            // Recargar lista al cambiar fecha
            viewModel.cargarPlanilla(calendario.getTime());

        }, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void actualizarFechaUI() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());
        binding.tvFecha.setText(sdf.format(calendario.getTime()));
    }
}
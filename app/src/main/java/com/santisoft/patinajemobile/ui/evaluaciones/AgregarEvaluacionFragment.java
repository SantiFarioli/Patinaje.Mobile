package com.santisoft.patinajemobile.ui.evaluaciones;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.databinding.FragmentAgregarEvaluacionBinding;
import com.santisoft.patinajemobile.util.DialogUtils;

import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AgregarEvaluacionFragment extends Fragment {

    private FragmentAgregarEvaluacionBinding binding;
    private AgregarEvaluacionViewModel viewModel;
    private Calendar calendario = Calendar.getInstance();
    private SweetAlertDialog pDialog;

    // Variables temporales para simular selección (luego las cargaremos de API)
    private int selectedPatinadorId = 1;
    private int selectedTorneoId = 5;

    // Lanzador para seleccionar PDF
    private final ActivityResultLauncher<Intent> pdfLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        viewModel.seleccionarArchivo(uri);
                        // 👇 CAMBIO AQUÍ: Mostrar nombre real
                        String nombreArchivo = getFileName(uri);
                        binding.tvArchivoNombre.setText("📄 " + nombreArchivo);
                        binding.tvArchivoNombre.setTextColor(ContextCompat.getColor(requireContext(), R.color.success)); // Opcional: ponerlo en verde
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAgregarEvaluacionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AgregarEvaluacionViewModel.class);

        // Configurar UI
        binding.etFecha.setOnClickListener(v -> mostrarDatePicker());

        // Botón Adjuntar
        binding.btnAdjuntar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/pdf");
            pdfLauncher.launch(intent);
        });

        // Botón Guardar
        binding.btnGuardar.setOnClickListener(v -> {
            String obs = binding.etObservaciones.getText().toString();
            // TODO: Obtener IDs reales de los spinners
            viewModel.guardar(selectedPatinadorId, selectedTorneoId, calendario.getTime(), obs);
        });

        // Observadores
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
                    if (pDialog != null) pDialog.dismiss(); // 👈 IMPORTANTE: Cerrar carga
                    DialogUtils.showSuccess(requireContext(), event.getTitle(), event.getMessage());

                    // Volver atrás después de 1.5s
                    new android.os.Handler().postDelayed(() -> {
                        if (isAdded()) NavHostFragment.findNavController(this).popBackStack();
                    }, 1500);
                    break;

                case ERROR:
                    if (pDialog != null) pDialog.dismiss(); // 👈 IMPORTANTE: Cerrar carga
                    DialogUtils.showError(requireContext(), event.getTitle(), event.getMessage());
                    break;

                case WARNING: // Agregar este caso por si validas campos vacíos
                    DialogUtils.showWarning(requireContext(), event.getTitle(), event.getMessage());
                    break;
            }
        });

        // Cargar Spinners dummy (luego conectamos con API real)
        setupDummySpinners();
    }

    private void mostrarDatePicker() {
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            calendario.set(year, month, dayOfMonth);
            binding.etFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        }, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setupDummySpinners() {
        String[] patinadoras = {"Camila Gómez", "Sofía Martínez"};
        ArrayAdapter<String> adapterP = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, patinadoras);
        binding.spPatinadora.setAdapter(adapterP);
        binding.spPatinadora.setText(patinadoras[0], false); // Preselección

        String[] torneos = {"Torneo Apertura 2025", "Nacional B"};
        ArrayAdapter<String> adapterT = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, torneos);
        binding.spTorneo.setAdapter(adapterT);
        binding.spTorneo.setText(torneos[0], false);
    }

    // Método para obtener el nombre del archivo desde el Uri
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = requireActivity().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
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
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraListItem;
import com.santisoft.patinajemobile.data.model.torneos.Torneo;
import com.santisoft.patinajemobile.databinding.FragmentAgregarEvaluacionBinding;
import com.santisoft.patinajemobile.util.DialogUtils;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AgregarEvaluacionFragment extends Fragment {

    private FragmentAgregarEvaluacionBinding binding;
    private AgregarEvaluacionViewModel viewModel;
    private Calendar calendario = Calendar.getInstance();
    private SweetAlertDialog pDialog;

    // ID del patinador recibido por argumento
    private int argPatinadorId = -1;

    // Adapters para los Dropdowns
    private ArrayAdapter<PatinadoraListItem> adapterPatinadoras;
    private ArrayAdapter<Torneo> adapterTorneos;

    // Variables para guardar selección actual
    private PatinadoraListItem selectedPatinadora = null;
    private Torneo selectedTorneo = null;

    private final ActivityResultLauncher<Intent> pdfLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        viewModel.seleccionarArchivo(uri);
                        String nombre = getFileName(uri);
                        binding.tvArchivoNombre.setText("📄 " + nombre);
                        binding.tvArchivoNombre.setTextColor(ContextCompat.getColor(requireContext(), R.color.success));
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

        if (getArguments() != null) {
            argPatinadorId = getArguments().getInt("patinadorId", -1);
        }

        // Configurar Spinners (AutoCompleteTextViews)
        adapterPatinadoras = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line);
        binding.spPatinadora.setAdapter(adapterPatinadoras);

        adapterTorneos = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line);
        binding.spTorneo.setAdapter(adapterTorneos);

        // Manejar clics en los items de los dropdowns
        binding.spPatinadora.setOnItemClickListener((parent, v, position, id) -> {
            selectedPatinadora = adapterPatinadoras.getItem(position);
        });

        binding.spTorneo.setOnItemClickListener((parent, v, position, id) -> {
            selectedTorneo = adapterTorneos.getItem(position);
        });

        // Configurar Botones y Fecha
        binding.etFecha.setOnClickListener(v -> mostrarDatePicker());

        binding.btnAdjuntar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/pdf");
            pdfLauncher.launch(intent);
        });

        binding.btnGuardar.setOnClickListener(v -> {
            // Validar selección de torneo
            if (selectedTorneo == null) {
                Toast.makeText(getContext(), "Selecciona un Torneo", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar selección de patinadora (si no vino por argumento)
            int idPat = argPatinadorId;
            if (idPat == -1) {
                if (selectedPatinadora != null) {
                    idPat = selectedPatinadora.patinadorId;
                } else {
                    Toast.makeText(getContext(), "Selecciona una Patinadora", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            String obs = binding.etObservaciones.getText().toString();
            viewModel.guardar(idPat, selectedTorneo.torneoId, calendario.getTime(), obs);
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
                    if (pDialog != null) pDialog.dismiss();
                    DialogUtils.showSuccess(requireContext(), event.getTitle(), event.getMessage());
                    new android.os.Handler().postDelayed(() -> {
                        if (isAdded()) NavHostFragment.findNavController(this).popBackStack();
                    }, 1500);
                    break;
                case ERROR:
                case WARNING:
                    if (pDialog != null) pDialog.dismiss();
                    DialogUtils.showError(requireContext(), event.getTitle(), event.getMessage());
                    break;
            }
        });

        viewModel.getPatinadoras().observe(getViewLifecycleOwner(), lista -> {
            adapterPatinadoras.clear();
            if (lista != null) {
                adapterPatinadoras.addAll(lista);
                // Auto-seleccionar si tenemos ID
                if (argPatinadorId != -1) {
                    for (PatinadoraListItem p : lista) {
                        if (p.patinadorId == argPatinadorId) {
                            binding.spPatinadora.setText(p.toString(), false);
                            binding.spPatinadora.setEnabled(false); // Bloquear
                            selectedPatinadora = p;
                            break;
                        }
                    }
                }
            }
        });

        viewModel.getTorneos().observe(getViewLifecycleOwner(), lista -> {
            adapterTorneos.clear();
            if (lista != null) adapterTorneos.addAll(lista);
        });

        // Cargar datos
        viewModel.cargarDatos();
    }

    private void mostrarDatePicker() {
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            calendario.set(year, month, dayOfMonth);
            binding.etFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        }, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH)).show();
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = requireActivity().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) result = cursor.getString(index);
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) result = result.substring(cut + 1);
        }
        return result;
    }
}
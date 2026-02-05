package com.santisoft.patinajemobile.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView; // Importante para manejar el título si reusamos layout

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.data.model.eventos.Evento;
import com.santisoft.patinajemobile.data.model.torneos.Torneo;
import com.santisoft.patinajemobile.data.remote.EvaluacionesApi;
import com.santisoft.patinajemobile.data.remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TorneosFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        // Reutilizamos el layout de Pagos porque ya tiene un RecyclerView y un TextView
        // arriba
        // Si quieres ser más prolijo, duplica ese XML y llámalo
        // fragment_lista_simple.xml
        return inflater.inflate(R.layout.fragment_pagos_patinadora, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Configurar Título (opcional, ya que reusamos layout)
        TextView tvTitulo = view.findViewById(R.id.tvResumen);
        tvTitulo.setText("Calendario de Torneos");
        tvTitulo.setVisibility(View.VISIBLE);

        // 2. Configurar RecyclerView
        RecyclerView recycler = view.findViewById(R.id.recyclerPagos); // Ojo: Usamos el ID del layout de pagos
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        EventosAdapter adapter = new EventosAdapter();
        adapter.setListener(evento -> {
            android.widget.Toast.makeText(getContext(),
                    "Detalle de " + evento.nombre + " próximamente...",
                    android.widget.Toast.LENGTH_SHORT).show();
        });
        recycler.setAdapter(adapter);

        // 3. Llamar a la API
        EvaluacionesApi api = RetrofitClient.get(requireContext()).evaluacionesApi();

        api.getTorneos().enqueue(new Callback<List<Torneo>>() {
            @Override
            public void onResponse(Call<List<Torneo>> call, Response<List<Torneo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Convertir Torneo -> Evento para poder usar el mismo Adapter
                    List<Evento> eventos = new ArrayList<>();
                    for (Torneo t : response.body()) {
                        Evento e = new Evento();
                        e.nombre = t.nombre;
                        e.lugar = t.lugar;
                        e.fechaInicio = t.fechaInicio;
                        eventos.add(e);
                    }
                    adapter.submit(eventos);
                }
            }

            @Override
            public void onFailure(Call<List<Torneo>> call, Throwable t) {
            }
        });
    }
}
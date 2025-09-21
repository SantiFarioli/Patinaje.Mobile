package com.santisoft.patinajemobile.ui.main;

import android.os.Bundle;

import androidx.activity.ComponentActivity;

import com.santisoft.patinajemobile.databinding.ActivityMainBinding;

public class MainActivity extends ComponentActivity {
    private ActivityMainBinding vb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        vb.textView.setText("¡Login OK! (acá va el Dashboard)");
    }
}

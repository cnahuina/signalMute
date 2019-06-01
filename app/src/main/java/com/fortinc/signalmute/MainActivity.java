package com.fortinc.signalmute;

import android.content.Intent;
import android.speech.RecognizerIntent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView txvResult;
    private ImageView btnVoz;
    public static final int REQUEST_CODE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializandoVariables();
    }

    private void inicializandoVariables() {
        txvResult = findViewById(R.id.txvResult);
        btnVoz = findViewById(R.id.ivButton);
        btnVoz.setOnClickListener(this);
    }

    public void getSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            Toast.makeText(this, getString(R.string.dispositivo_no_soportado), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                txvResult.setText(result.get(0));
                analizarTexto(result);
            }
        }
    }

    private void analizarTexto(ArrayList<String> result) {
        String palabra = result.get(0);
        String[] p = separarPalabras(palabra);
        for (int i = 0; i < p.length; i++) {
            Log.d("textsssa", "analizarTexto: " + p[i]);
        }
    }

    public static String[] separarPalabras(String s) {
        int cp = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                cp++;
            }
        }
        String[] partes = new String[cp + 1];
        for (int i = 0; i < partes.length; i++) {
            partes[i] = "";
        }
        int ind = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                ind++;
                continue;
            }
            partes[ind] += s.charAt(i);
        }
        return partes;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivButton:
                getSpeechInput();
                break;
        }
    }
}

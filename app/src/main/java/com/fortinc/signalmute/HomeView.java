package com.fortinc.signalmute;

import android.content.Intent;
import android.speech.RecognizerIntent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fortinc.signalmute.model.MySingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class HomeView extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 777;
    private TextView txvResult;
    private ImageView btnVoz, ivTrad, ivUser;
    private LinearLayout linlay, linlay1, linlay2;
    private Animation uptodown, downtoup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_view);
        inicializandoVariables();
    }

    private void inicializandoVariables() {
        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        txvResult = findViewById(R.id.txvResult);
        btnVoz = findViewById(R.id.ivButton);
        btnVoz.setAnimation(uptodown);
        btnVoz.setOnClickListener(this);
        linlay = findViewById(R.id.linlay);
        linlay.setAnimation(downtoup);
        linlay1 = findViewById(R.id.linlay1);
        linlay1.setOnClickListener(this);
        linlay2 = findViewById(R.id.linlay2);
        linlay2.setOnClickListener(this);
        ivTrad = findViewById(R.id.ivTrad);
        ivTrad.setOnClickListener(this);
        ivUser = findViewById(R.id.ivUser);
        ivUser.setOnClickListener(this);
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
        if (!palabra.isEmpty()) {
            String[] p = separarPalabras(palabra);
            for (int i = 0; i < p.length; i++) {
                Log.d("textsssa", "analizarTexto: " + p[i]);
                postImage(p[i]);
            }
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

    private void setupDesignBottomBar(int ivTrad, int ivUser) {
        this.ivTrad.setImageResource(ivTrad);
        this.ivUser.setImageResource(ivUser);
    }

    private void postImage(String palabra) {
        String url = "http://c90f7dda.ngrok.io/rest/index.php/Buscar/buscarNDatos/" + palabra;
        try {
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                            Log.d("Response", response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.getMessage());
                        }
                    }
            );
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(getRequest);
        } catch (Exception e) {
            Log.e("Post", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivButton:
                getSpeechInput();
                break;
            case R.id.linlay1:
                setupDesignBottomBar(R.drawable.traductor_ic, R.drawable.user_ic_off);
                break;
            case R.id.linlay2:
                setupDesignBottomBar(R.drawable.traductor_ic_off, R.drawable.user_ic);
                break;
            case R.id.ivTrad:
                setupDesignBottomBar(R.drawable.traductor_ic, R.drawable.user_ic_off);
                break;
            case R.id.ivUser:
                setupDesignBottomBar(R.drawable.traductor_ic_off, R.drawable.user_ic);
                break;
        }
    }
}

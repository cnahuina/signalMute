package com.fortinc.signalmute;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fortinc.signalmute.Model.MySingleton;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {
    private TextView txvResult;
    public static final int REQUEST_CODE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txvResult = (TextView) findViewById(R.id.txvResult);
    }

    public void getSpeechInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
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
            postImage(p[i]);
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

    private void postImage(String palabra) {
        String url = "http://c90f7dda.ngrok.io/rest/index.php/Buscar/buscarNDatos/"+palabra;

        try {
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                            Log.d("Response", response.toString());
                        }
                    },
                    new Response.ErrorListener()
                    {
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
}

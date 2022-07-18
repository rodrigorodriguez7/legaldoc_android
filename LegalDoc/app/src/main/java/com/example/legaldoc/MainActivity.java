package com.example.legaldoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    EditText usuario, contrasena;
    Button comprobar;
    RequestQueue requestQueue;
    Boolean doubleBackToExitPressedOnce=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        usuario =(EditText) findViewById(R.id.Usuario);
        contrasena = (EditText) findViewById(R.id.Contrasena);
        comprobar = (Button) findViewById(R.id.Comprobar);

        if(getIntent().getBooleanExtra("EXIT",false)){
            finish();
        }else{
            verificarSession();

            comprobar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String u=usuario.getText().toString();
                        String c = contrasena.getText().toString();
                        if ((u.equals("")) || (c.equals("")) ){
                            Toast.makeText(getApplicationContext(), "LLenar los campos", Toast.LENGTH_SHORT).show();
                        }else{
                            Logear(u,c);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            finish();
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("EXIT",true);
            startActivity(i);

        }
        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(getApplicationContext(), "Pulse otra vez para salir de LegalDoc", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce= false;
            }
        },2000);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        verificarSession();
    }

    public void verificarSession(){
        SharedPreferences prefs = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);

        String tocken = prefs.getString("llave","");

        if(tocken.equals("")){

        }else{
            Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(getApplicationContext(), PaginaPrincipal.class);
            startActivity(i);
        }
    }

    public void Logear(String u, String c) throws JSONException {

            String url = "http://103.54.58.53:8080/legaldoc/api/v1/login";

            JSONObject jsonObject =  new JSONObject();
            jsonObject.put("correo",u);
            jsonObject.put("contrasena",c);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                JSONObject jsonObject = response.getJSONObject("data");

                                Toast.makeText(getApplicationContext(), "Bienvenido "+ jsonObject.getString("nombre"), Toast.LENGTH_LONG).show();

                                String llave = jsonObject.getString("tockenDeAcceso");
                                SharedPreferences prefs = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("llave",llave);
                                editor.commit();
                                Intent i = new Intent(getApplicationContext(), PaginaPrincipal.class);
                                startActivity(i);

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Error = "+ e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Verifique los datos e intentelo nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
            )
                    ;
            Volley.newRequestQueue(this).add(jsonObjectRequest);
            //requestQueue.add(jsonObjectRequest);
        }

    public void Registrarse(View v){
        startActivity(new Intent(getApplicationContext(),Registrarse.class));
    }
}
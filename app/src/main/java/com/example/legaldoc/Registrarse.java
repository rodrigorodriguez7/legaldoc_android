package com.example.legaldoc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Registrarse extends AppCompatActivity{

    EditText apellido,nombre,fechaNac,correo, contrasena,repetirContrasena;
    Button registrar;
    Boolean doubleBackToExitPressedOnce = false;
    private int dia, mes, year;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        apellido = (EditText) findViewById(R.id.Apellido);
        nombre = (EditText) findViewById(R.id.Nombre);
        fechaNac = (EditText) findViewById(R.id.FechaNac);
        correo = (EditText) findViewById(R.id.Correo);
        contrasena = (EditText) findViewById(R.id.Contrasena);
        repetirContrasena = (EditText) findViewById(R.id.RepetirContrasena);
        registrar = (Button) findViewById(R.id.Registrar);

        fechaNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c= Calendar.getInstance();
                dia=c.get(Calendar.DAY_OF_MONTH);
                mes=c.get(Calendar.MONTH);
                year=c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog =  new DatePickerDialog(Registrarse.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String tdia,tmes;

                        if (dayOfMonth <= 9){
                            tdia= "0"+dayOfMonth;
                        }else{
                            tdia = String.valueOf(dayOfMonth);
                        }

                        if ((month+1) <= 9){
                            tmes= "0"+(month+1);
                        }else{
                            tmes = String.valueOf((month+1));
                        }

                        fechaNac.setText(year+"-"+tmes+"-"+tdia);
                    }
                }, year,mes,dia);
                datePickerDialog.show();
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n,a,fn,cor,con,conC;
                n=apellido.getText().toString();
                a=nombre.getText().toString();
                fn=fechaNac.getText().toString();
                cor=correo.getText().toString();
                con=contrasena.getText().toString();
                conC=repetirContrasena.getText().toString();
                try {
                    if ((n.equals("")) || (a.equals("")) || (fn.equals("")) || (cor.equals("")) || (con.equals("")) || (conC.equals("")) ){
                        Toast.makeText(getApplicationContext(), "LLenar los campos", Toast.LENGTH_LONG).show();
                    }else{
                        if ((con.equals(conC))){
                            EnviarDatos(n,a,fn,cor,con,conC);
                        }else{
                            Toast.makeText(getApplicationContext(), "La contraseñas no son iguales", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void EnviarDatos(String n, String a, String fn, String cor, String con, String conC) throws JSONException {
        String url = "http://103.54.58.53:8080/legaldoc/api/v1/register";

            JSONObject jsonObject =  new JSONObject();

            jsonObject.put("nombre",n);
            jsonObject.put("apellido",a);
            jsonObject.put("fechaNacimiento",fn);
            jsonObject.put("correo",cor);
            jsonObject.put("contrasena",con);
            jsonObject.put("contrasenaConfirmacion",conC);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(getApplicationContext(), "Se ha regitrado correctamente", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error = "+ e, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Verifique los datos e intentelo nuevamente", Toast.LENGTH_LONG).show();
            }
        }
        );
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

}
package com.example.legaldoc.ui.gallery;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.legaldoc.MainActivity;
import com.example.legaldoc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GalleryFragment extends Fragment {

    EditText Actapellido,Actnombre,ActfechaNac,Actcorreo, Actcontrasena,ActnuevaContrasena;
    Button actualizar;
    private int dia, mes, year;
    RequestQueue requestQueue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        Actapellido = (EditText) root.findViewById(R.id.ActApellido);
        Actnombre = (EditText) root.findViewById(R.id.ActNombre);
        ActfechaNac = (EditText) root.findViewById(R.id.ActFechaNac);
        Actcorreo = (EditText) root.findViewById(R.id.ActCorreo);
        Actcontrasena = (EditText) root.findViewById(R.id.ActContrasena);
        ActnuevaContrasena = (EditText) root.findViewById(R.id.ActRepetirContrasena);
        actualizar = (Button) root.findViewById(R.id.Actualizar);

        try {
            obtenerDatos();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ActfechaNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c= Calendar.getInstance();
                dia=c.get(Calendar.DAY_OF_MONTH);
                mes=c.get(Calendar.MONTH);
                year=c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog =  new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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

                        ActfechaNac.setText(year+"-"+tmes+"-"+tdia);
                    }
                }, year,mes,dia);
                datePickerDialog.show();
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n,a,fn,cor,con,conC;
                n=Actapellido.getText().toString();
                a=Actnombre.getText().toString();
                fn=ActfechaNac.getText().toString();
                cor=Actcorreo.getText().toString();
                con=Actcontrasena.getText().toString();
                conC=ActnuevaContrasena.getText().toString();

                try {
                    if ((n.equals("")) || (a.equals("")) || (fn.equals("")) || (cor.equals("")) || (con.equals("")) || (conC.equals("")) ){
                        Toast.makeText(getContext().getApplicationContext(), "LLenar los campos", Toast.LENGTH_LONG).show();
                    }else{
                        EnviarDatos(n,a,fn,cor);
                        ActualizarCntraseña(con,conC);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return root;
    }

        public void EnviarDatos(String n, String a, String fn, String cor) throws JSONException {
            String url = "http://103.54.58.53:8080/legaldoc/api/v1/cliente/authorization";
            SharedPreferences prefs = getContext().getSharedPreferences("shared_datos_user", Context.MODE_PRIVATE);

            prefs.getString("fotoPerfil","");
            prefs.getString("descripcionUsuario","");
            prefs.getString("fechaCreacionCuenta","");

            SharedPreferences prefs2 = getContext().getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);

            String tocken = prefs2.getString("llave","");

            JSONObject jsonObject =  new JSONObject();

            jsonObject.put("fotoPerfil",prefs.getString("fotoPerfil",""));
            jsonObject.put("apellidoUsuario",a);
            jsonObject.put("nombreUsuario",n);
            jsonObject.put("descripcionUsuario",prefs.getString("descripcionUsuario",""));
            jsonObject.put("fechaCreacionCuenta",prefs.getString("fechaCreacionCuenta",""));
            jsonObject.put("correo",cor);
            jsonObject.put("fechaNacimiento",fn);

            //jsonObject.put("contrasena",con);
            //jsonObject.put("contrasenaConfirmacion",conC);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                startActivity(new Intent(getContext().getApplicationContext(), MainActivity.class));
                            } catch (Exception e) {
                                Toast.makeText(getContext().getApplicationContext(), "Error = "+ e, Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext().getApplicationContext(), "Ha ocurrido un error intentelo nuevamente", Toast.LENGTH_LONG).show();
                }
            }
            )
            {
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap header = new HashMap();
                    header.put("Content-Type","application/json");
                    header.put("Authorization",tocken);

                    return header;
                }}
                    ;
            requestQueue.add(jsonObjectRequest);
        }

        public void ActualizarCntraseña(String con, String conC) throws JSONException {
            String url = "http://103.54.58.53:8080/legaldoc/api/v1/cliente/authorization/password";

            SharedPreferences prefs2 = getContext().getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);

            String tocken = prefs2.getString("llave","");

            JSONObject jsonObject =  new JSONObject();

            jsonObject.put("contrasenaActual",con);
            jsonObject.put("contrasena",conC);
            jsonObject.put("contrasenaConfirmacion",conC);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Toast.makeText(getContext().getApplicationContext(), "Se ha actualizado correctamente", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getContext().getApplicationContext(), MainActivity.class));
                            } catch (Exception e) {
                                Toast.makeText(getContext().getApplicationContext(), "Error = "+ e, Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext().getApplicationContext(), "Ha ocurrido un error intentelo nuevamente", Toast.LENGTH_LONG).show();
                }
            }
            )
            {
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap header = new HashMap();
                    header.put("Content-Type","application/json");
                    header.put("Authorization",tocken);

                    return header;
                }}
                    ;
            requestQueue.add(jsonObjectRequest);
        }

        public void obtenerDatos() throws JSONException {

            SharedPreferences prefs = getContext().getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);

            String llave = prefs.getString("llave","");

            requestQueue = Volley.newRequestQueue(getContext());

            String url = "http://103.54.58.53:8080/legaldoc/api/v1/cliente/authorization";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                JSONObject jsonObject = response.getJSONObject("data");

                                SharedPreferences prefs = getContext().getSharedPreferences("shared_datos_user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("fotoPerfil",jsonObject.getString("fotoPerfil"));
                                editor.putString("apellidoUsuario",jsonObject.getString("apellidoUsuario"));
                                editor.putString("nombreUsuario",jsonObject.getString("nombreUsuario"));
                                editor.putString("descripcionUsuario",jsonObject.getString("descripcionUsuario"));
                                editor.putString("fechaCreacionCuenta",jsonObject.getString("fechaCreacionCuenta"));
                                editor.putString("correo",jsonObject.getString("correo"));
                                editor.putString("fechaNacimiento",jsonObject.getString("fechaNacimiento"));
                                editor.commit();

                                Actapellido.setText(jsonObject.getString("apellidoUsuario"));
                                Actnombre.setText(jsonObject.getString("nombreUsuario"));
                                ActfechaNac.setText(jsonObject.getString("fechaNacimiento"));
                                Actcorreo.setText(jsonObject.getString("correo"));

                            } catch (Exception e) {
                                Toast.makeText(getContext(), "Error = "+ e, Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Verifique los datos e intentelo nuevamente", Toast.LENGTH_LONG).show();
                }
            }
            )
            {
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap header = new HashMap();
                    header.put("Content-Type","application/json");
                    header.put("Authorization",llave);

                    return header;
                }};
            requestQueue.add(jsonObjectRequest);
        }
}
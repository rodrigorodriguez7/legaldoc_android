package com.example.legaldoc.ui.slideshow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.legaldoc.InformacionOrden;
import com.example.legaldoc.MainActivity;
import com.example.legaldoc.PaginaPrincipal;
import com.example.legaldoc.R;
import com.example.legaldoc.ui.home.HomeFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SlideshowFragment extends Fragment {

    RequestQueue requestQueue;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        requestQueue = Volley.newRequestQueue(getContext());

        final TextView textView = root.findViewById(R.id.text_slideshow);
        final Button siCerrar = root.findViewById(R.id.siCerrar);
        final Button noCerrar = root.findViewById(R.id.noCerrar);

        siCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSession();
            }
        });

        noCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), PaginaPrincipal.class);
                startActivity(i);
            }
        });

        return root;
    }

    public void cerrarSession(){

        SharedPreferences prefs = getContext().getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);

        String llave = prefs.getString("llave","");

        String url = "http://103.54.58.53:8080/legaldoc/api/v1/logout";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            getActivity().onBackPressed();
                            startActivity(new Intent(getContext(), MainActivity.class));
                            Toast.makeText(getContext(), "Hasta pronto", Toast.LENGTH_LONG).show();
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

        SharedPreferences prefs2 = getContext().getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs2.edit();
        editor.clear();
        editor.commit();

    }


}
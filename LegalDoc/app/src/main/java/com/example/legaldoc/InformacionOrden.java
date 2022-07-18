package com.example.legaldoc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.legaldoc.ListViewTemplates.ListViewAdapterOrdenes;
import com.example.legaldoc.ListViewTemplates.ListViewAdapterPropio;
import com.example.legaldoc.ListViewTemplates.Ordenes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

public class InformacionOrden extends AppCompatActivity {

    Boolean doubleBackToExitPressedOnce =  false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_orden);

        TextView textView = findViewById(R.id.lsttxtmontoOrden);

        SharedPreferences prefs = getSharedPreferences("shared_orden", Context.MODE_PRIVATE);

        String mO = prefs.getString("montoOrden","");

        textView.setText("El monto total es: "+mO);

        ListView lstinfoOrden = (ListView)findViewById(R.id.lstinfoOrden);

        ListViewAdapterOrdenes adapter = new ListViewAdapterOrdenes(this);

        lstinfoOrden.setAdapter(adapter);

    }
}
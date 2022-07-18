package com.example.legaldoc.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import com.example.legaldoc.ListViewTemplates.ListViewAdapterPropio;
import com.example.legaldoc.ListViewTemplates.Solicitudes;
import com.example.legaldoc.PaginaPrincipal;
import com.example.legaldoc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private Context context = getContext();
    TextView textView, textNombre, textCorreo;
    View root;

    private List<Solicitudes> solicitudes = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        View root1 = inflater.inflate(R.layout.nav_header_main, container, false);
        textView = root.findViewById(R.id.text_home);
        ListView lstView = root.findViewById(R.id.lstSolicitudes);

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                //Alternativa 2:
                String opcionSeleccionada = ((TextView)v.findViewById(R.id.lblid)).getText().toString();
                String opcionSeleccionada2 = ((TextView)v.findViewById(R.id.lblmontoOrden)).getText().toString();

                SharedPreferences prefs = getContext().getSharedPreferences("shared_orden", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("id",opcionSeleccionada);
                editor.putString("montoOrden",opcionSeleccionada2);
                editor.commit();

                Intent i = new Intent(getContext(), InformacionOrden.class);
                startActivity(i);

            }
        });

        ListViewAdapterPropio adapter = new ListViewAdapterPropio(getContext());

        lstView.setAdapter(adapter);

        textNombre=root.findViewById(R.id.textNombre);
        textCorreo=root1.findViewById(R.id.textCorreo);
        SharedPreferences prefs = getContext().getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);

        //String nombre = prefs.getString("nombre","");
        String correo = prefs.getString("correo","");

        // textNombre.setText(nombre);
        textCorreo.setText("hola");

        return root;
    }
}


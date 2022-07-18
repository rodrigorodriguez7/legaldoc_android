package com.example.legaldoc.ListViewTemplates;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.legaldoc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewAdapterOrdenes  extends ArrayAdapter<Ordenes> {

    private List<Ordenes> ordenes = new ArrayList<>();
    RequestQueue requestQueue;

    public ListViewAdapterOrdenes(Context context){
        super(context, R.layout.listview_template);

        SharedPreferences prefs = context.getSharedPreferences("shared_orden", Context.MODE_PRIVATE);

        String id = prefs.getString("id","");

        requestQueue = Volley.newRequestQueue(context);

        String url = "http://103.54.58.53:8080/legaldoc/api/v1/order/";

        String urlCustom = url+id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlCustom,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("data");
                            ordenes = parseJson(jsonObject);
                            notifyDataSetChanged();

                        } catch (Exception e) {
                            Toast.makeText(context, "Error = "+ e, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Verifique los datos e intentelo nuevamente", Toast.LENGTH_LONG).show();
            }
        }
        )
        ;
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public int getCount(){
        return ordenes != null ? ordenes.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.listview_ordenes, null);

        Ordenes orden = ordenes.get(position);


        TextView lblnombreServicio = (TextView)item.findViewById(R.id.lblnombreServicio);
        TextView lblDescripcion = (TextView)item.findViewById(R.id.lblDescripcion);
        TextView lblnombreAsesor = (TextView)item.findViewById(R.id.lblnombreAsesor);
        TextView lblstatusServicio = (TextView)item.findViewById(R.id.lblstatusServicio);

        lblnombreServicio.setText(orden.getNombreServicio());
        lblDescripcion.setText(orden.getDescriptionServicio());
        lblnombreAsesor.setText(orden.getNombre()+" "+orden.getApellido());
        lblstatusServicio.setText(orden.getEstado());

        return(item);
    }

    public List<Ordenes> parseJson(JSONObject jsonObject){
        List<Ordenes> ordenes = new ArrayList<>();

        JSONArray jsonArray =  null;

        try {
            jsonArray =  jsonObject.getJSONArray("detallesOrden");

            for (int i = 0; i< jsonArray.length();i++){

                JSONObject obj = jsonArray.getJSONObject(i);
                JSONObject obj2 = obj.getJSONObject("asesor");
                Ordenes Ordenes1 = new Ordenes(obj.getString("nombreServicio"),obj.getString("descriptionServicio"),
                        obj.getString("estado"),obj2.getString("apellido"),obj2.getString("nombre"));

                ordenes.add(Ordenes1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return ordenes;
    }

}

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewAdapterPropio extends ArrayAdapter<Solicitudes>  {

    private List<Solicitudes> solicitudes = new ArrayList<>();
    RequestQueue requestQueue;

    public ListViewAdapterPropio(Context context){
        super(context, R.layout.listview_template);

        SharedPreferences prefs = context.getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);

        String llave = prefs.getString("llave","");

        requestQueue = Volley.newRequestQueue(context);

        String url = "http://103.54.58.53:8080/legaldoc/api/v1/order/user/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            solicitudes = parseJson(response);
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

    @Override
    public int getCount(){
        return solicitudes != null ? solicitudes.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.listview_template, null);

        Solicitudes solicitude = solicitudes.get(position);


        TextView lblnombrePagador = (TextView)item.findViewById(R.id.lbltotalServicios);
        TextView lblfechaCreacionOrden = (TextView)item.findViewById(R.id.lblfechaCreacionOrden);
        TextView lblmontoOrden = (TextView)item.findViewById(R.id.lblmontoOrden);
        TextView lblid = (TextView)item.findViewById(R.id.lblid);

        lblnombrePagador.setText(solicitude.getTotalServiciosn());
        lblfechaCreacionOrden.setText(solicitude.getFechaCreacionOrden());
        lblmontoOrden.setText(solicitude.getMontoOrden());
        lblid.setText(solicitude.getId());

        return(item);
    }

    public List<Solicitudes> parseJson(JSONObject jsonObject){
        List<Solicitudes> solicitudes = new ArrayList<>();

        JSONArray jsonArray =  null;

        try {
            jsonArray =  jsonObject.getJSONArray("data");

            for (int i = 0; i< jsonArray.length();i++){

                JSONObject obj = jsonArray.getJSONObject(i);
                Double montoAreglado = Double.valueOf(obj.getString("montoOrden"));
                DecimalFormat df = new DecimalFormat("0.00");
                String monto = df.format(montoAreglado);
                String nuevaFecha = obj.getString("fechaCreacionOrden").substring(0,10);

                Solicitudes solicitudes1 = new Solicitudes(obj.getString("id"),obj.getString("totalServicios"),
                        "$"+monto, nuevaFecha);

                solicitudes.add(solicitudes1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return solicitudes;
    }

}

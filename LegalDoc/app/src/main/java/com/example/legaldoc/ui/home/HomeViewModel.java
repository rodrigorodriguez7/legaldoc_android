package com.example.legaldoc.ui.home;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.legaldoc.ListViewTemplates.ListViewAdapterPropio;
import com.example.legaldoc.ListViewTemplates.Solicitudes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    RequestQueue requestQueue;

    public HomeViewModel() {

        List<Solicitudes> solicitudes = new ArrayList<>();
        mText = new MutableLiveData<>();
        mText.setValue("This is");

    }

    public LiveData<String> getText() {
        return mText;
    }

}
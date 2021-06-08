package com.midterm.pbl5.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.midterm.pbl5.Adapter.HistoryAdapter;
import com.midterm.pbl5.Model.History;
import com.midterm.pbl5.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<History> histories;
    private HistoryAdapter historyAdapter;
    private RecyclerView rvHistory;
//    private final static String URL = "http://192.168.1.91:81/PBL5/GetHistory.php";
    private final static String URL = "http://192.168.11.65:81/NhietDoDoAm/Gethistory.php";

    String id,chedo,loaidat,date,luongnuoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);




        histories = new ArrayList<>();

        rvHistory = findViewById(R.id.rv_history);

        rvHistory.setLayoutManager(new LinearLayoutManager(this));

//        historyAdapter = new HistoryAdapter(histories);
//        rvHistory.setAdapter(historyAdapter);

        ReceiveData();




    }
    public void ReceiveData()
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        id = object.getString("id");
                        luongnuoc = object.getString("luongnuoc");
                        chedo = object.getString("tenchedo");
                        loaidat = object.getString("tendat");
                        date = object.getString("thoigian");
                        History newhtr = new History(Integer.parseInt(id),Float.parseFloat(luongnuoc),loaidat,chedo, Timestamp.valueOf(date));
                        histories.add(newhtr);
                        historyAdapter = new HistoryAdapter(histories);
                        rvHistory.setAdapter(historyAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HistoryActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
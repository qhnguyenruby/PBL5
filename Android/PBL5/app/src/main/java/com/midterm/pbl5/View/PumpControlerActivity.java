package com.midterm.pbl5.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Spinner;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.midterm.pbl5.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PumpControlerActivity extends AppCompatActivity {

    Spinner spinner;
    EditText edtluongnuoc;
    String lc,loaidat;
    LottieAnimationView btn_tuoicay;
//    public final static String SendHistoryURL = "http://192.168.1.91:81/PBL5/SendHistorydata.php";
    public final static String SendHistoryURL = "http://192.168.11.65:81/PBL5/SendHistorydata.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pump_controler);
        spinner = findViewById(R.id.spinner);
        edtluongnuoc = findViewById(R.id.edtAction);
        btn_tuoicay = findViewById(R.id.btnSubmit);
        final ArrayList<String> listLoaidat = new ArrayList<>();
        listLoaidat.add("Đất cát");
        listLoaidat.add("Đất thịt pha cát");
        listLoaidat.add("Đất cát pha");
        listLoaidat.add("Đất thịt");
        listLoaidat.add("Đất sét");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listLoaidat);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PumpControlerActivity.this, listLoaidat.get(position), Toast.LENGTH_SHORT).show();
                loaidat =  String.valueOf(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        btn_tuoicay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btn_tuoicay.playAnimation();
//                Toast.makeText(PumpControlerActivity.this, "hihi", Toast.LENGTH_SHORT).show();
//            }
//        });

        btn_tuoicay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference idDat = database.getReference("iddat");
                DatabaseReference lenh = database.getReference("lenh");
                DatabaseReference luongnuoc = database.getReference("luongnuoc");
                lc = edtluongnuoc.getText().toString();


                if (lc.equals("")) {
                    Toast.makeText(PumpControlerActivity.this, "Nhập đúng dữ liệu số", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        lenh.setValue("0");
                        luongnuoc.setValue(lc);
                        idDat.setValue(loaidat);
                        TimeUnit.SECONDS.sleep(6);
                        lenh.setValue("1");
                        SendHistorydata();
                        btn_tuoicay.playAnimation();
                        Toast.makeText(PumpControlerActivity.this, "Thành công!", Toast.LENGTH_LONG).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void SendHistorydata() {
        StringRequest request = new StringRequest(Request.Method.POST, SendHistoryURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PumpControlerActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("luongnuoc", lc);
                params.put("chedo", "2");
                params.put("loaidat",loaidat);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}

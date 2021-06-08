package com.midterm.pbl5.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.midterm.pbl5.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

//    public static final String URL = "http://192.168.1.91:81/PBL5/getDataFromSensor.php";
    public static final String URL = "http://192.168.11.65:81/PBL5/getDataFromSensor.php";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private String id, nhietdo,doam,doamdat;
    private TextView tvNhietdo, tvDoam, tvDoamdat;
    private CardView cvDothiluongmua;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvNhietdo = findViewById(R.id.tv_nhietdo);
        tvDoam = findViewById(R.id.tv_doam);
        tvDoamdat = findViewById(R.id.tv_doamdat);
        cvDothiluongmua = findViewById(R.id.cv_dothiluongmua);


        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);

        toolbar=findViewById(R.id.toolbar);

        cvDothiluongmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Chart.class);
                startActivity(intent);
            }
        });

        ReceiveData();

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

    }



    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_pump_controller:
                Intent intent = new Intent(MainActivity.this, PumpControlerActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_history:
                Intent intent1 = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_chart:
                Intent intent2 = new Intent(MainActivity.this, Chart.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START); return true;
    }

    public void ReceiveData()
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        id = object.getString("id");
                        nhietdo = object.getString("nhietdo");
                        doam = object.getString("doam");
                        doamdat = object.getString("doamdat");

                        tvNhietdo.setText(nhietdo + " \u2103");
                        tvDoam.setText(doam + " %");
                        tvDoamdat.setText(doamdat + " %");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}
package com.midterm.pbl5.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.midterm.pbl5.Adapter.CustomMarkerView;
import com.midterm.pbl5.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Chart extends AppCompatActivity implements OnChartValueSelectedListener {

    LineChart lineChart;
    ArrayList<Entry> x1, x2;
    ArrayList<String> y;

//    List<Rainfall> list_luongmua;
//    private final static String URL = "http://192.168.1.91:81/PBL5/getLuongmua.php";
    private final static String URL = "http://192.168.11.65:81/PBL5/getLuongmua.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_chart);
//        list_luongmua = new ArrayList<>();


        lineChart = findViewById(R.id.lineChart);


        x1 = new ArrayList<Entry>();
        x2 = new ArrayList<Entry>();
        y = new ArrayList<String>();

        lineChart.setDrawGridBackground(false);
        lineChart.setDescription("Đồ thị lượng mưa");
        lineChart.setDrawBorders(true);
        lineChart.setBorderColor(Color.RED);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.getXAxis().setTextSize(15f);
        lineChart.getAxisLeft().setTextSize(15f);
        lineChart.animateXY(3000, 3000);
        lineChart.setTouchEnabled(true);
        CustomMarkerView mv = new CustomMarkerView(this, R.layout.textview, x1, x2, y);
        lineChart.setMarkerView(mv);

        XAxis xl = lineChart.getXAxis();
        xl.setAvoidFirstLastClipping(true);
        xl.setPosition(XAxis.XAxisPosition.TOP);
        xl.setLabelsToSkip(0);
        xl.setTextColor(Color.GREEN);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setInverted(false);
        leftAxis.setTextColor(Color.GREEN);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend l = lineChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);


//        LineDataSet lineDataSet_lmtt = null;
//        LineDataSet lineDataSet_lmdd = null;
//        try {
//            lineDataSet_lmtt = new LineDataSet(lineChartDataSet_lmtt(),"Lượng mưa thực tế (mm)");
//            lineDataSet_lmdd = new LineDataSet(lineChartDataSet_lmdd(),"Lượng mưa dự đoán (mm)");
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
//        iLineDataSets.add(lineDataSet_lmtt);
//        iLineDataSets.add(lineDataSet_lmdd);
//
//        LineData lineData = new LineData(iLineDataSets);
//        lineData.setValueFormatter(new MyValueFormatter());
//        lineChart.setData(lineData);
//
//        lineChart.invalidate();
//
//
//        //if you want set background color use below method
//        //lineChart.setBackgroundColor(Color.RED);
//
//        // set text if data are are not available
//        lineChart.setNoDataText("Data not Available");
//        lineChart.setDrawBorders(true);
//        lineChart.setDrawGridBackground(true);
//        lineChart.setBorderColor(Color.RED);
//
//        //you can modify your line chart graph according to your requirement there are lots of method available in this library
//
//        //now customize line chart
//
//        lineDataSet_lmtt.setColor(Color.BLUE);
//        lineDataSet_lmtt.setCircleColor(Color.GREEN);
//        lineDataSet_lmtt.setDrawCircles(true);
//        lineDataSet_lmtt.setDrawCircleHole(true);
//        lineDataSet_lmtt.setLineWidth(5);
//        lineDataSet_lmtt.setCircleRadius(10);
//        lineDataSet_lmtt.setCircleHoleRadius(10);
//        lineDataSet_lmtt.setValueTextSize(10);
//        lineDataSet_lmtt.setValueTextColor(Color.BLACK);
//
//        lineDataSet_lmdd.setColor(Color.RED);
//        lineDataSet_lmdd.setCircleColor(Color.YELLOW);
//        lineDataSet_lmdd.setDrawCircles(true);
//        lineDataSet_lmdd.setDrawCircleHole(true);
//        lineDataSet_lmdd.setLineWidth(5);
//        lineDataSet_lmdd.setCircleRadius(10);
//        lineDataSet_lmdd.setCircleHoleRadius(10);
//        lineDataSet_lmdd.setValueTextSize(10);
//        lineDataSet_lmdd.setValueTextColor(Color.BLACK);


        getLuongmua();
//        Toast.makeText(this, ""+ list_luongmua.get(0).getThoigian(), Toast.LENGTH_SHORT).show();
    }

    private void getLuongmua(){

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json_data = jsonArray.getJSONObject(i);
                        String id = json_data.getString("id");
                        String luongmuatt = json_data.getString("luongmuathucte");
                        String luongmuadd = json_data.getString("luongmuadudoan");
                        String thoigian = json_data.getString("thoigian");

                        x1.add(new Entry(Float.parseFloat(luongmuatt), i));
                        x2.add(new Entry(Float.parseFloat(luongmuadd), i));
                        String[] strDate = thoigian.split("-");
                        String yDate = strDate[2] + "/" + strDate[1];
                        y.add(yDate);

                    }
                    LineDataSet set1 = new LineDataSet(x1, "Lượng mưa thực tế (mm)");
                    set1.setColors(Collections.singletonList(Color.RED));
                    set1.setLineWidth(1.5f);
                    set1.setCircleRadius(4f);

                    LineDataSet set2 = new LineDataSet(x2, "Lượng mưa dự đoán (mm)");
                    set2.setColors(Collections.singletonList(Color.BLUE));
                    set2.setLineWidth(1.5f);
                    set2.setCircleRadius(4f);

                    List<ILineDataSet> iLineDataSets = new ArrayList<>();
                    iLineDataSets.add(set1);
                    iLineDataSets.add(set2);

                    LineData data = new LineData(y, iLineDataSets);
                    lineChart.setData(data);
                    lineChart.invalidate();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Chart.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

//    private ArrayList<Entry> lineChartDataSet_lmtt() throws ParseException {
//
//        final ArrayList<Entry> dataSet = new ArrayList<Entry>();
//
////        for (Rainfall i: list_luongmua){
////            dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse(i.getThoigian()).getTime() / 1000),i.getLuongmuathucte()));
////        }
//
//        //Float h = (float) (new SimpleDateFormat("dd-MM-yyyy").parse("10-11-2010").getTime() / 1000);
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("10-11-2010").getTime() / 1000),30));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("11-11-2010").getTime() / 1000),20));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("12-11-2010").getTime() / 1000),25));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("13-11-2010").getTime() / 1000),11));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("14-11-2010").getTime() / 1000),30));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("15-11-2010").getTime() / 1000),20));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("16-11-2010").getTime() / 1000),17));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("17-11-2010").getTime() / 1000),21));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("18-11-2010").getTime() / 1000),17));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("19-11-2010").getTime() / 1000),33));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("20-11-2010").getTime() / 1000),43));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("21-11-2010").getTime() / 1000),25));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("22-11-2010").getTime() / 1000),17));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("23-11-2010").getTime() / 1000),25));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("24-11-2010").getTime() / 1000),20));
//
//
//        return dataSet;
//
//    }
//
//    private ArrayList<Entry> lineChartDataSet_lmdd() throws ParseException {
//        final ArrayList<Entry> dataSet = new ArrayList<Entry>();
//
////        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
////            @Override
////            public void onResponse(String response) {
////                try {
////                    JSONObject jsonObject = new JSONObject(response);
////                    JSONArray jsonArray = jsonObject.getJSONArray("data");
////                    for (int i = 0; i < jsonArray.length(); i++) {
////                        JSONObject object = jsonArray.getJSONObject(i);
////                        id = object.getString("id");
////                        luongmuadd = object.getString("luongmuadudoan");
////                        thoigian = object.getString("thoigian");
////
////
////
////
////                        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse(thoigian).getTime() / 1000),Float.parseFloat(luongmuadd)));
////
////                    }
////                } catch (JSONException | ParseException e) {
////                    e.printStackTrace();
////                }
////            }
////        }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError error) {
////                Toast.makeText(Chart.this,error.toString(),Toast.LENGTH_LONG).show();
////            }
////        });
////        RequestQueue requestQueue = Volley.newRequestQueue(this);
////        requestQueue.add(request);
//
//
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("10-11-2010").getTime() / 1000),31));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("11-11-2010").getTime() / 1000),24));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("12-11-2010").getTime() / 1000),22));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("13-11-2010").getTime() / 1000),14));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("14-11-2010").getTime() / 1000),28));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("15-11-2010").getTime() / 1000),24));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("16-11-2010").getTime() / 1000),18));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("17-11-2010").getTime() / 1000),23));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("18-11-2010").getTime() / 1000),20));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("19-11-2010").getTime() / 1000),30));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("20-11-2010").getTime() / 1000),41));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("21-11-2010").getTime() / 1000),24));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("22-11-2010").getTime() / 1000),19));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("23-11-2010").getTime() / 1000),27));
//        dataSet.add(new Entry((float) (new SimpleDateFormat("dd-MM-yyyy").parse("24-11-2010").getTime() / 1000),26));
//
//        return  dataSet;
//
//
//    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        Toast.makeText(Chart.this, "hihi", Toast.LENGTH_SHORT).show();
//        RainfallInfoDialog dialog = new RainfallInfoDialog();
//        dialog.show(getSupportFragmentManager(), "Dialog nè");
    }

    @Override
    public void onNothingSelected() {

    }

//    private class MyValueFormatter extends ValueFormatter implements IValueFormatter{
//
//        @Override
//        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//            return value+ " $";
//        }
//    }


//    private class MyAxisValueFormatter extends ValueFormatter{
//
//        @Override
//        public String getAxisLabel(float value, AxisBase axis) {
//            long emissionsMilliSince1970Time = ((long) value) * 1000;
//
//            // Show time in local version
//            Date timeMilliseconds = new Date(emissionsMilliSince1970Time);
//            DateFormat dateTimeFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
//
//            return dateTimeFormat.format(timeMilliseconds);
//        }
//
//
//
//
//    }


//    private void CopyListLuongMua(List<Rainfall> list_luongmua) {
//        this.list_luongmua = list_luongmua;
//    }
}
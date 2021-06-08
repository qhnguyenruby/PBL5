package com.midterm.pbl5.Adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.midterm.pbl5.R;

import java.util.ArrayList;

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;
    private ArrayList<Entry> x1, x2;
    private ArrayList<String> y;
    public CustomMarkerView (Context context, int layoutResource, ArrayList<Entry> x1, ArrayList<Entry> x2, ArrayList<String> y) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tvContent);
        tvContent.setSingleLine(false);
        this.x1 = x1;
        this.x2 = x2;
        this.y = y;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int index = e.getXIndex();
        tvContent.setText("Ngày: "+ y.get(index) + "\n" +
                            "Lượng mưa thực tế: " + x1.get(index).getVal() + "  " +
                            "Lượng mưa dự đoán: "+ x2.get(index).getVal() );
    }

    @Override
    public int getXOffset(float xpos) {
//        return -(getWidth() / 2);
        int min_offset = 50;
        if (xpos < min_offset)
            return 0;

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        //For right hand side
        if (metrics.widthPixels - xpos < min_offset)
            return -getWidth();
            //For left hand side
        else if (metrics.widthPixels - xpos < 0)
            return -getWidth();
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        return -getHeight();
    }

}
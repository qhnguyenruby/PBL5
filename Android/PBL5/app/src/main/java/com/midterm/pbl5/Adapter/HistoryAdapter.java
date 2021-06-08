package com.midterm.pbl5.Adapter;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.midterm.pbl5.Model.History;
import com.midterm.pbl5.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    private List<History> histories;


    public HistoryAdapter(List<History> histories) {
        this.histories = histories;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryAdapter.ViewHolder holder, final int position) {
        holder.tvDate.setText(histories.get(position).getThoigian().toString());
        holder.tvLuongnuoc.setText(String.valueOf(histories.get(position).getLuongnuoc()) + "  ml");
        holder.tvDat.setText(histories.get(position).getLoaidat());
        holder.tvMode.setText(histories.get(position).getChedo());
        holder.arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.expandableView.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(holder.cardView, new AutoTransition());
                    holder.expandableView.setVisibility(View.VISIBLE);
                    holder.arrowBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    //TransitionManager.beginDelayedTransition(holder.cardView, new AutoTransition());
                    holder.expandableView.setVisibility(View.GONE);
                    holder.arrowBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final TextView tvLuongnuoc, tvDat, tvMode;
        private final Button arrowBtn;
        private final ConstraintLayout expandableView;
        private final CardView cardView;


        public ViewHolder(View view) {
            super(view);

            tvDate = view.findViewById(R.id.tv_datetime);
            tvLuongnuoc = view.findViewById(R.id.tv_luongnuoc);
            tvDat = view.findViewById(R.id.tv_dat);
            tvMode = view.findViewById(R.id.tv_mode);
            cardView = view.findViewById(R.id.cardView);
            expandableView = view.findViewById(R.id.expandableView);
            arrowBtn = view.findViewById(R.id.arrowBtn);
        }

    }
}

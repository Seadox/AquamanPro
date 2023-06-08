package com.seadox.aquamanpro.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.seadox.aquamanpro.Interface.WorkoutCallBacks;
import com.seadox.aquamanpro.Models.DrillList;
import com.seadox.aquamanpro.R;

import java.util.ArrayList;

public class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<DrillList> list;
    private WorkoutCallBacks workoutCallBacks;

    public WorkoutsAdapter(Context context, ArrayList<DrillList> list, WorkoutCallBacks workoutCallBacks) {
        this.context = context;
        this.list = list;
        this.workoutCallBacks = workoutCallBacks;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.workout_item, parent, false);

        return new WorkoutsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DrillList drillList = list.get(position);

        holder.timeline_item_MTW_distance.setText(drillList.getDistance() + " m");
        holder.timeline_item_MTW_time.setText(drillList.getTime() + "");
        holder.timeline_item_MTW_calories.setText(drillList.getCalories() + " Kcal");

        holder.timeline_item.setOnClickListener(v -> workoutCallBacks.onWorkoutClick(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private MaterialTextView timeline_item_MTW_distance;
        private MaterialTextView timeline_item_MTW_time;
        private MaterialTextView timeline_item_MTW_calories;
        private ShapeableImageView timeline_item_IMG_like;
        private RelativeLayout timeline_item;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            timeline_item = itemView.findViewById(R.id.timeline_item);
            timeline_item_MTW_distance = itemView.findViewById(R.id.timeline_item_MTW_distance);
            timeline_item_MTW_time = itemView.findViewById(R.id.timeline_item_MTW_time);
            timeline_item_MTW_calories = itemView.findViewById(R.id.timeline_item_MTW_calories);
        }
    }
}

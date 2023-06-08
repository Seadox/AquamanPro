package com.seadox.aquamanpro.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.seadox.aquamanpro.Models.Drill;
import com.seadox.aquamanpro.Models.DrillList;
import com.seadox.aquamanpro.R;
import com.seadox.aquamanpro.databinding.FragmentWorkoutBinding;

public class WorkoutFragment extends Fragment {
    private FragmentWorkoutBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWorkoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            WorkoutFragmentArgs args = WorkoutFragmentArgs.fromBundle(getArguments());
            DrillList list = args.getWorkout();

            addWorkout(list);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void addWorkout(DrillList drillList) {
        int laps = 0;

        for (Drill drill : drillList.getWarmup()) {
            addDrillLine(binding.workoutLLWarmup, drill);
            laps += Integer.parseInt(drill.getRounds());
        }

        for (Drill drill : drillList.getMain()) {
            addDrillLine(binding.workoutLLMain, drill);
            laps += Integer.parseInt(drill.getRounds());
        }

        for (Drill drill : drillList.getWarmdown()) {
            addDrillLine(binding.workoutLLWarmdown, drill);
            laps += Integer.parseInt(drill.getRounds());
        }

        setTotal(drillList.getDistance(), drillList.getTime(), laps, drillList.getCalories());
    }

    private void setTotal(int distance, String duration, int laps, int calories) {

        binding.workoutMTVDuration.setText(duration);
        binding.workoutMTVDistance.setText(distance + " m");
        binding.workoutMTVCalories.setText(calories + " Kcal");
        binding.workoutMTVLaps.setText(laps + "");
    }

    private void addDrillLine(LinearLayout layout, Drill drill) {
        View view = getLayoutInflater().inflate(R.layout.workout_data_item, null, false);

        ShapeableImageView workout_data_item_SIV_fins = view.findViewById(R.id.workout_data_item_SIV_fins);
        ShapeableImageView workout_data_item_SIV_pullBuoy = view.findViewById(R.id.workout_data_item_SIV_pullBuoy);
        ShapeableImageView workout_data_item_SIV_paddles = view.findViewById(R.id.workout_data_item_SIV_paddles);
        ShapeableImageView workout_data_item_SIV_kikboard = view.findViewById(R.id.workout_data_item_SIV_kikboard);

        ShapeableImageView workout_data_item_SIV_color = view.findViewById(R.id.workout_data_item_SIV_color);
        MaterialTextView workout_data_item_MTV_time = view.findViewById(R.id.workout_data_item_MTV_time);
        MaterialTextView workout_data_item_MTV_stroke = view.findViewById(R.id.workout_data_item_MTV_stroke);
        MaterialTextView workout_data_item_MTV_distance = view.findViewById(R.id.workout_data_item_MTV_distance);
        MaterialTextView workout_data_item_MTV_rounders = view.findViewById(R.id.workout_data_item_MTV_rounders);

        int backgroundColor;

        switch (drill.getColor()) {
            case 1:
                backgroundColor = ContextCompat.getColor(binding.getRoot().getContext(), R.color.workout_data_item_yellow);
                break;
            case 2:
                backgroundColor = ContextCompat.getColor(binding.getRoot().getContext(), R.color.workout_data_item_green);
                break;
            case 0:
            default:
                backgroundColor = ContextCompat.getColor(binding.getRoot().getContext(), R.color.workout_data_item_red);
                break;
        }
        workout_data_item_SIV_color.setBackgroundColor(backgroundColor);

        workout_data_item_MTV_time.setText(drill.getTime());
        workout_data_item_MTV_stroke.setText(drill.getStroke());
        workout_data_item_MTV_distance.setText(drill.getDistance());
        workout_data_item_MTV_rounders.setText(drill.getRounds());

        workout_data_item_SIV_fins.setVisibility(drill.isFins() ? View.VISIBLE : View.INVISIBLE);
        workout_data_item_SIV_pullBuoy.setVisibility(drill.isPullBuoy() ? View.VISIBLE : View.INVISIBLE);
        workout_data_item_SIV_paddles.setVisibility(drill.isPaddles() ? View.VISIBLE : View.INVISIBLE);
        workout_data_item_SIV_kikboard.setVisibility(drill.isKikboard() ? View.VISIBLE : View.INVISIBLE);

        layout.addView(view);
    }
}
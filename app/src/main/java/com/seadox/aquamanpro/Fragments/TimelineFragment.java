package com.seadox.aquamanpro.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.slider.RangeSlider;
import com.seadox.aquamanpro.Adapters.WorkoutsAdapter;
import com.seadox.aquamanpro.DB.DBManager;
import com.seadox.aquamanpro.Interface.DBCallBack;
import com.seadox.aquamanpro.Interface.WorkoutCallBacks;
import com.seadox.aquamanpro.Models.DrillList;
import com.seadox.aquamanpro.databinding.FragmentTimelineBinding;

import java.util.ArrayList;
import java.util.Collections;

public class TimelineFragment extends Fragment {
    private FragmentTimelineBinding binding;
    private NavController navController;
    private ArrayList<DrillList> listDrills;
    private WorkoutCallBacks workoutCallBacks = new WorkoutCallBacks() {
        @Override
        public void onWorkoutClick(int position) {
            TimelineFragmentDirections.ActionTimelineFragmentToWorkoutFragment
                    action = TimelineFragmentDirections.actionTimelineFragmentToWorkoutFragment(listDrills.get(position));

            navController.navigate(action);
        }
    };

    private DBCallBack dbCallBack = new DBCallBack() {
        @Override
        public void initAdapter(ArrayList<DrillList> list) {
            listDrills = list;
            Collections.reverse(listDrills);
            setAdapter();
        }

        @Override
        public void saveImageToUser(Uri downloadURi) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTimelineBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        init();
        DBManager.getWorkoutsByDistanceFromDB(0, 5000, dbCallBack);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void init() {
        binding.timelineRangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                float down = slider.getValues().get(0);
                float up = slider.getValues().get(1);

                DBManager.getWorkoutsByDistanceFromDB(down, up, dbCallBack);
            }
        });
    }

    private void setAdapter() {
        WorkoutsAdapter adapter = new WorkoutsAdapter(binding.getRoot().getContext(), listDrills, workoutCallBacks);
        binding.timelineRecyclerView.setAdapter(adapter);
        binding.timelineRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
    }
}
package com.seadox.aquamanpro.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.seadox.aquamanpro.DB.DBManager;
import com.seadox.aquamanpro.Models.Drill;
import com.seadox.aquamanpro.Models.DrillList;
import com.seadox.aquamanpro.R;
import com.seadox.aquamanpro.Utilities.ErrorsMsg;
import com.seadox.aquamanpro.Utilities.SignalGenerator;
import com.seadox.aquamanpro.Utilities.Utils;
import com.seadox.aquamanpro.databinding.FragmentCreateWorkoutBinding;

import java.util.ArrayList;

public class CreateWorkoutFragment extends Fragment {
    private final String TAG = "CreateWorkoutFragment";

    private FragmentCreateWorkoutBinding binding;
    private DrillList drillList = new DrillList();
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateWorkoutBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        addDrill();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void init() {
        binding.createWorkoutAddDrill.setOnClickListener(v -> {
            Utils.hideKeyboardFrom(binding.getRoot().getContext(), getActivity());
            addDrill();
        });
        binding.createMBCreate.setOnClickListener(v -> createWorkout());
    }

    private void createWorkout() {
        final String name = binding.createMBCreate.getText().toString();
        final String create = getResources().getString(R.string.create_workout_create);
        final String main = getResources().getString(R.string.create_workout_main);
        final String warmup = getResources().getString(R.string.create_workout_warmup);

        if (name.equals(create)) {
            if (createSet(drillList.getWarmdown())) {
                drillList.setUID(DBManager.getUser().getUid());
                addWorkoutToDB();
                drillList = new DrillList();
                binding.createTVTitle.setText(R.string.create_workout_warmup_title);
                binding.createMBCreate.setText(R.string.create_workout_warmup);
            }
        } else if (name.equals(main)) {
            if (createSet(drillList.getMain())) {
                binding.createTVTitle.setText(R.string.create_workout_warmdown_title);
                binding.createMBCreate.setText(R.string.create_workout_create);
            }
        } else if (name.equals(warmup)) {
            if (createSet(drillList.getWarmup())) {
                binding.createTVTitle.setText(R.string.create_workout_main_title);
                binding.createMBCreate.setText(R.string.create_workout_main);
            }
        }
        binding.createLLDrill.removeAllViews();
        addDrill();
    }

    private void addWorkoutToDB() {
        drillList.calcData();

        DBManager.addWorkout(drillList);

        showWorkout(drillList);
    }

    private boolean createSet(ArrayList<Drill> drills) {
        Utils.hideKeyboardFrom(binding.getRoot().getContext(), getActivity());

        for (int i = 0; i < binding.createLLDrill.getChildCount(); i++) {
            View view = binding.createLLDrill.getChildAt(i);
            Spinner create_item_stroke = view.findViewById(R.id.create_item_stroke);
            Spinner create_item_colors = view.findViewById(R.id.create_item_color);
            AppCompatEditText create_item_ET_laps = view.findViewById(R.id.create_item_ET_laps);
            AppCompatEditText create_item_ET_distance = view.findViewById(R.id.create_item_ET_distance);
            AppCompatEditText create_item_ET_time = view.findViewById(R.id.create_item_ET_time);

            CheckBox create_item_checkbox_fins = view.findViewById(R.id.create_item_checkbox_fins);
            CheckBox create_item_checkbox_pullBouy = view.findViewById(R.id.create_item_checkbox_pullBouy);
            CheckBox create_item_checkbox_paddles = view.findViewById(R.id.create_item_checkbox_paddles);
            CheckBox create_item_checkbox_kikboard = view.findViewById(R.id.create_item_checkbox_kikboard);

            if (!checkSet(create_item_ET_laps.getText() + "", create_item_ET_distance.getText() + "", create_item_ET_time.getText() + ""))
                return false;

            Drill drill = new Drill()
                    .setColor(create_item_colors.getSelectedItemPosition())
                    .setStroke(create_item_stroke.getSelectedItem().toString())
                    .setRounds(create_item_ET_laps.getText() + "")
                    .setDistance(create_item_ET_distance.getText() + "")
                    .setTime(create_item_ET_time.getText() + "")
                    .setFins(create_item_checkbox_fins.isChecked())
                    .setPullBuoy(create_item_checkbox_pullBouy.isChecked())
                    .setPaddles(create_item_checkbox_paddles.isChecked())
                    .setKikboard(create_item_checkbox_kikboard.isChecked());

            drills.add(drill);
        }
        return true;
    }

    private void addDrill() {
        View view = getLayoutInflater().inflate(R.layout.create_workout_item, null, false);
        Spinner create_item_stroke = view.findViewById(R.id.create_item_stroke);
        Spinner create_item_color = view.findViewById(R.id.create_item_color);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(binding.getRoot().getContext(),
                R.array.strokes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        create_item_stroke.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapterColor = ArrayAdapter.createFromResource(binding.getRoot().getContext(),
                R.array.colors_array, android.R.layout.simple_spinner_item);
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        create_item_color.setAdapter(adapterColor);

        binding.createLLDrill.addView(view);
    }

    private void showWorkout(DrillList list) {
       CreateWorkoutFragmentDirections.ActionCreateWorkoutFragmentToWorkoutFragment
                action = CreateWorkoutFragmentDirections.actionCreateWorkoutFragmentToWorkoutFragment(list);
        navController.navigate(action);
    }

    private boolean checkSet(String laps, String distance, String time) {
        if (Utils.MyChecks.checkNegativeNumber(laps)) {
            SignalGenerator.getInstance().toast(ErrorsMsg.CreateWorkout.POSITIVE_NUMBERS, Toast.LENGTH_SHORT);
            return false;
        }
        if (laps.length() < 1 || distance.length() < 1 || time.length() < 1) {
            SignalGenerator.getInstance().toast(ErrorsMsg.CreateWorkout.INVALID_INPUT, Toast.LENGTH_SHORT);
            return false;
        }
        if (!Utils.MyChecks.isInteger(laps)) {
            SignalGenerator.getInstance().toast(ErrorsMsg.CreateWorkout.LAPS_ERROR, Toast.LENGTH_SHORT);
            return false;
        }
        if (!Utils.MyChecks.checkDistance(distance)) {
            SignalGenerator.getInstance().toast(ErrorsMsg.CreateWorkout.DISTANCE_ERROR, Toast.LENGTH_SHORT);
            return false;
        }
        if (!Utils.MyChecks.checkTime(time)) {
            SignalGenerator.getInstance().toast(ErrorsMsg.CreateWorkout.TIME_FORMAT_ERROR, Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }
}
package com.seadox.aquamanpro.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.seadox.aquamanpro.Adapters.WorkoutsAdapter;
import com.seadox.aquamanpro.AuthenticationActivity;
import com.seadox.aquamanpro.DB.DBManager;
import com.seadox.aquamanpro.Interface.DBCallBack;
import com.seadox.aquamanpro.Interface.WorkoutCallBacks;
import com.seadox.aquamanpro.Models.DrillList;
import com.seadox.aquamanpro.R;
import com.seadox.aquamanpro.databinding.FragmentProfileBinding;

import java.util.ArrayList;
import java.util.Collections;

public class ProfileFragment extends Fragment {
    private String TAG = "ProfileFragment";
    private FragmentProfileBinding binding;
    private NavController navController;
    private ArrayList<DrillList> listDrills;
    private WorkoutCallBacks workoutCallBacks = new WorkoutCallBacks() {
        @Override
        public void onWorkoutClick(int position) {
            ProfileFragmentDirections.ActionProfileFragmentToWorkoutFragment
                    action = ProfileFragmentDirections.actionProfileFragmentToWorkoutFragment(listDrills.get(position));

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
            loadProfileImage(String.valueOf(downloadURi));
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        binding.profileMTWUsername.setText(DBManager.getUser().getDisplayName());
        loadProfileImage(String.valueOf(DBManager.getUser().getPhotoUrl()));

        DBManager.getWorkoutsByUidFromDB(dbCallBack);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setAdapter() {
        WorkoutsAdapter adapter = new WorkoutsAdapter(binding.getRoot().getContext(), listDrills, workoutCallBacks);
        binding.profileRecyclerView.setAdapter(adapter);
        binding.profileRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));

        int size = 0, distance = 0, calories = 0;

        if (listDrills != null) {
            size = listDrills.size();
            for (DrillList workout : listDrills) {
                distance += workout.getDistance();
                calories += workout.getCalories();
            }
        }

        binding.profileMTVWorkouts.setText(size + "");
        binding.profileMTVDistance.setText(distance + "m");
        binding.profileMTVCalories.setText(calories + " Kcal");

        binding.profileSIVProfile.setOnClickListener(v -> selectImage());
        binding.profileSIVSignout.setOnClickListener(v -> signout());
    }

    private void signout() {
        FirebaseAuth.getInstance().signOut();
        gotoLogin();
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(intent);
    }

    private final ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null)
                        DBManager.uploadImage(data.getData(), dbCallBack);
                }
            });

    private void loadProfileImage(String url) {
        Glide
                .with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.profile_image)
                .into(binding.profileSIVProfile);
    }

    private void gotoLogin() {
        Intent intent = new Intent(binding.getRoot().getContext(), AuthenticationActivity.class);
        startActivity(intent);
    }
}
package com.seadox.aquamanpro;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.seadox.aquamanpro.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
            gotoLogin();

        NavController navController = Navigation.findNavController(this, R.id.frame);

        binding.navbar.setOnItemSelectedListener(item -> {
            int id = navController.getCurrentDestination().getId();

            if (id == R.id.timelineFragment) {
                if (item.getItemId() == R.id.menu_add)
                    navController.navigate(R.id.action_timelineFragment_to_createWorkoutFragment);
                else if (item.getItemId() == R.id.menu_profile)
                    navController.navigate(R.id.action_timelineFragment_to_profileFragment);
            } else if (id == R.id.createWorkoutFragment) {
                if (item.getItemId() == R.id.menu_home)
                    navController.navigate(R.id.action_createWorkoutFragment_to_timelineFragment);
                else if (item.getItemId() == R.id.menu_profile)
                    navController.navigate(R.id.action_createWorkoutFragment_to_profileFragment);
            } else if (id == R.id.profileFragment) {
                if (item.getItemId() == R.id.menu_home)
                    navController.navigate(R.id.action_profileFragment_to_timelineFragment);
                else if (item.getItemId() == R.id.menu_add)
                    navController.navigate(R.id.action_profileFragment_to_createWorkoutFragment);
            } else if ((id == R.id.workoutFragment)) {
                if (item.getItemId() == R.id.menu_home)
                    navController.navigate(R.id.action_workoutFragment_to_timelineFragment);
                else if (item.getItemId() == R.id.menu_add)
                    navController.navigate(R.id.action_workoutFragment_to_createWorkoutFragment);
                else if (item.getItemId() == R.id.menu_profile)
                    navController.navigate(R.id.action_workoutFragment_to_profileFragment);
            }
            return true;
        });
    }

    private void gotoLogin() {
        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivity(intent);
    }
}
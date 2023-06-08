package com.seadox.aquamanpro.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.seadox.aquamanpro.Interface.MainActivityCallBacks;
import com.seadox.aquamanpro.R;
import com.seadox.aquamanpro.Utilities.SignalGenerator;
import com.seadox.aquamanpro.Utilities.signUtilities;

public class signUpFragment extends Fragment {
    private AppCompatEditText signUp_ET_name;
    private AppCompatEditText signUp_ET_username;
    private AppCompatEditText signUp_ET_password;
    private MaterialButton signUp_BTN_signUp;

    private MainActivityCallBacks mainActivityCallBacks;

    public signUpFragment(MainActivityCallBacks mainActivityCallBacks) {
        this.mainActivityCallBacks = mainActivityCallBacks;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        findViews(view);
        init();

        return view;
    }

    private void findViews(View view) {
        signUp_ET_name = view.findViewById(R.id.signUp_ET_name);
        signUp_ET_username = view.findViewById(R.id.signUp_ET_username);
        signUp_ET_password = view.findViewById(R.id.signUp_ET_password);
        signUp_BTN_signUp = view.findViewById(R.id.signUp_BTN_signUp);
    }

    private void init() {
        signUp_BTN_signUp.setOnClickListener(v -> {
            String email = signUp_ET_username.getText().toString();
            String password = signUp_ET_password.getText().toString();
            String name = signUp_ET_name.getText().toString();
            if (signUtilities.isEmailValid(email) && signUtilities.isPasswordValid(password) && signUtilities.isNameValid(name))
                mainActivityCallBacks.signUp(email, password, name);
            else {
                resetUI();
                SignalGenerator.getInstance().toast(getResources().getString(R.string.valid_input), Toast.LENGTH_SHORT);
            }
        });
    }

    private void resetUI() {
        signUp_ET_username.setText("");
        signUp_ET_password.setText("");
        signUp_ET_name.setText("");
    }
}
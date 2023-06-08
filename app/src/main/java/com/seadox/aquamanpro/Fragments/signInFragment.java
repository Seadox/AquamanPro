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

public class signInFragment extends Fragment {
    private AppCompatEditText signIn_ET_username;
    private AppCompatEditText signIn_ET_password;
    private MaterialButton signIn_BTN_signIn;
    private MaterialButton signIn_BTN_signUp;
    private MainActivityCallBacks mainActivityCallBacks;

    public signInFragment(MainActivityCallBacks mainActivityCallBacks) {
        this.mainActivityCallBacks = mainActivityCallBacks;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        findViews(view);
        init();

        return view;
    }

    private void findViews(View view) {
        signIn_ET_username = view.findViewById(R.id.signIn_ET_email);
        signIn_ET_password = view.findViewById(R.id.signIn_ET_password);
        signIn_BTN_signIn = view.findViewById(R.id.signIn_BTN_signIn);
        signIn_BTN_signUp = view.findViewById(R.id.signIn_BTN_signUp);
    }

    private void init() {
        signIn_BTN_signIn.setOnClickListener(v -> {
            String email = signIn_ET_username.getText().toString();
            String password = signIn_ET_password.getText().toString();

            if (signUtilities.isEmailValid(email) && signUtilities.isPasswordValid(password))
                mainActivityCallBacks.signIn(email, password);
            else {
                resetUI();
                SignalGenerator.getInstance().toast(getResources().getString(R.string.valid_input), Toast.LENGTH_SHORT);
            }
        });
        signIn_BTN_signUp.setOnClickListener(v -> mainActivityCallBacks.signUpClick());
    }

    private void resetUI() {
        signIn_ET_username.setText("");
        signIn_ET_password.setText("");
    }
}
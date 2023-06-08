package com.seadox.aquamanpro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.seadox.aquamanpro.DB.DBManager;
import com.seadox.aquamanpro.Fragments.signInFragment;
import com.seadox.aquamanpro.Fragments.signUpFragment;
import com.seadox.aquamanpro.Interface.AuthenticationCallBack;
import com.seadox.aquamanpro.Interface.MainActivityCallBacks;
import com.seadox.aquamanpro.Utilities.SignalGenerator;
import com.seadox.aquamanpro.Utilities.Utils;

public class AuthenticationActivity extends AppCompatActivity {

    private signInFragment signInFragment;
    private signUpFragment signUpFragment;
    MainActivityCallBacks mainActivityCallBacks = new MainActivityCallBacks() {
        @Override
        public void signIn(String email, String password) {
            hideKeyboard();
            DBManager.signInUser(getActivity(), email, password, authenticationCallBack);
        }

        @Override
        public void signUpClick() {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_FRAME_signIn, signUpFragment).commit();
        }

        @Override
        public void signUp(String email, String password, String name) {
            hideKeyboard();
            DBManager.createUser(getActivity(), email, password, name, authenticationCallBack);
        }
    };

    private AuthenticationCallBack authenticationCallBack = new AuthenticationCallBack() {
        @Override
        public void createSuccess() {
            gotoMainActivity();
        }

        @Override
        public void createFailed(String msg) {
            SignalGenerator.getInstance().toast(msg, Toast.LENGTH_SHORT);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        initFragments();
        beginTransactions();
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    private void initFragments() {
        signInFragment = new signInFragment(mainActivityCallBacks);
        signUpFragment = new signUpFragment(mainActivityCallBacks);
    }

    private void beginTransactions() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_FRAME_signIn, signInFragment).commit();
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void hideKeyboard() {
        Utils.hideKeyboardFrom(this, this);
    }

    private Activity getActivity() {
        return this;
    }
}
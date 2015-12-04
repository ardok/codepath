package com.codepath.instagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codepath.instagram.R;
import com.codepath.instagram.networking.InstagramClient;
import com.codepath.oauth.OAuthLoginActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends OAuthLoginActivity<InstagramClient> {
    // This fires once the user is authenticated, or fires immediately
    // if the user is already authenticated.

    @Bind(R.id.loginBtn) Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.loginBtn)
    public void onLoginBtnClick(Button button) {
        loginToRest(button);
    }

    @Override
    public void onLoginSuccess() {
        Intent i = new Intent(this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    // Fires if the authentication process fails for any reason.
    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }

    // Method to be called to begin the authentication process
    // assuming user is not authenticated.
    // Typically used as an event listener for a button for the user to press.
    public void loginToRest(View view) {
        getClient().connect();
    }
}

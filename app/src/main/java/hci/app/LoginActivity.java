package hci.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * LoginActivity class: Requires the user to sign in to facebook before entering the app.
 * Many code fragments have been found in facebooks API library: https://developers.facebook.com/docs/facebook-login/android
 *
 * @author Frederik Andersen
 */

public class LoginActivity extends Activity {

    private LoginButton loginButton;
    private Button loginButtonFacade;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Creating LoginActivity");
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.bt_facebook);
        loginButton.setPermissions("public_profile");
        loginButtonFacade = findViewById(R.id.bt_facebookCustom);

        callbackManager = CallbackManager.Factory.create();

        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);

        // Register facebook callback
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("Login success");
                Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(MainIntent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "An error occured while logging in", Toast.LENGTH_LONG).show();
            }
        });

        // Check if a user is already signed in, and launch the app if this is true
        try {
            Thread.sleep(1000);
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
            // If the user is logged in, redirect to the main screen
            if (isLoggedIn) {
                startActivity(mainIntent);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void onClick(View v) {
        if (v == loginButtonFacade) {
            loginButton.performClick();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

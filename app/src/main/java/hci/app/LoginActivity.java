package hci.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private LoginButton loginButton;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.fblogin_button);
        loginButton.setPermissions("public_profile");
        callbackManager = CallbackManager.Factory.create();

        Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("Login success");
                Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(MainIntent);
            }

            @Override
            public void onCancel() {
                System.out.println("Login cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("Login failed");
            }
        });

        // Check if a user is already signed in, and launch the app if this is true
        try {
            Thread.sleep(1000);
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
            if (isLoggedIn) {
                startActivity(MainIntent);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

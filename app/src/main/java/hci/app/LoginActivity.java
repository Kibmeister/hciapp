package hci.app;

import android.content.Intent;
import android.content.pm.*;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

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
                Toast.makeText(getApplicationContext(), "Login cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "An error occured while logging in", Toast.LENGTH_LONG).show();
                error.printStackTrace();

            }
        });

        // Check if a user is already signed in, and launch the app if this is true
        try {
            Thread.sleep(1000);
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
            // If the user is logged in, redirect to the main screen
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

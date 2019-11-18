package hci.app;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginManager;


/**
 * SettingsFragment: Currently only responsible for logging out the user and send him to the main screen
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }

    private Button logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        logoutButton = v.findViewById(R.id.fblogin_buttonSettings);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set onClickListener for user logout
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();

                // Send the user back to the login screen
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginIntent);

                // Logout feedback popup
                Toast.makeText(getContext(), "User logged out", Toast.LENGTH_LONG).show();
            }
        });
    }
}

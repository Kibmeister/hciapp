package hci.app;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;


/**
 * ProfileFragment class: Loads user information from facebook, and displays it.
 * <p>
 * Graph code fragments found on: https://developers.facebook.com/docs/facebook-login/android
 *
 * @author Frederik Andersen
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {


    public ProfileFragment() {
        // Required empty public constructor
    }

    private TextView textName;
    private ImageView profileImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        textName = v.findViewById(R.id.usernameTextfield);
        profileImage = v.findViewById(R.id.profileImage);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load user profile data
        loadUserData();

        // Set button onClickListeners
        Button logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        System.out.println("blablabla");
        System.out.println(R.id.logoutButton);
        System.out.println(v.getId());
        if (v.getId() == R.id.logoutButton) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Log out")
                    .setMessage("Do you want to log out?")
                    .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LoginManager.getInstance().logOut();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    // Loads user profile data from facebook graph API
    private void loadUserData() {
        String name = Profile.getCurrentProfile().getName();
        String id = Profile.getCurrentProfile().getId();
        String imageUrl;
        imageUrl = "https://graph.facebook.com/" + id + "/picture?type=large";
        textName.setText(name);
        if (profileImage != null) {
            Picasso.get().load(imageUrl).into(profileImage);
        }
    }
}

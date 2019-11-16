package hci.app;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
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
        Button settingsButton = view.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.settingsButton) {
            Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_settingsFragment);
        }
    }

    // Loads user profile data from facebook graph API
    private void loadUserData() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            // Save response as variables and render user image and user name
                            String name = object.getString("name");
                            String id = object.getString("id");
                            String imageUrl = "https://graph.facebook.com/"+id+"/picture?type=large";
                            System.out.println("id: " + id);
                            textName.setText(name);
                            Picasso.get().load(imageUrl).into(profileImage);
                        } catch (JSONException e) {
                            // Catch facebook exception
                            e.printStackTrace();
                        }
                    }
                }
        );

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name");
        request.setParameters(parameters);
        request.executeAsync();
    }
}

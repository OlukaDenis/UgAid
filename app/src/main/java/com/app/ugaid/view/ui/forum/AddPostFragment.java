package com.app.ugaid.view.ui.forum;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.app.ugaid.R;
import com.app.ugaid.model.Post;
import com.app.ugaid.model.User;
import com.app.ugaid.utils.Config;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostFragment extends Fragment {
    private TextInputEditText mName, mEmail, mDesc;
    private ForumViewModel viewModel;
    private MaterialButton btnSubmit;
    private String name, email, description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root;
        if (Config.isNetworkAvailable(getContext())) {
            root = inflater.inflate(R.layout.fragment_add_post, container, false);

            mName = root.findViewById(R.id.post_user_name);
            mEmail = root.findViewById(R.id.post_user_email);
            mDesc = root.findViewById(R.id.post_description);
            btnSubmit = root.findViewById(R.id.btn_add_post);

            ForumViewModelFactory factory = new ForumViewModelFactory(getActivity().getApplication());
            viewModel = new ViewModelProvider(this, factory).get(ForumViewModel.class);

            btnSubmit.setOnClickListener(v -> submitPost());
        } else {
            root = inflater.inflate(R.layout.fragment_no_connection, container, false);
        }

        return root;
    }

    private void submitPost() {
        name = mName.getText().toString();
        email = Objects.requireNonNull(mEmail.getText()).toString();
        description = Objects.requireNonNull(mDesc.getText()).toString();

        Date date= new Date();
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);

        if (name.isEmpty()){
            name = "Anonymous";
        }

        if(email.isEmpty()) {
            mEmail.setError("Please provide your email for notifications");
        } else if(description.isEmpty()){
            mDesc.setError("Write something to post");
        } else {
            User user = new User(name, email);
            Post post = new Post(user, description, timestamp.toString());
            viewModel.sendPost(post);

            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_forum);

            reset();
        }

    }

    private void reset() {
        mDesc.setText("");
        mName.setText("");
        mEmail.setText("");
    }
}


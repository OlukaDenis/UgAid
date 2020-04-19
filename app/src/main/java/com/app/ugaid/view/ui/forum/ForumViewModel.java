package com.app.ugaid.view.ui.forum;

import android.app.Application;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.app.ugaid.data.repository.CovidRepository;
import com.app.ugaid.model.Post;
import com.app.ugaid.utils.Config;
import com.app.ugaid.view.viewholder.ForumViewHolder;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class ForumViewModel extends ViewModel {
    private CovidRepository repository;
    private FirebaseRecyclerAdapter adapter;

    public ForumViewModel(Application application) {
        repository = new CovidRepository(application);
    }

    public void sendPost(Post post){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference formReference = database.getReference().child("posts");

        String key = Config.generateKey();

        formReference.child(key).setValue(post);
    }
}

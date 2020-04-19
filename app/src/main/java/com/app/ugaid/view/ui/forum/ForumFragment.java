package com.app.ugaid.view.ui.forum;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.app.ugaid.R;
import com.app.ugaid.model.Post;
import com.app.ugaid.utils.Config;
import com.app.ugaid.view.viewholder.ForumViewHolder;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment {
    private static final String TAG = "ForumFragment";
    private FloatingActionButton fabAddPost;
    private FirebaseAnalytics mFirebaseAnalytics;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Post, ForumViewHolder> adapter;
    private LinearLayoutManager layoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root;
        if (Config.isNetworkAvailable(getContext())) {
            root = inflater.inflate(R.layout.fragment_forum, container, false);
            mShimmerViewContainer = root.findViewById(R.id.shimmer_container);

            //Init firebase analytics
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
            mFirebaseAnalytics.setCurrentScreen(getActivity(), this.getClass().getSimpleName(),
                    this.getClass().getSimpleName());

            fabAddPost = root.findViewById(R.id.fab_add_post);
            recyclerView = root.findViewById(R.id.forum_recyclerview);

            layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            fetchAllPosts();

            fabAddPost.setOnClickListener(v -> openAddFragment());
        } else {
            root = inflater.inflate(R.layout.fragment_no_connection, container, false);
        }

        return root;

    }

    private void fetchAllPosts(){
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("posts");

        FirebaseRecyclerOptions<Post> options =
                new FirebaseRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
        Log.d(TAG, "Options: "+ options);

        adapter = new FirebaseRecyclerAdapter<Post, ForumViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ForumViewHolder holder, int position, @NonNull Post model) {
                holder.bindTO(model);
                mShimmerViewContainer.setVisibility(View.GONE);
            }

            @NonNull
            @Override
            public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post, parent, false);
                return new ForumViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void openAddFragment() {
        NavController navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_add_post);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Add a new post");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        mShimmerViewContainer.startShimmer();
    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        mShimmerViewContainer.stopShimmer();
    }

}

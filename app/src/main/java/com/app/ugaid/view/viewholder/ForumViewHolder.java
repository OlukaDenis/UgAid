package com.app.ugaid.view.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ugaid.R;
import com.app.ugaid.model.Post;
import com.app.ugaid.model.User;

public class ForumViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "ForumViewHolder";
    private TextView name, desc, timestamp;

    public ForumViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.f_user_name);
        desc = itemView.findViewById(R.id.f_description);
        timestamp = itemView.findViewById(R.id.f_time_stamp);
    }

    public void bindTO(Post post){
        User user = post.getUser();

        name.setText(user.getName());
        desc.setText(post.getDescription());
        timestamp.setText(post.getTimestamp());
    }
}

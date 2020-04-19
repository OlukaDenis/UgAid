package com.app.ugaid.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.app.ugaid.R;
//import com.app.ugaid.model.Post;
//import com.app.ugaid.view.viewholder.ForumViewHolder;
//
//import java.util.List;
//
//public class ForumAdapter extends RecyclerView.Adapter<ForumViewHolder> {
//    private List<Post> postList;
//    private Context context;
//
//    public ForumAdapter(List<Post> postList, Context context) {
//        this.postList = postList;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post, parent, false);
//        return new ForumViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
//        Post post = postList.get(position);
//        if (post != null){
//            holder.bindTO(post);
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return postList==null?0 : postList.size();
//    }
//}

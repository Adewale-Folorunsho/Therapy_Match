package com.codepath.therapymatch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.therapymatch.models.Post;
import com.parse.ParseFile;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{
    private Context context;
    private List<Post> posts;
    private String TAG = "PostAdapter";

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostsAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPostUsername;
        private TextView tvCreatedAt;
        private TextView tvDescription;
        private ImageView ivUserProfileImage;
        private ImageView ivPostImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPostUsername = itemView.findViewById(R.id.tvPostUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCreatedAt= itemView.findViewById(R.id.tvCreatedAt);

            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            ivUserProfileImage = itemView.findViewById(R.id.ivUserProfileImage);
        }

        public void bind(Post post) {
            tvDescription.setText(post.getDescription());
            tvPostUsername.setText(post.getUser().getUsername());
            tvCreatedAt.setText(post.getTime());

            ParseFile profilePicture = post.getUser().getParseFile("profileImage");
            Glide.with(context).load(profilePicture.getUrl()).into(ivUserProfileImage);

            ParseFile postImage = post.getImage();
            if (postImage != null) {
                Glide.with(context).load(postImage.getUrl()).placeholder(R.drawable.ic_baseline_cloud_download_24).into(ivPostImage);
            }
            else {
                ivPostImage.setVisibility(View.GONE);
            }

        }
    }
}

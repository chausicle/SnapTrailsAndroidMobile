package com.justinchau.snaptrailsandroidmobile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private Context mContext;
    private List<Post> mPostList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView userImage;
        public ImageView postImage;
        public TextView location;
        public TextView description;
        public TextView username;
        public TextView createdAt;

        public MyViewHolder(View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.post_userImage);
            postImage = itemView.findViewById(R.id.post_image);
            location = itemView.findViewById(R.id.location);
            description = itemView.findViewById(R.id.description);
            username = itemView.findViewById(R.id.post_username);
            createdAt = itemView.findViewById(R.id.created_at);
        }
    }

    public PostAdapter(Context context, List<Post> postList) {
        mContext = context;
        mPostList = postList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Post post = mPostList.get(position);
        DateTimeUtils.setTimeZone("PST");

        if (post != null) {
            Picasso.get()
                    .load(post.getUser().getUserImage())
                    .transform(new CircleTransform(500, 0))
                    .into(holder.userImage);
            Picasso.get()
                    .load(post.getImageUrl())
                    .into(holder.postImage);
        }
        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass = MapsFragment.class;
                Bundle bundle = new Bundle();
                bundle.putString("getOne", "getOne");
                bundle.putDouble("latitude", mPostList.get(position).getLatitude());
                bundle.putDouble("longitude", mPostList.get(position).getLongitude());
                bundle.putString("location", mPostList.get(position).getLocation());

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.location.setText(post.getLocation());
        holder.description.setText(post.getDescription());
        holder.username.setText(post.getUser().getUsername());
        holder.createdAt.setText(DateTimeUtils.getTimeAgo(mContext, post.getCreatedAt()));

    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }
}

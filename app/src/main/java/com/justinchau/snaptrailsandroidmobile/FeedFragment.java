package com.justinchau.snaptrailsandroidmobile;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FeedFragment extends Fragment {

    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private PostAdapter mAdapter;
    private List<Post> mPostList;
    private OnFragmentInteractionListener mListener;

    public FeedFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.feed_fragment, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);

        mPostList = new ArrayList<>();
        mAdapter = new PostAdapter(getContext() , mPostList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(
                1,
                dpToPx(10),
                true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        getPosts();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Make a HTTP request to web server
     */
    public void getPosts() {
//        String url = "http://10.0.2.2:8082/posts";
        String url = "https://hidden-thicket-31298.herokuapp.com/posts";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + e.getMessage());
            }


            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String postJson = null;

                        try {
                            postJson = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.i(TAG, "onResonse: " + postJson);

                        Post[] posts = new Gson().fromJson(postJson, Post[].class);

                        System.out.println(posts[0].getCreatedAt());

                        // for each post, instantiate new post and add into mPostList
                        for (Post post : posts) {

                            System.out.println("image_url: " + post.getImageUrl());
                            System.out.println("user_url: " + post.getUser().getUserImage());

                            Post newPost =
                                    new Post(
                                            post.getLocation(),
                                            post.getDescription(),
                                            post.getImageUrl(),
                                            post.getCreatedAt(),
                                            new User(
                                                    post.getUser().getUsername(),
                                                    post.getUser().getUserImage()
                                            )
                                    );
                            mPostList.add(newPost);
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private int dpToPx(int dp) {
        Resources r = getResources();

        return Math.round(
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        dp,
                        r.getDisplayMetrics())
        );

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

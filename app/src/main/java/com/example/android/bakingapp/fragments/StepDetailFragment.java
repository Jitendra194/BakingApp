package com.example.android.bakingapp.fragments;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.recipe_data.RecipeData;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

/**
 * Created by jiten on 9/9/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class StepDetailFragment extends Fragment {

    private ImageView mPrevious;
    private ImageView mNext;


    private static final String STEPS = "STEPS";
    private static final String STEPS_POSITION = "STEPS_POSITION";

    private ArrayList<RecipeData.Steps> mStepData;
    private int position;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;

    public StepDetailFragment() {
    }

    public static StepDetailFragment newInstance(ArrayList<RecipeData.Steps> steps, int stepPosition) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(STEPS, steps);
        args.putInt(STEPS_POSITION, stepPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStepData = getArguments().getParcelableArrayList(STEPS);
            position = getArguments().getInt(STEPS_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        setOrientation();
        mPlayerView = rootView.findViewById(R.id.playerView);

        TextView mStepNumber = rootView.findViewById(R.id.step_detail_title_number);
        TextView mStepFullDescription = rootView.findViewById(R.id.step_full_description);

        mPrevious = rootView.findViewById(R.id.previous_step);
        mNext = rootView.findViewById(R.id.next_step);

        String stepNumber = "Step " + position;
        String fullStepDescription = mStepData.get(position).getDescription().trim();

        mStepNumber.setText(stepNumber);
        mStepFullDescription.setText(fullStepDescription);

        initializeButtons();

        initializePlayer();
        return rootView;
    }

    @SuppressWarnings("WeakerAccess")
    public void setOrientation() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float scaleFactor = metrics.density;
        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;
        float smallestWidth = Math.min(widthDp, heightDp);
        if (smallestWidth < 600) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void initializeButtons() {
        final FragmentManager fragmentManager = getFragmentManager();
        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mStepData, position - 1);
                fragmentManager.beginTransaction()
                        .replace(R.id.step_details_fragment_container, stepDetailFragment)
                        .commit();
            }
        });
        if (position == 0) {
            mPrevious.setVisibility(View.INVISIBLE);
        }

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mStepData, position + 1);
                fragmentManager.beginTransaction()
                        .replace(R.id.step_details_fragment_container, stepDetailFragment)
                        .commit();
            }
        });
        if (position + 1 == mStepData.size()) {
            mNext.setVisibility(View.INVISIBLE);
        }
    }

    private void initializePlayer() {
        if (!(mStepData.get(position).getVideoURL().equals("")) || !(mStepData.get(position).getThumbnailURL().equals(""))) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(getContext(), "RecipeVideos");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            if (!(mStepData.get(position).getVideoURL().equals(""))) {
                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mStepData.get(position).getVideoURL()),
                        new DefaultDataSourceFactory(getContext(), userAgent), extractorsFactory, null, null);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(false);
            } else if (mStepData.get(position).getVideoURL().equals("")) {
                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mStepData.get(position).getThumbnailURL()),
                        new DefaultDataSourceFactory(getContext(), userAgent), extractorsFactory, null, null);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(false);
            }
        } else {
            mPlayerView.setVisibility(View.GONE);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}

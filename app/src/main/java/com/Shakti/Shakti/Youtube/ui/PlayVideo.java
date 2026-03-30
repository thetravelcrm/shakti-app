package com.Shakti.Shakti.Youtube.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class PlayVideo extends AppCompatActivity  implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_REQUEST = 1;
    // private YouTubePlayerView youTubeView;
    private YouTubePlayerSupportFragment youTubeView;
    private YouTubePlayer player;
    String video="";
    String Title="";

    TextView tvVideoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        // Set toolbar icon in ...
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitle("Video");
        toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolBar);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvVideoTitle=(TextView)findViewById(R.id.tv_video_title);

        video=  getIntent().getStringExtra("videoid");
        Title=  getIntent().getStringExtra("discriptiom");


     // video=  "MPPwJcCpjaA";
     //   Title= "Home.videoTitle";

        tvVideoTitle.setText(Title);
        // youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_view);
        youTubeView.initialize(ApplicationConstant.INSTANCE.YOUTUBE_API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        this.player = player;

        if (!wasRestored) {
            Log.v("video",video);
            player.cueVideo(video.replace("https://youtu.be/",""));
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(ApplicationConstant.INSTANCE.YOUTUBE_API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

}

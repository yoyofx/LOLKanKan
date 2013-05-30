package com.maxzhang.lolkankan;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
/**
 * Created with IntelliJ IDEA.
 * User: Maxzhang8
 * Date: 13-5-28
 * Time: 上午10:44
 * To change this template use File | Settings | File Templates.
 */
public class VideoPlayActivity extends Activity {

    private VideoView mVideoView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoplay);

        String playUrl = getIntent().getStringExtra("player");

        Log.v("log", "start");
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
          return;
        Log.v("log", "checked");

        mVideoView = (VideoView) findViewById(R.id.surface_view);
        //http://live.gslb.letv.com/gslb?stream_id=cctv1&tag=live&ext=m3u8&sign=live_ipad
        Uri pathUri = Uri.parse(playUrl);

        mVideoView.setVideoURI(pathUri);

        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
        mVideoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();
    }

    private int mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mVideoView != null)
            mVideoView.setVideoLayout(mLayout, 0);
        super.onConfigurationChanged(newConfig);
    }
}
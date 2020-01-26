package com.example.videoapplication;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    int SELECT_VIDEO_REQUEST=100;
    String selectedVideoPath;
    MyVideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectVideoFromGallery();
            }
        });
         videoView = (MyVideoView) findViewById(R.id.myvideoview);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void selectVideoFromGallery()
    {
        Intent intent;
        if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        }
        else
        {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI);
        }
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(intent,SELECT_VIDEO_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == SELECT_VIDEO_REQUEST && resultCode == RESULT_OK)
        {
            if(data.getData()!=null)
            {
                Uri videoUri = data.getData();

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(getApplicationContext(),videoUri);
                long duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

                retriever.release();
                videoView.setVideoSize(width,height);

                selectedVideoPath = videoUri.getPath();
                videoView.setVideoURI(data.getData());
                videoView.measure(width,height);

                videoView.start();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Failed to select video" , Toast.LENGTH_LONG).show();
            }
        }
    }

}

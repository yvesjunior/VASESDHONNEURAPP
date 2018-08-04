package activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import carousel.CarouselAdapter;
import carousel.CarouselViewPager;
import objects.MediaEntity;

public class MainActivity extends AppCompatActivity {
    private static int count_video_read = 0;
    private static VideoView videoView;
    String albumName = "VHO";
    private CarouselViewPager carousel;
    File file_IMAGE = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "VHO/IMAGES");
    File file_VHO = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), this.albumName);
    File file_VIDEO = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "VHO/VIDEOS");
    File file_background = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "VHO/background.jpg");
    File file_media = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "VHO/media.json");
    public static MediaController mediaController;
    public ArrayList<MediaEntity> mediaEntities = new ArrayList();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        getMedia();
        videoView = (VideoView) findViewById(R.id.videoView);
        if (this.mediaController == null) {
            mediaController = new MediaController(MainActivity.this);
            mediaController.setAnchorView(videoView);
            //videoView.setMediaController(mediaController);
            mediaController.setMediaPlayer(videoView);
        }
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });

        this.carousel = (CarouselViewPager) findViewById(R.id.carousel);
        ArrayList<MediaEntity> entities = buildData();

        carousel = (CarouselViewPager) findViewById(R.id.carousel);
        CarouselAdapter carouselAdapter = new CarouselAdapter(this, carousel, getSupportFragmentManager(), entities);

        carousel.setAdapter(carouselAdapter);
        carousel.addOnPageChangeListener(carouselAdapter);
        carousel.setOffscreenPageLimit(entities.size());
        carousel.setClipToPadding(false);

        carousel.setScrollDurationFactor(1.5f);
        carousel.setPageWidth(0.55f);
        carousel.settPaddingBetweenItem(16);
        carousel.setAlpha(0.0f);

        if (this.file_background.isFile()) {
            findViewById(R.id.mainContainer).setBackground(Drawable.createFromPath(this.file_background.getAbsolutePath()));
        }
    }

    public void getMedia() {
        if (this.file_VHO.isDirectory()) {
            verifyJOSNFile(this.file_media);
            return;
        }
        Toast.makeText(this, "Directory not created at :" + this.file_VHO.getAbsolutePath().toString(), Toast.LENGTH_LONG).show();
        this.file_VHO.mkdirs();
        this.file_IMAGE.mkdirs();
        this.file_VIDEO.mkdirs();
    }

    public void verifyJOSNFile(File file) {
        if (file.isFile()) {
            try {
                String str = readFile(file);
                if (isJSONValid(str)) {
                    JSONArray array = new JSONObject(str).getJSONArray("media");
                    for (int i = 0; i < array.length(); i++) {
                        String name = array.getJSONObject(i).getString("name");
                        String image = array.getJSONObject(i).getString("image");
                        String video = array.getJSONObject(i).getString("video");
                        this.mediaEntities.add(new MediaEntity(name, this.file_IMAGE.getAbsolutePath().toString() + "/" + image, this.file_VIDEO.getAbsolutePath().toString() + "/" + video, array.getJSONObject(i).getString("description")));
                    }
                    return;
                }
                Toast.makeText(this, "media.json is not well written ", Toast.LENGTH_LONG).show();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        Toast.makeText(this, "JSON file not created at :" + file.getAbsolutePath().toString(), Toast.LENGTH_LONG).show();
    }

    public boolean isJSONValid(String test) {
        try {
            JSONObject jSONObject = new JSONObject(test);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String readFile(File file) {
        String content = null;
        FileReader reader;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public ArrayList<MediaEntity> buildData() {
        return this.mediaEntities;
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            carousel.startAnimation(false, new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    carousel.setAlpha(1.0f);
                }

                @Override
                public void onAnimationEnd(Animation animation) { }

                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
        }
    }

    public static VideoView getVideoView() {
        return videoView;
    }

    public static int getCount_video_read() {
        return count_video_read;
    }

    public static void setCount_video_read() {
        count_video_read++;
    }
}

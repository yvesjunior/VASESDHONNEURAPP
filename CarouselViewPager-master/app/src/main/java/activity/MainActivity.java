package activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
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
    private MediaController mediaController;
    public ArrayList<MediaEntity> mediaEntities = new ArrayList();
    int v_height;
    int v_width;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        getMedia();
        videoView = (VideoView) findViewById(R.id.videoView);
        if (this.mediaController == null) {
            this.mediaController = new MediaController(this);
            this.mediaController.setAnchorView(videoView);
            videoView.setMediaController(this.mediaController);
        }
        this.carousel = (CarouselViewPager) findViewById(R.id.carousel);
        ArrayList<MediaEntity> entities = buildData();

        CarouselAdapter carouselAdapter = new CarouselAdapter(this, this.carousel, getSupportFragmentManager(), entities);
        this.carousel.setAdapter(carouselAdapter);
        this.carousel.addOnPageChangeListener(carouselAdapter);
        this.carousel.setOffscreenPageLimit(entities.size());
        this.carousel.setClipToPadding(false);
        this.carousel.setScrollDurationFactor(1.5d);
        this.carousel.setPageWidth(0.55f);
        this.carousel.settPaddingBetweenItem(16);
        this.carousel.setAlpha(0.0f);
        this.carousel.setAlpha(CarouselAdapter.BIG_SCALE);
        Toast.makeText(this, entities.toString(), Toast.LENGTH_LONG).show();

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
                    List<String> list = new ArrayList();
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

    public boolean checkMediaValues() {
        return true;
    }

    public boolean isJSONValid(String test) {
        try {
            JSONObject jSONObject = new JSONObject(test);
        } catch (JSONException e) {
            try {
                JSONArray jSONArray = new JSONArray(test);
            } catch (JSONException e2) {
                return false;
            }
        }
        return true;
    }

    public String readFile(File file) throws IOException {
        String line = null;

        try {

            BufferedReader bufferreader = new BufferedReader(new FileReader(file));
            line = bufferreader.readLine();

            while (line != null) {
                //do whatever here
                line = bufferreader.readLine();
            }

        } catch (FileNotFoundException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return line;
    }

    public ArrayList<MediaEntity> buildData() {
        return this.mediaEntities;
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

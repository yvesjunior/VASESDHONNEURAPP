package carousel;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import java.util.ArrayList;

import activity.R;
import objects.MediaEntity;

public class CarouselAdapter extends FragmentPagerAdapter implements OnPageChangeListener {
    public static final float BIG_SCALE = 1.0f;
    public static final float DIFF_SCALE = 0.100000024f;
    public static final float SMALL_SCALE = 0.9f;
    private CarouselViewPager carousel;
    private Context context;
    private ScaledFrameLayout cur = null;
    private ArrayList<MediaEntity> entities = new ArrayList();
    private FragmentManager fragmentManager;
    private ScaledFrameLayout next = null;
    private float scale;

    public CarouselAdapter(Context context, CarouselViewPager carousel, FragmentManager fragmentManager, ArrayList<MediaEntity> mData) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.carousel = carousel;
        this.entities = mData;
    }

    public Fragment getItem(int position){
        if (position == 0) {
            this.scale = BIG_SCALE;
        } else {
            this.scale = SMALL_SCALE;
        }
        try {
            return CarouselFragment.newInstance(this.context, (MediaEntity) this.entities.get(position), position, this.scale);
        }catch (Exception e){
            return CarouselFragment.newInstance(this.context, (MediaEntity) this.entities.get(0), 0, this.scale);
        }
    }

    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    public int getCount() {
        return this.entities.size();
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset >= 0.0f && positionOffset <= BIG_SCALE) {
            this.cur = getRootView(position);
            try {
                this.cur.setScaleBoth(BIG_SCALE - (DIFF_SCALE * positionOffset));
                if (position < this.entities.size() - 1) {
                    this.next = getRootView(position + 1);
                    this.next.setScaleBoth(SMALL_SCALE + (DIFF_SCALE * positionOffset));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void onPageSelected(int position) {
        Log.w("onPageSelected", "position : " + position);
    }

    public void onPageScrollStateChanged(int state) {
    }

    private ScaledFrameLayout getRootView(int position) {
        ScaledFrameLayout res = null;
        try {
            res= (ScaledFrameLayout) this.fragmentManager.findFragmentByTag(getFragmentTag(position)).getView().findViewById(R.id.rootItem);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return res;
        }
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + this.carousel.getId() + ":" + position;
    }
}

package carousel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import activity.MainActivity;
import activity.R;
import objects.MediaEntity;

public class CarouselFragment extends Fragment {
    public static Fragment newInstance(Context context, MediaEntity entity, int position, float scale) {
        Bundle b = new Bundle();
        b.putString("image", entity.getImage());
        b.putString("video", entity.getVideo());
        b.putInt("position", position);
        b.putFloat("scale", scale);
        b.putString("title", entity.getName());
        b.putString("description", entity.getDescription());
        return Fragment.instantiate(context, CarouselFragment.class.getName(), b);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        final ScaledFrameLayout root = (ScaledFrameLayout) inflater.inflate(R.layout.item_carousel, container, false);
        root.setScaleBoth(getArguments().getFloat("scale"));
        root.setTag("view" + getArguments().getInt("position"));
        computePadding(root);

        final VideoView videoView = (VideoView) getActivity().findViewById(R.id.videoView);
        ImageView imageView = (ImageView) root.findViewById(R.id.image);
        imageView.setImageDrawable(Drawable.createFromPath(getArguments().getString("image")));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarouselViewPager carousel = (CarouselViewPager) getActivity().findViewById(R.id.carousel);
                carousel.setCurrentItem(getArguments().getInt("position"), true);
                videoView.stopPlayback();
                videoView.setVideoPath(getArguments().getString("video"));
                videoView.start();

            }
        });

        final TextView labelView = (TextView) root.findViewById(R.id.label);
        labelView.setText(getArguments().getString("title"));

        final RelativeLayout descriptionLayout = (RelativeLayout) root.findViewById(R.id.description_layout);
        final ImageButton backButton = (ImageButton) root.findViewById(R.id.back_button);
        final TextView descriptionView = (TextView) root.findViewById(R.id.description_view);

        final ImageButton infoButton = (ImageButton) root.findViewById(R.id.info_button);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View infoView) {
                opacityAnimation(descriptionLayout, 0.0f, 1.0f, 750, true, new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        descriptionView.setText(getArguments().getString("description"));
                        backButton.setEnabled(true);
                        infoView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        backButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View backView) {
                                opacityAnimation(descriptionLayout, 1.0f, 0.0f, 750, true, new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {}

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        backView.setEnabled(false);
                                        infoView.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {}
                                });
                            }
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
            }
        });

        return root;
    }

    private void computePadding(final ViewGroup rootLayout) {
        rootLayout.post(new Runnable() {
            @Override
            public void run() {
                CarouselViewPager carousel = (CarouselViewPager) getActivity().findViewById(R.id.carousel);
                int width = rootLayout.getWidth();
                int paddingWidth = (int) (width * (1-carousel.getPageWidth())/2);
                rootLayout.setPadding(paddingWidth, 0, paddingWidth, 0);
                carousel.setPageMargin(-(paddingWidth - carousel.getPaddingBetweenItem()) * 2);
            }
        });
    }

    private void opacityAnimation(final View view, float fromAlpha, float toAlpha, int duration, boolean keepResult, Animation.AnimationListener listener){
        Animation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        if(keepResult) alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setAnimationListener(listener);
        view.startAnimation(alphaAnimation);
    }
}
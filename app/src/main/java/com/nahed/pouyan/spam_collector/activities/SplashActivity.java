package com.nahed.pouyan.spam_collector.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nahed.pouyan.spam_collector.R;
import com.nahed.pouyan.spam_collector.helpers.PermissionHelper;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imgSplash = findViewById(R.id.imgSplash);
        final TextView txtSplash = findViewById(R.id.txtSplash);

        Glide.with(this)
                .asGif()
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (resource instanceof GifDrawable) {
                            ((GifDrawable)resource).setLoopCount(1);
                        }
                        return false;
                    }
                })
                .load(Uri.parse("file:///android_asset/splash.gif"))
                .into(imgSplash);

        PropertyValuesHolder alphaAnimation = PropertyValuesHolder.ofFloat(View.ALPHA, 0, 1);
        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 100, 0);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(txtSplash, alphaAnimation, translationY);
        animator.setStartDelay(3500);
        animator.setDuration(1500);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                txtSplash.setVisibility(View.VISIBLE);
            }
        });
        animator.start();

        Handler HANDLER = new Handler();
        HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(PermissionHelper.hasPermission()){
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }else{
                    intent = new Intent(SplashActivity.this, PermissionActivity.class);
                }
                SplashActivity.this.startActivity(intent);
                finish();
            }
        }, 5500);

    }
}

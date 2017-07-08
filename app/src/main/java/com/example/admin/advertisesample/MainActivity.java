package com.example.admin.advertisesample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class MainActivity extends AppCompatActivity {

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeAdMob();
        setBannerAd();
        setInterAd();
        setRewardedVideoAd();
    }

    // synchronise app life cycle with rewarded video Ad life cycle
    @Override
    public void onResume() {
        mAd.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mAd.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mAd.destroy();
        super.onDestroy();
    }

    //initialize Mobile Ad, this is the pre requisite for all AdMobs
    private void initializeAdMob(){
        MobileAds.initialize(this, getResources().getString(R.string.app_id));
    }

    //on Button clicked show add if loaded
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
        }
        if (view.getId() == R.id.button2){
            displayRewardedVideoAd();
        }
    }

    //for banner Ad
    //URL: https://developers.google.com/admob/android/banner?hl=en-US
    private void setBannerAd(){
        mAdView = (AdView) findViewById(R.id.adView);
        //load the Ad
        mAdView.loadAd(new AdRequest.Builder().build());
    }

    //for Interstitial Ad
    // URL: https://developers.google.com/admob/android/interstitial
    //A single InterstitialAd object can be used to request and display multiple
    // interstitial ads over the course of an activity's lifespan
    private void setInterAd(){
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.inter_ad_sample_id));

        //load the Ad
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        //add listener onAdClosed load another Ad for next time button clicked
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    //for rewarder video Ad
    // URL: https://developers.google.com/admob/android/rewarded-video
    private void setRewardedVideoAd(){
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        loadRewardedVideoAd();
        mAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(MainActivity.this, "Video Loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Toast.makeText(MainActivity.this, "Video Opened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
                Toast.makeText(MainActivity.this, "Video started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Toast.makeText(MainActivity.this, "Video closed", Toast.LENGTH_SHORT).show();
                loadRewardedVideoAd();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                Toast.makeText(MainActivity.this, "Yes! 100 Points Rewarded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Toast.makeText(MainActivity.this, "left App", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Toast.makeText(MainActivity.this, "Video failed to load", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayRewardedVideoAd(){
        if (mAd.isLoaded()){
            mAd.show();
        }
    }

    private void loadRewardedVideoAd(){
        mAd.loadAd(getResources().getString(R.string.rewarded_video_ad_sample_id), new AdRequest.Builder().build());
    }
}

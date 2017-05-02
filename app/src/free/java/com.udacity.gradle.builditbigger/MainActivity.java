package com.udacity.gradle.builditbigger;

import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.udacity.gradle.joketeller.Joke;

public class MainActivity extends BaseMainActivity {

    private InterstitialAd interstitialAd;
    private Joke joke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the interstitial ad
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                launchJokeViewer(joke);
            }
        });

        requestNewInterstitial();
    }

    @Override
    protected void showJoke(Joke joke) {
        this.joke = joke;

        if(interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            launchJokeViewer(joke);
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        interstitialAd.loadAd(adRequest);
    }
}


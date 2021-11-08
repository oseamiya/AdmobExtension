package com.oseamiya.admobinterstitial;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import org.jetbrains.annotations.NotNull;

public class AdmobInterstitial extends AndroidNonvisibleComponent {
  private final Context context;
  private final Activity activity;
  private boolean testMode = false;
  private String interstitialAdUnit;
  private InterstitialAd mInterstitialAd;
  public AdmobInterstitial(ComponentContainer container) {
    super(container.$form());
    context = container.$context();
    activity = (Activity) container.$context();
  }
  @DesignerProperty(defaultValue = "False", editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN)
  @SimpleProperty
  public void TestMode(boolean isTestMode){
    testMode = isTestMode;
  }
  @SimpleProperty
  public boolean TestMode(){
    return testMode;
  }
  @DesignerProperty
  @SimpleProperty
  public void AdUnitId(String adUnitId){
    interstitialAdUnit = adUnitId;
  }

  /**
   * Events and methods from here features to load the interstitial ad
   */
  @SimpleEvent
  public void AdLoaded(){
    EventDispatcher.dispatchEvent(this , "AdLoaded");
  }
  @SimpleEvent
  public void FailedToLoad(String error){
    EventDispatcher.dispatchEvent(this , "FailedToLoad");
  }
  @SimpleEvent
  public void FailedToShowFullScreenContent(String error){
    EventDispatcher.dispatchEvent(this , "FailedToShowFullScreenContent", error);
  }
  @SimpleEvent
  public void ShowedFullScreenContent(){
    EventDispatcher.dispatchEvent(this , "ShowedFullScreenContent");
  }
  @SimpleEvent
  public void DismissedFullScreenContent(){
    EventDispatcher.dispatchEvent(this, "DismissedFullScreenContent");
  }
  @SimpleEvent
  public void AdImpression(){
    EventDispatcher.dispatchEvent(this , "AdImpression");
  }
  @SimpleEvent
  public void AdClicked(){
    EventDispatcher.dispatchEvent(this , "AdClicked");
  }

  @SimpleFunction
  public void LoadInterstitialAd(){
    AdRequest adRequest = new AdRequest.Builder().build();
    String unitId = testMode ? "ca-app-pub-3940256099942544/1033173712" : interstitialAdUnit;
    InterstitialAd.load(context, unitId, adRequest, new InterstitialAdLoadCallback() {
      @Override
      public void onAdLoaded(@NonNull @NotNull InterstitialAd interstitialAd) {
        mInterstitialAd = interstitialAd;
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
          @Override
          public void onAdFailedToShowFullScreenContent(@NonNull @NotNull AdError adError) {
            FailedToShowFullScreenContent(adError.getMessage());
          }

          @Override
          public void onAdShowedFullScreenContent() {
            ShowedFullScreenContent();
          }

          @Override
          public void onAdDismissedFullScreenContent() {
            DismissedFullScreenContent();
          }

          @Override
          public void onAdImpression() {
            AdImpression();
          }

          @Override
          public void onAdClicked() {
            AdClicked();
          }
        });
        AdLoaded();
      }

      @Override
      public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
        FailedToLoad(loadAdError.getMessage());
      }
    });
  }

  /**
   * Events and methods from here features to show the interstitial ad
   * Call method `ShowAd` on AdLoaded event.
   */
  @SimpleEvent
  public void FailedToShowAd(){
    EventDispatcher.dispatchEvent(this, "FailedToShowAd");
  }
  @SimpleFunction
  public void ShowAd(){
    if(mInterstitialAd != null){
      mInterstitialAd.show(activity);
    }else{
      FailedToShowAd();
    }
  }


}

package com.oseamiya.admobappopen;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import org.jetbrains.annotations.NotNull;

public class AdmobAppOpen extends AndroidNonvisibleComponent {
  private final Context context;
  private boolean testMode = false;
  private String appOpenAdUnitId;
  private AppOpenAd appOpenAd;
  private final String TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294";
  public AdmobAppOpen(ComponentContainer container) {
    super(container.$form());
    context = container.$context();
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
  @SimpleProperty
  public void TestMode(boolean isTestMode){
    testMode = isTestMode;
  }
  @SimpleProperty
  public boolean TestMode(){
    return testMode;
  }
  @SimpleProperty
  public void AdUnitId(String adUnitId){
    appOpenAdUnitId = adUnitId;
  }
  @SimpleEvent
  public void AdLoaded(){
    EventDispatcher.dispatchEvent(this, "AdLoaded");
  }
  @SimpleEvent
  public void AdFailedToLoad(String error){
    EventDispatcher.dispatchEvent(this, "AdFailedToLoad", error);
  }
  @SimpleEvent
  public void AdFailedToShowFullScreenContent(String error){
    EventDispatcher.dispatchEvent(this, "AdFailedToShowFullScreenContent", error);
  }
  @SimpleEvent
  public void AdShowedFullScreenContent(){
    EventDispatcher.dispatchEvent(this, "AdShowedFullScreenContent");
  }
  @SimpleEvent
  public void AdDismissedFullScreenContent(){
    EventDispatcher.dispatchEvent(this, "AdDismissedFullScreenContent");
  }
  @SimpleEvent
  public void AdImpression(){
    EventDispatcher.dispatchEvent(this, "AdImpression");
  }
  @SimpleEvent
  public void AdClicked(){
    EventDispatcher.dispatchEvent(this, "AdClicked");
  }
  @SimpleFunction
  public void LoadAd(int orientation){
    String adId = testMode? TEST_AD_UNIT_ID : appOpenAdUnitId;
    if(adId != null) {
      AppOpenAd.load(context, adId, new AdRequest.Builder().build(), orientation, new AppOpenAd.AppOpenAdLoadCallback() {
        @Override
        public void onAdLoaded(@NonNull @NotNull AppOpenAd ad) {
          appOpenAd = ad;
          appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull @NotNull AdError adError) {
              AdFailedToShowFullScreenContent(adError.getMessage());
            }

            @Override
            public void onAdShowedFullScreenContent() {
              AdShowedFullScreenContent();
            }

            @Override
            public void onAdDismissedFullScreenContent() {
              AdDismissedFullScreenContent();
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
          AdFailedToLoad(loadAdError.getMessage());
        }
      });
    }else{
      throw new YailRuntimeError("No AdUnitId is provided", "RuntimeError");
    }
  }
  @SimpleEvent
  public void FailedToShowAd(){
    EventDispatcher.dispatchEvent(this, "FailedToShowAd");
  }
  @SimpleFunction
  public void ShowAd(){
    if(appOpenAd != null){
      appOpenAd.show((Activity) context);
    }else{
      FailedToShowAd();
    }
  }
  @SimpleProperty
  public int Landscape(){
    return AppOpenAd.APP_OPEN_AD_ORIENTATION_LANDSCAPE;
  }
  @SimpleProperty
  public int Portrait(){
    return AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT;
  }

}

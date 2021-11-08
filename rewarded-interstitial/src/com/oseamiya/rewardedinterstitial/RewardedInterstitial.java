package com.oseamiya.rewardedinterstitial;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import org.jetbrains.annotations.NotNull;

public class RewardedInterstitial extends AndroidNonvisibleComponent {
  private final String TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/5354046379";
  private final Context context;
  private final Activity activity;
  private boolean testMode = false;
  private String rewardedInterstitialAdUnitId;
  private RewardedInterstitialAd mRewardedInterstitialAd;
  public RewardedInterstitial(ComponentContainer container) {
    super(container.$form());
    context = container.$context();
    activity = (Activity) container.$context();
  }
  @DesignerProperty(defaultValue = "False" , editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN)
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
    rewardedInterstitialAdUnitId = adUnitId;
  }
  @SimpleEvent
  public void AdLoaded(){
    EventDispatcher.dispatchEvent(this, "AdLoaded");
  }
  @SimpleEvent
  public void AdFailedToLoad(){
    EventDispatcher.dispatchEvent(this, "AdFailedToLoad");
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
  public void LoadAd(){
    String adUnitId = testMode ? TEST_AD_UNIT_ID : rewardedInterstitialAdUnitId;
    RewardedInterstitialAd.load(context, TEST_AD_UNIT_ID, new AdRequest.Builder().build(), new RewardedInterstitialAdLoadCallback() {
      @Override
      public void onAdLoaded(@NonNull @NotNull RewardedInterstitialAd rewardedInterstitialAd) {
        mRewardedInterstitialAd = rewardedInterstitialAd;
        mRewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
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
        AdFailedToLoad();
      }
    });
  }
  @SimpleEvent
  public void GotRewardItem(int rewardAmount, String rewardType){
    EventDispatcher.dispatchEvent(this, "GotRewardItem", rewardAmount, rewardType);
  }
  @SimpleEvent
  public void FailedToShowAd(){
    EventDispatcher.dispatchEvent(this, "FailedToShowAd");
  }
  @SimpleFunction
  public void ShowAd() {
    if (mRewardedInterstitialAd != null) {
      mRewardedInterstitialAd.show(activity, new OnUserEarnedRewardListener() {
        @Override
        public void onUserEarnedReward(@NonNull @NotNull RewardItem rewardItem) {
          GotRewardItem(rewardItem.getAmount(), rewardItem.getType() == null ? "" : rewardItem.getType());
        }
      });
    }else{
      FailedToShowAd();
    }
  }

}

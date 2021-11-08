package com.oseamiya.admobrewarded;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import org.jetbrains.annotations.NotNull;

public class AdmobRewarded extends AndroidNonvisibleComponent {
  private final Context context;
  private final Activity activity;
  private boolean testMode = false;
  private String rewardedAdUnitId;
  private RewardedAd mRewardedAd;
  public AdmobRewarded(ComponentContainer container) {
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
    rewardedAdUnitId = adUnitId;
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
  public void LoadAd(){
    AdRequest adRequest = new AdRequest.Builder().build();
    String unitId = testMode ? "ca-app-pub-3940256099942544/5224354917" : rewardedAdUnitId;
    RewardedAd.load(context, rewardedAdUnitId, adRequest, new RewardedAdLoadCallback() {
      @Override
      public void onAdLoaded(@NonNull @NotNull RewardedAd rewardedAd) {
        mRewardedAd = rewardedAd;
        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
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
  }
  /**
   * The events and methods from here are dedicated to show the rewarded ad
   */
  @SimpleEvent
  public void GotRewardItem(int rewardAmount, String rewardType){
    EventDispatcher.dispatchEvent(this, "GotRewardItem", rewardAmount, rewardType);
  }
  @SimpleEvent
  public void FailedToShowAd(){
    EventDispatcher.dispatchEvent(this, "FailedToShowAd");
  }
   @SimpleFunction
  public void ShowAd(){
     if(mRewardedAd != null){
       mRewardedAd.show(activity, new OnUserEarnedRewardListener() {
         @Override
         public void onUserEarnedReward(@NonNull @NotNull RewardItem rewardItem) {
           GotRewardItem(rewardItem.getAmount() , rewardItem.getType()==null?"":rewardItem.getType());
         }
       });
     }else{
       FailedToShowAd();
     }
   }
}

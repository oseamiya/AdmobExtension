package com.oseamiya.admobbanner;

import android.content.Context;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.*;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import org.jetbrains.annotations.NotNull;

public class AdmobBanner extends AndroidNonvisibleComponent {
  private final Context context;
  private String bannerAdUnit;
  private AdView bannerAdView;
  private boolean testMode = false;
  public AdmobBanner(ComponentContainer container) {
    super(container.$form());
    context = container.$context();
  }
  @DesignerProperty()
  @SimpleProperty
  public void AdUnitId(String adUnit){
    bannerAdUnit = adUnit;
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
  @SimpleFunction
  public void ShowBanner(AndroidViewComponent in){
    ViewGroup viewGroup = (ViewGroup) in.getView();
    if(bannerAdView.getParent() != null){
      ((ViewGroup) bannerAdView.getParent()).removeView(bannerAdView);
    }
    viewGroup.addView(bannerAdView);
  }
  @SimpleEvent
  public void AdLoaded(){
    EventDispatcher.dispatchEvent(this , "AdLoaded");
  }
  @SimpleEvent
  public void AdClosed(){
    EventDispatcher.dispatchEvent(this , "AdClosed");
  }
  @SimpleEvent
  public void AdFailedToLoad(String error){
    EventDispatcher.dispatchEvent(this  ,"AdFailedToLoad", error);
  }
  @SimpleEvent
  public void AdOpened(){
    EventDispatcher.dispatchEvent(this , "AdOpened");
  }
  @SimpleEvent
  public void AdOnImpression(){
    EventDispatcher.dispatchEvent(this , "AdOnImpression");
  }
  @SimpleEvent
  public void AdClicked(){
    EventDispatcher.dispatchEvent(this , "AdClicked");
  }
  @SimpleFunction
  public void LoadBanner(){
    bannerAdView = new AdView(context);
    bannerAdView.setAdSize(AdSize.BANNER);
    if(testMode){
      bannerAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
    }else{
      bannerAdView.setAdUnitId(bannerAdUnit);
    }
    AdRequest adRequest = new AdRequest.Builder().build();
    bannerAdView.loadAd(adRequest);
    bannerAdView.setAdListener(new AdListener() {
      @Override
      public void onAdClosed() {
        AdClosed();
      }

      @Override
      public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
        AdFailedToLoad(loadAdError.toString());
      }

      @Override
      public void onAdOpened() {
        AdOpened();
      }

      @Override
      public void onAdLoaded() {
        AdLoaded();
      }

      @Override
      public void onAdClicked() {
        AdClicked();
      }

      @Override
      public void onAdImpression() {
        AdOnImpression();
      }
    });
  }
}

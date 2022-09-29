package com.oseamiya.admobbanner;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.google.ads.mediation.admob.AdMobAdapter;
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

    private final Activity activity;
    private String bannerAdUnit;
    private AdView bannerAdView;
    private boolean testMode = false;
    private boolean isNonPersonalizedAds = false;

    private boolean isRdpSignal = false;

    public AdmobBanner(ComponentContainer container) {
        super(container.$form());
        context = container.$context();
        activity = (Activity) container.$context();
    }

    @DesignerProperty()
    @SimpleProperty
    public void AdUnitId(String adUnit) {
        bannerAdUnit = adUnit;
    }

    @DesignerProperty(defaultValue = "False", editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN)
    @SimpleProperty
    public void TestMode(boolean isTestMode) {
        testMode = isTestMode;
    }

    @SimpleProperty
    public boolean TestMode() {
        return testMode;
    }

    @SimpleFunction
    public void ShowBanner(AndroidViewComponent in) {
        ViewGroup viewGroup = (ViewGroup) in.getView();
        if (bannerAdView.getParent() != null) {
            ((ViewGroup) bannerAdView.getParent()).removeView(bannerAdView);
        }
        viewGroup.addView(bannerAdView);
    }

    @SimpleEvent
    public void AdLoaded() {
        EventDispatcher.dispatchEvent(this, "AdLoaded");
    }

    @SimpleEvent
    public void AdClosed() {
        EventDispatcher.dispatchEvent(this, "AdClosed");
    }

    @SimpleEvent
    public void AdFailedToLoad(String error) {
        EventDispatcher.dispatchEvent(this, "AdFailedToLoad", error);
    }

    @SimpleEvent
    public void AdOpened() {
        EventDispatcher.dispatchEvent(this, "AdOpened");
    }

    @SimpleEvent
    public void AdOnImpression() {
        EventDispatcher.dispatchEvent(this, "AdOnImpression");
    }

    @SimpleEvent
    public void AdClicked() {
        EventDispatcher.dispatchEvent(this, "AdClicked");
    }

    @SimpleProperty
    public void IsNonPersonalizedAds(boolean value) {
        isNonPersonalizedAds = value;
    }

    @SimpleProperty
    public void IsRDPSignal(boolean value) {
        isRdpSignal = value;
        if (value) {
            SharedPreferences sharedPref = this.activity.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("gad_rdp", 1);
            editor.commit();
        }
    }

    @SimpleFunction
    public void LoadBanner(Object adSize) {
        if (adSize instanceof AdSize) {
            bannerAdView = new AdView(context);
            bannerAdView.setAdSize((AdSize) adSize);
            String adId = testMode ? "ca-app-pub-3940256099942544/6300978111" : bannerAdUnit;
            bannerAdView.setAdUnitId(adId);
            AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
            if (isNonPersonalizedAds) {
                Bundle extras = new Bundle();
                extras.putString("npa", "1");
                adRequestBuilder
                        .addNetworkExtrasBundle(AdMobAdapter.class, extras);
            }
            if (isRdpSignal) {
                Bundle networkExtrasBundle = new Bundle();
                networkExtrasBundle.putInt("rdp", 1);
                adRequestBuilder
                        .addNetworkExtrasBundle(AdMobAdapter.class, networkExtrasBundle);
            }
            bannerAdView.loadAd(adRequestBuilder.build());
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
        } else {
            AdFailedToLoad("This AdSize is not found");
        }
    }

    @SimpleProperty
    public Object BannerSize() {
        return AdSize.BANNER;
    }

    @SimpleProperty
    public Object LargeBannerSize() {
        return AdSize.LARGE_BANNER;
    }

    @SimpleProperty
    public Object MediumRectangleSize() {
        return AdSize.MEDIUM_RECTANGLE;
    }

    @SimpleProperty
    public Object FullSizeBannerSize() {
        return AdSize.FULL_BANNER;
    }

    @Deprecated
    @SimpleProperty
    public Object SmartSize() {
        return AdSize.SMART_BANNER;
    }

    @SimpleFunction
    public Object CurrentOrientationInlineAdaptiveSize(int width) {
        return AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(this.context, width);
    }

    @SimpleFunction
    public Object PortraitInlineAdaptiveSize(int width) {
        return AdSize.getPortraitInlineAdaptiveBannerAdSize(this.context, width);
    }

    @SimpleFunction
    public Object LandscapeInlineAdaptiveSize(int width) {
        return AdSize.getLandscapeInlineAdaptiveBannerAdSize(this.context, width);
    }

    @SimpleFunction
    public Object InlineAdaptiveBannerAdSize(int width, int maxHeight) {
        return AdSize.getInlineAdaptiveBannerAdSize(width, maxHeight);
    }

    @SimpleFunction
    public Object CurrentOrientationAnchoredAdaptiveSize(int width) {
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this.context, width);
    }

    @SimpleFunction
    public Object LandscapeAnchoredAdaptiveSize(int width) {
        return AdSize.getLandscapeAnchoredAdaptiveBannerAdSize(this.context, width);
    }

    @SimpleFunction
    public Object PortraitAnchoredAdaptiveSize(int width) {
        return AdSize.getPortraitAnchoredAdaptiveBannerAdSize(this.context, width);
    }

    @SimpleFunction
    public Object LandscapeInterScrollerSize(int width) {
        return AdSize.getLandscapeInterscrollerAdSize(this.context, width);
    }

    @SimpleFunction
    public Object PortraitInterScrollerAdSize(int width) {
        return AdSize.getPortraitInterscrollerAdSize(this.context, width);
    }

    @SimpleFunction
    public Object CurrentOrientationInterScrollerSize(int width) {
        return AdSize.getCurrentOrientationInterscrollerAdSize(this.context, width);
    }

    @SimpleFunction
    public Object CustomBannerSize(int width, int height) {
        return new AdSize(width, height);
    }
}

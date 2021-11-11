package com.oseamiya.admobsdks;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import org.jetbrains.annotations.NotNull;

public class AdmobSdks extends AndroidNonvisibleComponent {
    private final Context context;

    public AdmobSdks(ComponentContainer container) {
        super(container.$form());
        context = container.$context();
    }

    @SimpleEvent
    public void SdkInitialized() {
        EventDispatcher.dispatchEvent(this, "SdkInitialized");
    }

    @SimpleFunction
    public void InitializeSdk() {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull @NotNull InitializationStatus initializationStatus) {
                SdkInitialized();
            }
        });
    }

    /**
     * Tag for child directed treatment can either be 0, 1 or -1. You can find more in developers.google.com/admob/android/targeting
     * RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE == 1;
     * RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_FALSE == 0;
     * RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_UNSPECIFIED == -1;
     */
    @SimpleProperty
    public void TagForChildDirectedTreatment(int value) {
        if (value == 0 || value == 1 || value == -1) {
            RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setTagForChildDirectedTreatment(value)
                    .build();
            MobileAds.setRequestConfiguration(requestConfiguration);
        }else {
            throw new YailRuntimeError("Value for TagForChildDirectedTreatment can either be 1,0 or -1. Read Documentation Carefully", "RuntimeError");
        }
    }

    /**
     * RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE == 1;
     * RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_FALSE == 0;
     * RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_UNSPECIFIED == -1;
     */
    @SimpleProperty
    public void TagForUnderAgeOfConsent(int value){
        if (value == 0 || value == 1 || value == -1) {
            RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setTagForUnderAgeOfConsent(value)
                    .build();
            MobileAds.setRequestConfiguration(requestConfiguration);
        }else{
            throw new YailRuntimeError("Value for TagForUnderAgeOfConsent can either be 1,0 or -1. Read Documentation Carefully", "RuntimeError");
        }
    }

    /**
     * value can either be G, MA, PG, or T
     * Content that are suitable for general audiences, including families can be "G";
     * Content that are suitable for only matured audiences can be "MA";
     * Content that are suitable for most audiences with parental guidance can be "PG";
     * Content that are suitable for teen and older audiences can be "T"
     */
    @SimpleProperty
    public void MaxAdContentRating(String value) {
        String upperCaseValue = value.toUpperCase();
        if (upperCaseValue.equals("G") || upperCaseValue.equals("MA") || upperCaseValue.equals("PG") || upperCaseValue.equals("T")) {
            RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setMaxAdContentRating(upperCaseValue)
                    .build();
	    MobileAds.setRequestConfiguration(requestConfiguration);
        }else{
            throw new YailRuntimeError("Value for MaxAdContentRating can either be G,MA,PG or T. Read Documentation Carefully", "RuntimeError");
        }
    }
}



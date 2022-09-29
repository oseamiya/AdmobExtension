package com.oseamiya.admobsdklite;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.ads.AdInspectorError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnAdInspectorClosedListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.ump.*;
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

public class AdmobSdkLite extends AndroidNonvisibleComponent {
  private final Context context;
  private final Activity activity;
  private ConsentInformation consentInformation;
  private ConsentForm consentForm;

  private String debugGeography = "DISABLED";

  public AdmobSdkLite(ComponentContainer container) {
    super(container.$form());
    context = container.$context();
    activity = (Activity) container.$context();
  }

  @DesignerProperty(defaultValue = "DISABLED",
          editorArgs = {"DISABLED", "EEA", "NOT EEA"},
          editorType = PropertyTypeConstants.PROPERTY_TYPE_CHOICES
  )
  @SimpleProperty
  public void DebugGeography(String value) {
    debugGeography = value;
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
    } else {
      throw new YailRuntimeError("Value for TagForChildDirectedTreatment can either be 1,0 or -1. Read Documentation Carefully", "RuntimeError");
    }
  }

  /**
   * RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE == 1;
   * RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_FALSE == 0;
   * RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_UNSPECIFIED == -1;
   */
  @SimpleProperty
  public void TagForUnderAgeOfConsent(int value) {
    if (value == 0 || value == 1 || value == -1) {
      RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
              .toBuilder()
              .setTagForUnderAgeOfConsent(value)
              .build();
      MobileAds.setRequestConfiguration(requestConfiguration);
    } else {
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
    } else {
      throw new YailRuntimeError("Value for MaxAdContentRating can either be G,MA,PG or T. Read Documentation Carefully", "RuntimeError");
    }
  }

  @SimpleEvent
  public void ConsentInfoUpdateSuccess(boolean isConsentFormAvailable) {
    EventDispatcher.dispatchEvent(this, "ConsentInfoUpdateSuccess", isConsentFormAvailable);
  }

  @SimpleEvent
  public void ConsentInfoUpdateFailure(String error) {
    EventDispatcher.dispatchEvent(this, "ConsentInfoUpdateFailure", error);
  }

  @SimpleFunction
  public void RequestConsentInformation() {
    int debugInteger = ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_DISABLED;
    switch (debugGeography) {
      case "DISABLED":
        debugInteger = ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_DISABLED;
        break;
      case "EEA":
        debugInteger = ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA;
        break;
      case "NOT EEA":
        debugInteger = ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_NOT_EEA;
        break;
    }
    ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(this.context)
            .setDebugGeography(debugInteger)
            .build();

    ConsentRequestParameters params = new ConsentRequestParameters
            .Builder()
            .setConsentDebugSettings(debugSettings)
            .setTagForUnderAgeOfConsent(false)
            .build();

    consentInformation = UserMessagingPlatform.getConsentInformation(this.context);
    consentInformation.requestConsentInfoUpdate(
            this.activity,
            params,
            new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
              @Override
              public void onConsentInfoUpdateSuccess() {
                ConsentInfoUpdateSuccess(consentInformation.isConsentFormAvailable());
              }
            },
            new ConsentInformation.OnConsentInfoUpdateFailureListener() {
              @Override
              public void onConsentInfoUpdateFailure(FormError formError) {
                ConsentInfoUpdateFailure(formError.getMessage());
              }
            });
  }

  /**
   * Consent status can be
   * NOT_REQUIRED = 1
   * OBTAINED = 3
   * REQUIRED = 2
   * UNKNOWN = 0
   * You may show the form when consent status is REQUIRED or UNKNOWN ie, 2 and 0
   */
  @SimpleEvent
  public void ConsentFormLoadSuccess(int status) {
    EventDispatcher.dispatchEvent(this, "ConsentFormLoadSuccess", status);
  }

  @SimpleEvent
  public void ConsentFormLoadFailure(String error) {
    EventDispatcher.dispatchEvent(this, "ConsentFormLoadFailure", error);
  }

  @SimpleFunction
  public void LoadForm() {
    UserMessagingPlatform.loadConsentForm(this.context, new UserMessagingPlatform.OnConsentFormLoadSuccessListener() {
      @Override
      public void onConsentFormLoadSuccess(ConsentForm cForm) {
        consentForm = cForm;
        ConsentFormLoadSuccess(consentInformation.getConsentStatus());
      }
    }, new UserMessagingPlatform.OnConsentFormLoadFailureListener() {
      @Override
      public void onConsentFormLoadFailure(FormError formError) {
        ConsentFormLoadFailure(formError.getMessage());
      }
    });
  }

  @SimpleEvent
  public void ConsentFormDismissed(String error) {
    EventDispatcher.dispatchEvent(this, "ConsentFormDismissed", error);
  }

  @SimpleFunction
  public void ShowForm() {
    consentForm.show(this.activity, new ConsentForm.OnConsentFormDismissedListener() {
      @Override
      public void onConsentFormDismissed(FormError formError) {
        ConsentFormDismissed(formError.getMessage());
        // if user dismissed the consent form then you might call the loadForm again.
      }
    });
  }

  @SimpleFunction
  public void ResetConsentState() {
    if (consentInformation != null) {
      consentInformation.reset();
    }
  }

  @SimpleEvent
  public void AdInspectorClosed(String error) {
    EventDispatcher.dispatchEvent(this, "AdInspectorClosed", error);
  }

  @SimpleFunction
  public void OpenAdInspector() {
    MobileAds.openAdInspector(context, new OnAdInspectorClosedListener() {
      public void onAdInspectorClosed(@Nullable AdInspectorError error) {
        AdInspectorClosed(error == null ? "" : error.getMessage());
      }
    });
  }
}
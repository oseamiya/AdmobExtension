package com.oseamiya.admobsdks;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import org.jetbrains.annotations.NotNull;

public class AdmobSdks extends AndroidNonvisibleComponent {
  private final Context context;
  public AdmobSdks(ComponentContainer container) {
    super(container.$form());
    context = container.$context();
  }
  @SimpleEvent
  public void SdkInitialized(){
      EventDispatcher.dispatchEvent(this , "SdkInitialized");
  }
  @SimpleFunction
  public void InitializeSdk(){
      MobileAds.initialize(context, new OnInitializationCompleteListener() {
          @Override
          public void onInitializationComplete(@NonNull @NotNull InitializationStatus initializationStatus) {
              SdkInitialized();
          }
      });
  }
}

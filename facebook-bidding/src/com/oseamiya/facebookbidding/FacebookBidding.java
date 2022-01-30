package com.oseamiya.facebookbidding;

import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

import android.content.Context;

public class FacebookBidding extends AndroidNonvisibleComponent {
  private final Context context;
  public FacebookBidding(ComponentContainer container) {
    super(container.$form());
    context = container.$context();
  }

  
  
}

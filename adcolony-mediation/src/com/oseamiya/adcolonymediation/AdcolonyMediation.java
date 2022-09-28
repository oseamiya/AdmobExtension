package com.oseamiya.adcolonymediation;

import com.adcolony.sdk.AdColonyAppOptions;
import com.google.ads.mediation.adcolony.AdColonyMediationAdapter;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;


public class AdcolonyMediation extends AndroidNonvisibleComponent {

    public AdcolonyMediation(ComponentContainer container) {
        super(container.$form());
    }

    @DesignerProperty(defaultValue = "false", editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN)
    @SimpleProperty
    public void GDPAConsent(boolean required) {
        AdColonyAppOptions appOptions = AdColonyMediationAdapter.getAppOptions();
        appOptions.setPrivacyFrameworkRequired(AdColonyAppOptions.GDPR, required);
        appOptions.setPrivacyConsentString(AdColonyAppOptions.GDPR, "1");
    }

    @DesignerProperty(defaultValue = "false", editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN)
    @SimpleProperty
    public void CCPAConsent(boolean required) {
        AdColonyAppOptions appOptions = AdColonyMediationAdapter.getAppOptions();
        appOptions.setPrivacyFrameworkRequired(AdColonyAppOptions.CCPA, required);
        appOptions.setPrivacyConsentString(AdColonyAppOptions.CCPA, "1");
    }
}

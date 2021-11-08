# Add any ProGuard configurations specific to this
# extension here.

-keep public class com.oseamiya.rewardedinterstitial.RewardedInterstitial {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'com/oseamiya/rewardedinterstitial/repack'
-flattenpackagehierarchy
-dontpreverify

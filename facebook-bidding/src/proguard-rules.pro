# Add any ProGuard configurations specific to this
# extension here.

-keep public class com.oseamiya.facebookbidding.FacebookBidding {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'com/oseamiya/facebookbidding/repack'
-flattenpackagehierarchy
-dontpreverify

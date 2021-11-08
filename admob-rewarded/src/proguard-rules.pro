# Add any ProGuard configurations specific to this
# extension here.

-keep public class com.oseamiya.admobrewarded.AdmobRewarded {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'com/oseamiya/admobrewarded/repack'
-flattenpackagehierarchy
-dontpreverify

# Add any ProGuard configurations specific to this
# extension here.

-keep public class com.oseamiya.admobbanner.AdmobBanner {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'com/oseamiya/admobbanner/repack'
-flattenpackagehierarchy
-dontpreverify

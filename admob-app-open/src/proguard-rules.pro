# Add any ProGuard configurations specific to this
# extension here.

-keep public class com.oseamiya.admobappopen.AdmobAppOpen {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'com/oseamiya/admobappopen/repack'
-flattenpackagehierarchy
-dontpreverify

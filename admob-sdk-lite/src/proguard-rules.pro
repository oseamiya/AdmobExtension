# Add any ProGuard configurations specific to this
# extension here.

-keep public class com.oseamiya.admobsdklite.AdmobSdkLite {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'com/oseamiya/admobsdklite/repack'
-flattenpackagehierarchy
-dontpreverify

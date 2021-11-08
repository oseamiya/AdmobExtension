# Add any ProGuard configurations specific to this
# extension here.

-keep public class com.oseamiya.admobsdks.AdmobSdks {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'com/oseamiya/admobsdks/repack'
-flattenpackagehierarchy
-dontpreverify

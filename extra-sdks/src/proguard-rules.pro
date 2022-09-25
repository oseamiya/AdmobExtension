# Add any ProGuard configurations specific to this
# extension here.

-keep public class com.oseamiya.extrasdks.ExtraSdks {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'com/oseamiya/extrasdks/repack'
-flattenpackagehierarchy
-dontpreverify

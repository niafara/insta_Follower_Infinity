# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in J:\mohammad\Life\Program\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

##Tapsell Rules
#-keepattributes Signature
#-keepattributes *Annotation*
#-keepattributes EnclosingMethod
#-keep class sun.misc.Unsafe { *; }
#-keep interface ir.tapsell.tapsellvideosdk.NoProguard
#-keep class * implements ir.tapsell.tapsellvideosdk.NoProguard { *; }
#-keep interface * extends ir.tapsell.tapsellvideosdk.NoProguard { *; }
###
#
###Magnet
#-keep class ir.magnet.sdk.** { *; }
#-keepattributes Signature
#-keepattributes *Annotation*
#####
#
##adPlay
#-keep class ir.adPlay.plugin.noProguard
#-keep class * extends ir.adPlay.plugin.noProguard { *; }
##adPlay
#
##tapstream
#-keep class com.google.android.gms.ads.identifier.** { *; }
#
##Secure-preferences
#-keep class com.tozny.crypto.android.AesCbcWithIntegrity$PrngFixes$* { *; }
#
##-dontwarn butterknife.internal.**
##-dontwarn com.github.siyamed.shapeimageview.**
###-dontwarn com.squareup.picasso.OkHttpDownloader.**
#-dontwarn com.squareup.picasso.OkHttpDownloader
#-dontwarn okio.Okio
#-dontwarn butterknife.internal.ButterKnifeProcessor
#-dontwarn com.github.siyamed.shapeimageview
#-dontwarn com.github.siyamed.shapeimageview.path.SvgToPath
#-dontwarn okio.DeflatorSink
#
#
#-keep class com.github.siyamed.shapeimageview.*{ *; }
#-keep class butterknife.**{ *; }
#-keep class com.squareup.picasso.OkHttpDownloader.**{ *; }
#-dontwarn javax.management.**
#-dontwarn java.lang.management.**
#-dontwarn org.apache.log4j.**
#-dontwarn org.apache.commons.logging.**
#-dontwarn org.slf4j.**
#-dontwarn org.json.*
-keep class javax.** { *; }
-keep class org.** { *; }
-keep class Lorg.** { *; }
#-keep class twitter4j.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}
-dontwarn com.github.siyamed.shapeimageview.**
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
#-keep class org.kxml2.io.KXmlParser.** { *; }
#
#-keep interface org.kxml2.io.KXmlParser.** { *; }
#-keep class com.github.siyamed.shapeimageview.** { *; }
#
#-keep interface com.github.siyamed.shapeimageview.** { *; }



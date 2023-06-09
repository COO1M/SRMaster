# XXPermission
-keep class com.hjq.permissions.** {*;}

# Pytorch
-keep class org.pytorch.** {*;}
-keep class com.facebook.** {*;}

# UM
-keep class com.umeng.** {*;}
-keep class org.repackage.** {*;}
-keepclassmembers class * {
    public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class com.master.sr.R$*{
    public static final int *;
}
# R8 / ProGuard logger removal comparison

It appears that R8 does not remove logging code the same way that ProGuard does when the `assumenosideeffects` rule is used.


## proguard-rules.pro

```
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
    public static *** w(...);
}
-assumenosideeffects class * implements org.slf4j.Logger {
    public *** trace(...);
    public *** debug(...);
    public *** info(...);
    public *** warn(...);
}
```


## Decompiled class obfuscated by ProGuard

![Decompiled class obfuscated by ProGuard](decompiled-proguard.png)


## Decompiled class obfuscated by R8

![Decompiled class obfuscated by R8](decompiled-r8.png)

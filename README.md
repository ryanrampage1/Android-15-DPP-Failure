## Sample app of DPP Failures on Phones running Android 15 and 16. 

This repository contains a small sample app and APK to reproduce Easy Connect intents immediatly returning as cancelled without any user interaction. 

This bug was trtacked and fix in the AOSP. The initial bug report can be found here: [ACTION_PROCESS_WIFI_EASY_CONNECT_URI Always Cancelled](https://issuetracker.google.com/issues/355482872/)

## Background

The main piece of code to reproduce the failure launching the `android.settings.PROCESS_WIFI_EASY_CONNECT_URI` with any URI as shown below. 

The result of the launched intent is returned immediatly as Cancelled. 

``` Kotlin
val intent = Intent(android.settings.PROCESS_WIFI_EASY_CONNECT_URI)
intent.data = Uri.parse(uri)
startActivityForResult(intent, 1111)
```

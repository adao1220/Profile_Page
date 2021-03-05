# What you need to run it

## Android Studio
https://developer.android.com/studio
### Download Android Virtual Device (AVD) Manager 
Choose pixel 4XL
### System Image
Choose Lollipop (API level 22)
### Android Virtual Device (AVD)
You can leave the settings the way they are here

## Things you need to add
### AndroidManifest.xml
We need to give permission to access the external storage<br/> 
Before `<application>` tab insert the following code: <br/>
    `<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>`


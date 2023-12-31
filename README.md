<div align="center">
  <h1>Sample App for Depth Estimation</h1>
</div>

Description
----------

This project is just a sample app for integrating the [depth estimation library](https://github.com/asfaksyd/midas_depth_sdk) using camera based app.
App use camera to preview from phone and regulary feed the image data to library so in response of the library will get depth map which is going to display in the UI.

prerequisites
-------------

- JAVA/JDK users need to have installed on their system before setting up this app project in your system.
- Please install latest supported [Android Studio](https://developer.android.com/studio).
- Please also download android SDK platform 34 and latest gradle version (8.2 or later) using Android Studio after installation of Android Studio.
- If you want to test this sample app with your phone in debug mode then kindly enable [Developer Options](https://developer.android.com/studio/debug/dev-options) in your phone.

Installation
------------

1. Clone the repository into your : 
````comandline
https://github.com/asfaksyd/DepthEstimationSample.git
````
2. Open the project in Android Studio.
4. Build the project using Android Studio.

Apk
--
If you want to try the saple app before setting up this project in your enviornment 
1. Please [download](https://github.com/asfaksyd/DepthEstimationSample/blob/master/APK/sample.apk) APK file in your Android phone.
2. Install apk into your phone. (If its ask for any Unknow source permmission whule installig then please this [instructions](https://www.applivery.com/docs/mobile-app-distribution/android-unknown-sources/) to enabkle it first)
3. Launch the app it will start capturing live streaming using the camera as well as start displaying depth image below of it.
4. There is on screen option to switch the camera as well.

Sample App Screenshot
---------------------
![depth image sample](https://github.com/asfaksyd/midas_depth_sdk/blob/master/repo_images/midas_depth_sample_image.jpeg)
<h2>Enjoy!</h2>


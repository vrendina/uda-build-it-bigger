# Build It Bigger

### Overview

The purpose of this project is to demonstrate advanced gradle usage by creating external Java and Android
libraries that are automatically incorporated into an Android application build. A Google Cloud Endpoints
 (GCE) module will also be created to serve data to the application. Gradle will be utilized to run
 instrumentation and unit tests to automatically verify the application is functioning as expected.

### Development Tasks

> **Required** Your first task is to create a Java library that provides jokes.

+ Java library `JokeTeller` was added to the project.
+ The `JokeTeller` library incorporates Google's `AutoValue` for creating the serializable value class `Joke`. 
+ `JokeTeller` incorporates `JUnit4` and has unit tests to verify proper functionality.

> **Required** Create an Android Library containing an Activity that will display a joke
passed to it as an intent extra. 

+ Android module `JokeViewer` was added to the project.
+ The `JokeViewerActivity` receives a serialized `Joke` object as an intent extra field. 
+ Instrumentation tests with Espresso were created to test the functionality of the `JokeViewerActivity`
in isolation from the rest of the application.

> **Required** Instead of pulling jokes directly from
our Java library, we'll set up a Google Cloud Endpoints development server,
and pull our jokes from there. 

+ Added `backend` module to project that has a `JokeEndPoint`.
+ The backend service depends on the java `JokeTeller` library to obtain jokes.

> **Required** Add connected tests to verify the jokes are being retrieved from the Google Cloud Enpoints server.

+ Espresso tests added to verify that a joke is obtained and displayed from the GCE backend.

> **Required** Add free and paid product flavors to your app. Remove the ad (and any
dependencies you can) from the paid flavor.

+ Created free and full product flavors. 
+ Added line `freeCompile 'com.google.firebase:firebase-ads:10.2.1'` to `build.gradle` so the ad library
is only compiled into the free flavor.

> **Optional** Add an interstitial ad to the free version.

+ Created an abstract `BaseMainActivity` class and then implemented `MainActivity` differently for each
build flavor.

> **Optional** Add a loading indicator that is shown while the joke is being retrieved and
disappears when the joke is ready. 

+ Created a `ProgressBar` that is displayed when the `AsyncTaskLoader` is retrieving a joke.
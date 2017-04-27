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

+ TODO

> **Required** Add connected tests to verify the jokes are being retrieved from the Google Cloud Enpoints server.

+ TODO

> **Required** Add free and paid product flavors to your app. Remove the ad (and any
dependencies you can) from the paid flavor.

+ TODO

> **Optional** Add an interstitial ad to the free version.

+ TODO

> **Optional** Add a loading indicator that is shown while the joke is being retrieved and
disappears when the joke is ready. 

+ Created a `ProgressBar` that is displayed when the `AsyncTaskLoader` is retrieving a joke.

> **Optional** To tie it all together, create a Gradle task that: 1. Launches the GCE local development server, 2. Runs all tests, 3. Shuts the server down again

+ TODO
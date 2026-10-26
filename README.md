# FitTrack Android

FitTrack Android is the mobile client for the FitTrack fitness application. It provides Google and Supabase email/password sign-in and opens a welcome screen after successful authentication. The app communicates with the FitTrack Spring Boot API for Google sign-in and user profile data.

## Links

- Backend repository: <https://github.com/MaldonadoJack/CST438-Project2-API>
- Live API: <https://fittrack-api-noemhi.onrender.com/>
- Swagger UI: <https://fittrack-api-noemhi.onrender.com/swagger-ui.html>
- Latest Android release: <https://github.com/MaldonadoJack/cst438-Project2/releases/latest>

## Technology

- Kotlin
- Jetpack Compose
- Android Credential Manager and Google Identity
- Retrofit and Gson
- Supabase Auth REST API
- Gradle

## Requirements

- Android Studio with JDK 21
- Android SDK Platform 37.0
- An emulator or device running Android 14/API 34 or newer
- A Google OAuth project configured for this application's package and signing certificate
- A Supabase project with email/password authentication enabled
- The FitTrack backend, running locally for Google sign-in requests

The Android package name is:

```text
com.example.cst438_project2
```

## Clone and open the project

```bash
git clone https://github.com/MaldonadoJack/cst438-Project2.git
cd cst438-Project2
```

Open the repository folder in Android Studio and allow Gradle to finish syncing. If Android Studio asks to install SDK Platform 37.0, approve the installation.

## Local configuration

Add the following values to your user-level Gradle properties file at `~/.gradle/gradle.properties`:

```properties
GOOGLE_WEB_CLIENT_ID=your-web-client-id.apps.googleusercontent.com
SUPABASE_URL=https://your-project.supabase.co/
SUPABASE_PUBLISHABLE_KEY=your-supabase-publishable-key
```

Use the Google **web client ID**, not a client secret. The Android OAuth client in the same Google project must use this app's package name and the SHA-1 fingerprint of the certificate that signs the app.

Do not commit real configuration values, passwords, OAuth client secrets, database credentials, `local.properties`, or a private signing keystore.

## Choose the backend

### Use the deployed Render API

Using Render is the simplest way to try the app because it does not require a second terminal or a locally running backend.

Open `app/src/main/java/com/example/cst438_project2/auth/FitTrackApiClient.kt` and set the Retrofit base URL to:

```kotlin
private const val LOCAL_EMULATOR_URL = "https://fittrack-api-noemhi.onrender.com/"
```

Keep the final `/` because Retrofit requires it. Sync or rebuild the project after changing the URL. The first request can take longer if the Render service was sleeping.

### Use a local backend

To develop the Android app and backend together, use this emulator address instead:

```kotlin
private const val LOCAL_EMULATOR_URL = "http://10.0.2.2:8080/"
```

`10.0.2.2` is the Android emulator's address for port `8080` on your computer. Start the backend locally before using Google sign-in. Follow the backend repository's README to configure its database and OAuth environment variables.

If you use a physical Android device with a local backend, replace the emulator address with an address on your network that the device can reach.

## Run the app

1. Configure `FitTrackApiClient.kt` to use the deployed Render API, or start the FitTrack backend locally.
2. Open this project in Android Studio.
3. Start an API 34-or-newer emulator or connect an Android device.
4. Select the `app` run configuration.
5. Select **Run**.
6. Sign in with a Google account registered as a test user in the Google OAuth project, or use a valid Supabase email/password account.

After a successful sign-in, the app opens the welcome screen.

## Test the app

Run the local JVM unit tests:

```bash
./gradlew testDebugUnitTest
```

With an emulator or device running, run the Android and Compose UI tests:

```bash
./gradlew connectedDebugAndroidTest
```

The UI tests cover the login fields, Google and email sign-in buttons, error messages, and the welcome screen.

## Build the debug APK

```bash
./gradlew assembleDebug
```

The APK is created at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

GitHub Actions runs the local unit tests, builds the debug APK, and saves it as a workflow artifact on pushes and pull requests to `master`. Published APKs are available from the repository's [Releases](https://github.com/MaldonadoJack/cst438-Project2/releases) page.

## Project structure

```text
app/src/main/java/com/example/cst438_project2/
  MainActivity.kt              Application entry point and navigation
  LoginScreen.kt               Google and email/password login UI
  HomeScreen.kt                Welcome screen shown after login
  auth/                        Google, Supabase, and backend networking code
  ui/theme/                    Compose colors, typography, and theme

app/src/test/                  Local JVM unit tests
app/src/androidTest/           Instrumented and Compose UI tests
.github/workflows/             Android CI configuration
```

## Troubleshooting

- **Google OAuth is not configured:** add `GOOGLE_WEB_CLIENT_ID` to `~/.gradle/gradle.properties` and sync Gradle.
- **Google sign-in does not list an account:** verify the OAuth test-user list, package name, and SHA-1 fingerprint.
- **Supabase login says it is not configured:** check `SUPABASE_URL` and `SUPABASE_PUBLISHABLE_KEY`.
- **Failed to connect to `10.0.2.2:8080`:** start the backend locally and confirm it is listening on port `8080`.
- **The first Render request is slow:** wait briefly and try again because the deployed service may be waking up.
- **The Render API returns 401:** sign in again and confirm that the Google web client ID matches the backend configuration.
- **No target device found:** start an emulator from Android Studio's Device Manager or connect a device with USB debugging enabled.
- **Gradle cannot find API 37:** install Android SDK Platform 37.0 from SDK Manager.


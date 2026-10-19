# Kme — Android keyboard (IME)

This is a GitHub/MGit-ready Android project for the Kme keyboard.

## What was corrected
- Kept the Android InputMethodService registration explicit in `AndroidManifest.xml`.
- Added a small launcher/setup screen so Kme appears in the app list and provides direct buttons to Android's keyboard settings and keyboard picker.
- Kept the keyboard service, Persian subtype metadata, supplied keyboard image, and Send action.
- Kept the GitHub Actions workflow building with Gradle 8.9 and Android SDK 35; it does not depend on a missing Gradle wrapper or the obsolete `sdkmanager tools` package.

## Build with GitHub Actions
1. Extract this ZIP and use the contents of the `Kme` folder as the repository root.
2. Push to `main` or `master`, or run **Android Build** manually from Actions.
3. Download the `Kme-debug-apk` artifact from the successful workflow.
4. Install `app-debug.apk` on the phone.

## Enable the keyboard
1. Open the **Kme** app.
2. Tap **Enable Kme in settings** and enable **Kme Keyboard**.
3. Return to Kme and tap **Choose active keyboard** to select it.

Note: the Send key invokes the editor action offered by the focused app/site and falls back to Enter. A website's private Send button cannot be forced by a general Android keyboard unless the site supports an appropriate editor action or handles Enter. The supplied keyboard image is still a visual reference; individual image key regions are not mapped to typing yet.

# Kme Android Keyboard

Kme is an Android Input Method Editor (IME) project. The keyboard view is built from touchable controls matching the keys in `app/src/main/assets/keyboard_layout.jpg`; the image is retained as the layout reference and is not displayed as a fake, non-interactive keyboard.

## Changes in this revision
- Removed the extra full-width `Send` button and explanatory strip previously placed beneath the keyboard.
- No controls are added beyond the keys represented in the supplied keyboard layout.
- Pressing a key lights that same key in a sun-yellow color briefly.
- Persian letters, digits/punctuation, space, delete, enter, navigation arrows, clipboard actions, select, undo/redo, emoji, diacritics, File picker, and Send have handlers.
- `Send` requests the focused editor's declared IME action; if the editor does not provide a usable action, it sends Enter. Websites that require tapping a separate HTML button may not expose that button to an Android keyboard service, so the keyboard cannot universally click a site's page button.

## Build
GitHub Actions workflow: `.github/workflows/android.yml`

The workflow builds `:app:assembleDebug` and uploads `app-debug.apk` as the `Kme-debug-apk` artifact.

## Install and enable
Install the APK, open Kme once if needed, then enable **Kme Keyboard** under Android Settings > System/Language & input > On-screen keyboard (the exact menu wording varies by device), and select it from the keyboard picker.

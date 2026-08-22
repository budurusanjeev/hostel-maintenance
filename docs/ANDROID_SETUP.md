# Android App Setup

This repository includes a **native Android app** in the `android/` folder (Kotlin + Jetpack Compose + Room).

The web app in the project root is optional. Use the Android app on your phone or emulator.

## Requirements

- [Android Studio](https://developer.android.com/studio) (latest stable, e.g. Ladybug or newer)
- JDK 17 (bundled with Android Studio)
- Android SDK 35

## Open the project on Windows

1. Install Android Studio if you have not already.
2. Open Android Studio → **File → Open**.
3. Select this folder:

   `C:\Users\Sanjeev Buduru\Documents\GitHub\hostel-maintenance\android`

4. Wait for Gradle sync to finish (first time may take several minutes).
5. Connect an Android phone with USB debugging enabled **or** start an emulator (**Device Manager → Create Device**).
6. Click the green **Run** button (or **Run → Run 'app'**).

The app installs as **Hostel Maintenance** on your device.

## Pull latest code first

```powershell
cd "C:\Users\Sanjeev Buduru\Documents\GitHub\hostel-maintenance"
git pull origin main
```

Then reopen or sync the `android` project in Android Studio.

## Build APK from command line (optional)

```powershell
cd "C:\Users\Sanjeev Buduru\Documents\GitHub\hostel-maintenance\android"
.\gradlew.bat assembleDebug
```

APK output:

`android\app\build\outputs\apk\debug\app-debug.apk`

Copy this file to your phone and install it (enable “Install unknown apps” if needed).

## App features

| Tab | Feature |
| --- | --- |
| **Maintenance** | Log repair requests by room |
| **Students** | Register student details (room, course, parent, fee) |
| **Fees** | View overdue/upcoming fee reminders; mark fee paid |

Data is stored **locally on the device** using Room (SQLite). No internet required after install.

## Project structure

```text
android/
├── app/src/main/java/com/hostel/maintenance/
│   ├── MainActivity.kt          # App entry + bottom navigation
│   ├── data/                    # Room database, DAOs, repository
│   ├── model/                   # Fee reminder models
│   ├── ui/screens/              # Compose screens
│   └── viewmodel/               # ViewModel
├── app/build.gradle.kts
└── settings.gradle.kts
```

## Troubleshooting

| Issue | Fix |
| --- | --- |
| Gradle sync failed | **File → Invalidate Caches → Restart** |
| SDK not found | **Tools → SDK Manager** → install Android 14/15 SDK |
| `gradlew` missing | Open project in Android Studio once; it creates the wrapper |
| App won't install on phone | Enable Developer options + USB debugging |

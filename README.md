# Hostel Maintenance

**Native Android app** for hostel maintenance, student registration, and fee date reminders.

> The web app in the project root is a bonus demo. **Use the Android app** (`android/`) on your phone.

## Android app (recommended)

See **[docs/ANDROID_SETUP.md](docs/ANDROID_SETUP.md)** for full instructions.

### Quick start (Windows)

1. Pull the repo:

```powershell
cd "C:\Users\Sanjeev Buduru\Documents\GitHub\hostel-maintenance"
git pull origin main
```

2. Open **`android`** folder in Android Studio.
3. Click **Run** to install on your phone or emulator.

## Features

| Feature | Android tab |
| --- | --- |
| Maintenance requests | Maintenance |
| Student details entry | Students |
| Fee date reminders | Fees |

## Repository layout

```text
hostel-maintenance/
├── android/          ← Native Android app (Kotlin, Jetpack Compose)
├── docs/             ← Setup guides
├── src/              ← Optional web app (Next.js)
└── scripts/          ← Setup scripts for web
```

## Web app (optional)

If you want to run the web version locally:

```bash
npm install
npm run dev
```

Open http://localhost:3000

# Folder Structure

This document describes the project layout so you can clone the repository and run it on your machine.

## Repository layout

```text
hostel-maintenance/
├── .cursor/
│   └── environment.json       # Cloud Agent environment config
├── docs/
│   ├── FOLDER_STRUCTURE.md    # This file
│   └── LOCAL_SETUP.md         # Step-by-step local setup guide
├── public/                    # Static assets served by Next.js
├── scripts/
│   ├── cloud-agent-install.sh # Cloud Agent install script
│   ├── setup.sh               # Local setup for macOS/Linux
│   └── setup.ps1              # Local setup for Windows
├── src/
│   ├── app/                   # Next.js App Router pages and API routes
│   │   ├── api/
│   │   │   └── requests/      # REST API for maintenance requests
│   │   ├── globals.css        # Global styles
│   │   ├── layout.tsx         # Root layout
│   │   └── page.tsx           # Home page
│   ├── components/            # Reusable React UI components
│   │   ├── MaintenanceForm.tsx
│   │   ├── RequestCard.tsx
│   │   └── RequestList.tsx
│   ├── lib/                   # Shared application logic
│   │   ├── api.ts             # Browser API client helpers
│   │   ├── constants.ts       # Labels and display constants
│   │   ├── store.ts           # In-memory request store (server)
│   │   └── types.ts           # TypeScript types
│   └── styles/
│       └── shared.ts          # Shared style objects
├── .env.example               # Example environment variables
├── .gitignore
├── .nvmrc                     # Recommended Node.js version
├── eslint.config.mjs
├── next.config.ts
├── package.json
├── package-lock.json
├── README.md
└── tsconfig.json
```

## What each area does

| Path | Purpose |
| --- | --- |
| `src/app/api/requests` | Server API for listing, creating, and updating requests |
| `src/components` | UI building blocks used by the home page |
| `src/lib` | Types, constants, API helpers, and server-side store |
| `scripts/setup.sh` / `setup.ps1` | One-command local dependency setup |
| `public` | Static files such as images or icons |

## Quick start on your machine

```bash
git clone https://github.com/budurusanjeev/hostel-maintenance.git
cd hostel-maintenance
./scripts/setup.sh    # macOS/Linux
npm run dev
```

On Windows PowerShell:

```powershell
git clone https://github.com/budurusanjeev/hostel-maintenance.git
cd hostel-maintenance
.\scripts\setup.ps1
npm run dev
```

Open [http://localhost:3000](http://localhost:3000).

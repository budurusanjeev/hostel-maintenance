# Hostel Management

A web app for hostel maintenance, student registration, and fee date reminders.

## Quick start (local machine)

```bash
git clone https://github.com/budurusanjeev/hostel-maintenance.git
cd hostel-maintenance
./scripts/setup.sh   # macOS/Linux
npm run dev
```

Windows PowerShell:

```powershell
git clone https://github.com/budurusanjeev/hostel-maintenance.git
cd hostel-maintenance
.\scripts\setup.ps1
npm run dev
```

Open [http://localhost:3000](http://localhost:3000).

## Requirements

- Node.js 20+ (see `.nvmrc`)
- npm

## Project structure

```text
hostel-maintenance/
├── docs/           # Setup guides and folder structure
├── public/         # Static assets
├── scripts/        # Setup scripts (local + Cloud Agent)
└── src/
    ├── app/        # Next.js pages and API routes
    ├── components/ # React UI components
    ├── lib/        # Types, API helpers, server store
    └── styles/     # Shared styles
```

See [docs/FOLDER_STRUCTURE.md](docs/FOLDER_STRUCTURE.md) for the full layout and [docs/LOCAL_SETUP.md](docs/LOCAL_SETUP.md) for detailed setup steps.

## Scripts

| Command | Description |
| --- | --- |
| `npm run setup` | Install dependencies for local development |
| `npm run dev` | Start the development server |
| `npm run build` | Create a production build |
| `npm run start` | Run the production server |
| `npm run lint` | Run ESLint |
| `npm run typecheck` | Run TypeScript checks |

## Features

- **Maintenance**: Create and track repair requests by room
- **Student details**: Register students with contact, course, parent info, and fee details
- **Fee reminders**: View overdue and upcoming fee due dates; mark fees as paid

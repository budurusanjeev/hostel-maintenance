# Local Setup Guide

Follow these steps to run **Hostel Maintenance** on your own computer.

## Requirements

- **Node.js 20+** (22 recommended — see `.nvmrc`)
- **npm** (included with Node.js)
- **Git**

## 1. Clone the repository

```bash
git clone https://github.com/budurusanjeev/hostel-maintenance.git
cd hostel-maintenance
```

## 2. Install dependencies

### macOS / Linux

```bash
chmod +x scripts/setup.sh
./scripts/setup.sh
```

### Windows (PowerShell)

```powershell
.\scripts\setup.ps1
```

### Manual install

```bash
npm install
cp .env.example .env   # optional
```

## 3. Start the development server

```bash
npm run dev
```

The app runs at [http://localhost:3000](http://localhost:3000).

## 4. Verify it works

1. Open the app in your browser.
2. Submit a maintenance request (room, issue, priority).
3. Confirm the request appears in the list.
4. Change its status using the dropdown.

## Available commands

| Command | Description |
| --- | --- |
| `npm run setup` | Install dependencies (same as `scripts/setup.sh`) |
| `npm run dev` | Start development server |
| `npm run build` | Create production build |
| `npm run start` | Run production server |
| `npm run lint` | Run ESLint |
| `npm run typecheck` | Run TypeScript checks |

## Production build

```bash
npm run build
npm run start
```

## Troubleshooting

| Issue | Fix |
| --- | --- |
| `node: command not found` | Install Node.js 20+ from [nodejs.org](https://nodejs.org/) |
| Port 3000 in use | Set `PORT=3001` in `.env` and restart |
| Dependency errors | Delete `node_modules` and run `npm install` again |

For the full folder layout, see [FOLDER_STRUCTURE.md](./FOLDER_STRUCTURE.md).

#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

echo "==> Hostel Maintenance local setup"

if ! command -v node >/dev/null 2>&1; then
  echo "Error: Node.js is required. Install Node.js 20+ from https://nodejs.org/"
  exit 1
fi

NODE_VERSION="$(node -p "process.versions.node")"
echo "Node.js version: ${NODE_VERSION}"

if [[ -f package-lock.json ]]; then
  echo "==> Installing dependencies with npm ci"
  npm ci
else
  echo "==> Installing dependencies with npm install"
  npm install
fi

if [[ ! -f .env && -f .env.example ]]; then
  echo "==> Creating .env from .env.example"
  cp .env.example .env
fi

echo "==> Setup complete"
echo
echo "Run the app:"
echo "  npm run dev"
echo
echo "Then open http://localhost:3000"

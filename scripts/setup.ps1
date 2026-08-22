$ErrorActionPreference = "Stop"

Set-Location (Join-Path $PSScriptRoot "..")

Write-Host "==> Hostel Maintenance local setup"

if (-not (Get-Command node -ErrorAction SilentlyContinue)) {
  Write-Error "Node.js is required. Install Node.js 20+ from https://nodejs.org/"
}

$nodeVersion = node -p "process.versions.node"
Write-Host "Node.js version: $nodeVersion"

if (Test-Path "package-lock.json") {
  Write-Host "==> Installing dependencies with npm ci"
  npm ci
} else {
  Write-Host "==> Installing dependencies with npm install"
  npm install
}

if (-not (Test-Path ".env") -and (Test-Path ".env.example")) {
  Write-Host "==> Creating .env from .env.example"
  Copy-Item ".env.example" ".env"
}

Write-Host "==> Setup complete"
Write-Host ""
Write-Host "Run the app:"
Write-Host "  npm run dev"
Write-Host ""
Write-Host "Then open http://localhost:3000"

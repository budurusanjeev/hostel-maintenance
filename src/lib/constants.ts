import type { MaintenanceRequest } from "./types";

export const priorityLabels: Record<MaintenanceRequest["priority"], string> = {
  low: "Low",
  medium: "Medium",
  high: "High",
};

export const statusLabels: Record<MaintenanceRequest["status"], string> = {
  open: "Open",
  in_progress: "In Progress",
  resolved: "Resolved",
};

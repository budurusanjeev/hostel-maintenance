import type { FeeReminderStatus, MaintenanceRequest } from "./types";

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

export const feeReminderLabels: Record<FeeReminderStatus, string> = {
  overdue: "Overdue",
  due_soon: "Due Soon",
  upcoming: "Upcoming",
  paid: "Paid",
};

export const feeReminderColors: Record<FeeReminderStatus, string> = {
  overdue: "#b42318",
  due_soon: "#b54708",
  upcoming: "#2563eb",
  paid: "#027a48",
};

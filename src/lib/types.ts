export type MaintenanceRequest = {
  id: string;
  room: string;
  issue: string;
  priority: "low" | "medium" | "high";
  status: "open" | "in_progress" | "resolved";
  createdAt: string;
};

import type { MaintenanceRequest } from "./types";

const API_BASE = "/api/requests";

export async function fetchRequests(): Promise<MaintenanceRequest[]> {
  const response = await fetch(API_BASE);
  const data = (await response.json()) as { requests: MaintenanceRequest[] };
  return data.requests;
}

export async function createMaintenanceRequest(input: {
  room: string;
  issue: string;
  priority: MaintenanceRequest["priority"];
}): Promise<MaintenanceRequest> {
  const response = await fetch(API_BASE, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(input),
  });

  if (!response.ok) {
    const data = (await response.json()) as { error?: string };
    throw new Error(data.error ?? "Failed to create request.");
  }

  const data = (await response.json()) as { request: MaintenanceRequest };
  return data.request;
}

export async function updateMaintenanceRequestStatus(
  id: string,
  status: MaintenanceRequest["status"],
): Promise<MaintenanceRequest> {
  const response = await fetch(`${API_BASE}/${id}`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ status }),
  });

  if (!response.ok) {
    const data = (await response.json()) as { error?: string };
    throw new Error(data.error ?? "Failed to update request.");
  }

  const data = (await response.json()) as { request: MaintenanceRequest };
  return data.request;
}

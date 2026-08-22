import { randomUUID } from "crypto";
import type { MaintenanceRequest } from "./types";

const requests: MaintenanceRequest[] = [
  {
    id: randomUUID(),
    room: "A-101",
    issue: "Leaking faucet in bathroom",
    priority: "medium",
    status: "open",
    createdAt: new Date().toISOString(),
  },
  {
    id: randomUUID(),
    room: "B-204",
    issue: "Broken ceiling fan",
    priority: "high",
    status: "in_progress",
    createdAt: new Date().toISOString(),
  },
];

export function listRequests(): MaintenanceRequest[] {
  return [...requests].sort(
    (a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime(),
  );
}

export function createRequest(input: {
  room: string;
  issue: string;
  priority: MaintenanceRequest["priority"];
}): MaintenanceRequest {
  const request: MaintenanceRequest = {
    id: randomUUID(),
    room: input.room.trim(),
    issue: input.issue.trim(),
    priority: input.priority,
    status: "open",
    createdAt: new Date().toISOString(),
  };

  requests.unshift(request);
  return request;
}

export function updateRequestStatus(
  id: string,
  status: MaintenanceRequest["status"],
): MaintenanceRequest | null {
  const request = requests.find((item) => item.id === id);
  if (!request) {
    return null;
  }

  request.status = status;
  return request;
}

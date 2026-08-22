"use client";

import { updateMaintenanceRequestStatus } from "@/lib/api";
import { priorityLabels, statusLabels } from "@/lib/constants";
import type { MaintenanceRequest } from "@/lib/types";
import { inputStyle } from "@/styles/shared";

type RequestCardProps = {
  request: MaintenanceRequest;
  onUpdated: () => Promise<void>;
};

export function RequestCard({ request, onUpdated }: RequestCardProps) {
  async function handleStatusChange(status: MaintenanceRequest["status"]) {
    await updateMaintenanceRequestStatus(request.id, status);
    await onUpdated();
  }

  return (
    <li
      style={{
        border: "1px solid #d9e2ec",
        borderRadius: 10,
        padding: "1rem",
      }}
    >
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          gap: "1rem",
          flexWrap: "wrap",
        }}
      >
        <div>
          <strong>{request.room}</strong>
          <p style={{ margin: "0.35rem 0" }}>{request.issue}</p>
          <small style={{ color: "#52606d" }}>
            Priority: {priorityLabels[request.priority]} · Status:{" "}
            {statusLabels[request.status]}
          </small>
        </div>
        <label style={{ display: "grid", gap: "0.35rem", minWidth: 180 }}>
          Update status
          <select
            value={request.status}
            onChange={(event) =>
              void handleStatusChange(
                event.target.value as MaintenanceRequest["status"],
              )
            }
            style={inputStyle}
          >
            <option value="open">Open</option>
            <option value="in_progress">In Progress</option>
            <option value="resolved">Resolved</option>
          </select>
        </label>
      </div>
    </li>
  );
}

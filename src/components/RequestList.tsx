"use client";

import type { MaintenanceRequest } from "@/lib/types";
import { RequestCard } from "./RequestCard";

type RequestListProps = {
  requests: MaintenanceRequest[];
  loading: boolean;
  onUpdated: () => Promise<void>;
};

export function RequestList({ requests, loading, onUpdated }: RequestListProps) {
  if (loading) {
    return <p>Loading requests...</p>;
  }

  if (requests.length === 0) {
    return <p>No maintenance requests yet.</p>;
  }

  return (
    <ul style={{ listStyle: "none", margin: 0, padding: 0, display: "grid", gap: "1rem" }}>
      {requests.map((request) => (
        <RequestCard key={request.id} request={request} onUpdated={onUpdated} />
      ))}
    </ul>
  );
}

"use client";

import { FormEvent, useEffect, useState } from "react";
import type { MaintenanceRequest } from "@/lib/types";

const priorityLabels: Record<MaintenanceRequest["priority"], string> = {
  low: "Low",
  medium: "Medium",
  high: "High",
};

const statusLabels: Record<MaintenanceRequest["status"], string> = {
  open: "Open",
  in_progress: "In Progress",
  resolved: "Resolved",
};

export default function HomePage() {
  const [requests, setRequests] = useState<MaintenanceRequest[]>([]);
  const [room, setRoom] = useState("");
  const [issue, setIssue] = useState("");
  const [priority, setPriority] = useState<MaintenanceRequest["priority"]>("medium");
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function loadRequests() {
    const response = await fetch("/api/requests");
    const data = (await response.json()) as { requests: MaintenanceRequest[] };
    setRequests(data.requests);
    setLoading(false);
  }

  useEffect(() => {
    void loadRequests();
  }, []);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setSubmitting(true);
    setError(null);

    const response = await fetch("/api/requests", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ room, issue, priority }),
    });

    if (!response.ok) {
      const data = (await response.json()) as { error?: string };
      setError(data.error ?? "Failed to create request.");
      setSubmitting(false);
      return;
    }

    setRoom("");
    setIssue("");
    setPriority("medium");
    await loadRequests();
    setSubmitting(false);
  }

  async function handleStatusChange(
    id: string,
    status: MaintenanceRequest["status"],
  ) {
    const response = await fetch(`/api/requests/${id}`, {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ status }),
    });

    if (response.ok) {
      await loadRequests();
    }
  }

  return (
    <main style={{ maxWidth: 960, margin: "0 auto", padding: "2rem 1.5rem" }}>
      <header style={{ marginBottom: "2rem" }}>
        <h1 style={{ margin: 0, fontSize: "2rem" }}>Hostel Maintenance</h1>
        <p style={{ marginTop: "0.5rem", color: "#52606d" }}>
          Log issues and track repair status across hostel rooms.
        </p>
      </header>

      <section
        style={{
          background: "#fff",
          borderRadius: 12,
          padding: "1.5rem",
          marginBottom: "1.5rem",
          boxShadow: "0 1px 3px rgba(15, 23, 42, 0.08)",
        }}
      >
        <h2 style={{ marginTop: 0 }}>New maintenance request</h2>
        <form onSubmit={handleSubmit} style={{ display: "grid", gap: "1rem" }}>
          <label style={{ display: "grid", gap: "0.35rem" }}>
            Room
            <input
              value={room}
              onChange={(event) => setRoom(event.target.value)}
              placeholder="e.g. C-305"
              required
              style={inputStyle}
            />
          </label>

          <label style={{ display: "grid", gap: "0.35rem" }}>
            Issue
            <textarea
              value={issue}
              onChange={(event) => setIssue(event.target.value)}
              placeholder="Describe the maintenance issue"
              required
              rows={3}
              style={inputStyle}
            />
          </label>

          <label style={{ display: "grid", gap: "0.35rem", maxWidth: 220 }}>
            Priority
            <select
              value={priority}
              onChange={(event) =>
                setPriority(event.target.value as MaintenanceRequest["priority"])
              }
              style={inputStyle}
            >
              <option value="low">Low</option>
              <option value="medium">Medium</option>
              <option value="high">High</option>
            </select>
          </label>

          {error ? <p style={{ color: "#b42318", margin: 0 }}>{error}</p> : null}

          <button
            type="submit"
            disabled={submitting}
            style={{
              width: "fit-content",
              background: "#2563eb",
              color: "#fff",
              border: "none",
              borderRadius: 8,
              padding: "0.65rem 1rem",
              opacity: submitting ? 0.7 : 1,
            }}
          >
            {submitting ? "Submitting..." : "Submit request"}
          </button>
        </form>
      </section>

      <section
        style={{
          background: "#fff",
          borderRadius: 12,
          padding: "1.5rem",
          boxShadow: "0 1px 3px rgba(15, 23, 42, 0.08)",
        }}
      >
        <h2 style={{ marginTop: 0 }}>Open requests</h2>
        {loading ? (
          <p>Loading requests...</p>
        ) : requests.length === 0 ? (
          <p>No maintenance requests yet.</p>
        ) : (
          <ul style={{ listStyle: "none", margin: 0, padding: 0, display: "grid", gap: "1rem" }}>
            {requests.map((request) => (
              <li
                key={request.id}
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
                          request.id,
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
            ))}
          </ul>
        )}
      </section>
    </main>
  );
}

const inputStyle: React.CSSProperties = {
  border: "1px solid #cbd2d9",
  borderRadius: 8,
  padding: "0.6rem 0.75rem",
  background: "#fff",
};

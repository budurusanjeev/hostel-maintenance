"use client";

import { FormEvent, useState } from "react";
import { createMaintenanceRequest } from "@/lib/api";
import type { MaintenanceRequest } from "@/lib/types";
import { inputStyle } from "@/styles/shared";

type MaintenanceFormProps = {
  onCreated: () => Promise<void>;
};

export function MaintenanceForm({ onCreated }: MaintenanceFormProps) {
  const [room, setRoom] = useState("");
  const [issue, setIssue] = useState("");
  const [priority, setPriority] = useState<MaintenanceRequest["priority"]>("medium");
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setSubmitting(true);
    setError(null);

    try {
      await createMaintenanceRequest({ room, issue, priority });
      setRoom("");
      setIssue("");
      setPriority("medium");
      await onCreated();
    } catch (submitError) {
      setError(
        submitError instanceof Error ? submitError.message : "Failed to create request.",
      );
    } finally {
      setSubmitting(false);
    }
  }

  return (
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
  );
}

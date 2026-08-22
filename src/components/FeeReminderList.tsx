"use client";

import { markStudentFeePaid } from "@/lib/api";
import { feeReminderColors, feeReminderLabels } from "@/lib/constants";
import type { FeeReminder } from "@/lib/types";

type FeeReminderListProps = {
  reminders: FeeReminder[];
  loading: boolean;
  onUpdated: () => Promise<void>;
};

function formatDueMessage(reminder: FeeReminder): string {
  if (reminder.status === "overdue") {
    const days = Math.abs(reminder.daysUntilDue);
    return `${days} day${days === 1 ? "" : "s"} overdue`;
  }

  if (reminder.daysUntilDue === 0) {
    return "Due today";
  }

  return `Due in ${reminder.daysUntilDue} day${reminder.daysUntilDue === 1 ? "" : "s"}`;
}

export function FeeReminderList({ reminders, loading, onUpdated }: FeeReminderListProps) {
  if (loading) {
    return <p>Loading fee reminders...</p>;
  }

  if (reminders.length === 0) {
    return <p>No upcoming or overdue fee reminders.</p>;
  }

  return (
    <ul style={{ listStyle: "none", margin: 0, padding: 0, display: "grid", gap: "1rem" }}>
      {reminders.map((reminder) => (
        <li
          key={reminder.student.id}
          style={{
            border: "1px solid #d9e2ec",
            borderRadius: 10,
            padding: "1rem",
            borderLeft: `4px solid ${feeReminderColors[reminder.status]}`,
          }}
        >
          <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              gap: "1rem",
              flexWrap: "wrap",
              alignItems: "flex-start",
            }}
          >
            <div>
              <strong>{reminder.student.name}</strong>
              <span
                style={{
                  marginLeft: "0.5rem",
                  fontSize: "0.8rem",
                  fontWeight: 600,
                  color: feeReminderColors[reminder.status],
                }}
              >
                {feeReminderLabels[reminder.status]}
              </span>
              <p style={{ margin: "0.35rem 0", color: "#52606d" }}>
                Room {reminder.student.room} · ₹{reminder.student.feeAmount.toLocaleString()} · Due{" "}
                {reminder.student.feeDueDate}
              </p>
              <small style={{ color: feeReminderColors[reminder.status] }}>
                {formatDueMessage(reminder)}
              </small>
              <p style={{ margin: "0.35rem 0 0", fontSize: "0.9rem" }}>
                Parent: {reminder.student.parentName} ({reminder.student.parentPhone})
              </p>
            </div>
            <button
              type="button"
              onClick={() => void markStudentFeePaid(reminder.student.id).then(onUpdated)}
              style={{
                background: "#027a48",
                color: "#fff",
                border: "none",
                borderRadius: 8,
                padding: "0.55rem 0.9rem",
                whiteSpace: "nowrap",
              }}
            >
              Mark fee paid
            </button>
          </div>
        </li>
      ))}
    </ul>
  );
}

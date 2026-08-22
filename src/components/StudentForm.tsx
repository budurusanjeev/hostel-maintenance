"use client";

import { FormEvent, useState } from "react";
import { createStudent } from "@/lib/api";
import { inputStyle } from "@/styles/shared";

type StudentFormProps = {
  onCreated: () => Promise<void>;
};

function defaultDueDate(): string {
  const date = new Date();
  date.setDate(date.getDate() + 7);
  return date.toISOString().slice(0, 10);
}

export function StudentForm({ onCreated }: StudentFormProps) {
  const [name, setName] = useState("");
  const [room, setRoom] = useState("");
  const [phone, setPhone] = useState("");
  const [email, setEmail] = useState("");
  const [course, setCourse] = useState("");
  const [year, setYear] = useState("1st Year");
  const [parentName, setParentName] = useState("");
  const [parentPhone, setParentPhone] = useState("");
  const [feeAmount, setFeeAmount] = useState("8000");
  const [feeDueDate, setFeeDueDate] = useState(defaultDueDate);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setSubmitting(true);
    setError(null);

    try {
      await createStudent({
        name,
        room,
        phone,
        email,
        course,
        year,
        parentName,
        parentPhone,
        feeAmount: Number(feeAmount),
        feeDueDate,
      });

      setName("");
      setRoom("");
      setPhone("");
      setEmail("");
      setCourse("");
      setYear("1st Year");
      setParentName("");
      setParentPhone("");
      setFeeAmount("8000");
      setFeeDueDate(defaultDueDate());
      await onCreated();
    } catch (submitError) {
      setError(
        submitError instanceof Error ? submitError.message : "Failed to save student.",
      );
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} style={{ display: "grid", gap: "1rem" }}>
      <div style={{ display: "grid", gap: "1rem", gridTemplateColumns: "repeat(auto-fit, minmax(220px, 1fr))" }}>
        <label style={{ display: "grid", gap: "0.35rem" }}>
          Student name
          <input value={name} onChange={(e) => setName(e.target.value)} required style={inputStyle} />
        </label>
        <label style={{ display: "grid", gap: "0.35rem" }}>
          Room
          <input value={room} onChange={(e) => setRoom(e.target.value)} placeholder="e.g. A-101" required style={inputStyle} />
        </label>
        <label style={{ display: "grid", gap: "0.35rem" }}>
          Phone
          <input value={phone} onChange={(e) => setPhone(e.target.value)} required style={inputStyle} />
        </label>
        <label style={{ display: "grid", gap: "0.35rem" }}>
          Email
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} style={inputStyle} />
        </label>
        <label style={{ display: "grid", gap: "0.35rem" }}>
          Course
          <input value={course} onChange={(e) => setCourse(e.target.value)} required style={inputStyle} />
        </label>
        <label style={{ display: "grid", gap: "0.35rem" }}>
          Year
          <select value={year} onChange={(e) => setYear(e.target.value)} style={inputStyle}>
            <option>1st Year</option>
            <option>2nd Year</option>
            <option>3rd Year</option>
            <option>4th Year</option>
          </select>
        </label>
        <label style={{ display: "grid", gap: "0.35rem" }}>
          Parent / guardian name
          <input value={parentName} onChange={(e) => setParentName(e.target.value)} required style={inputStyle} />
        </label>
        <label style={{ display: "grid", gap: "0.35rem" }}>
          Parent phone
          <input value={parentPhone} onChange={(e) => setParentPhone(e.target.value)} required style={inputStyle} />
        </label>
        <label style={{ display: "grid", gap: "0.35rem" }}>
          Monthly fee (₹)
          <input type="number" min="1" value={feeAmount} onChange={(e) => setFeeAmount(e.target.value)} required style={inputStyle} />
        </label>
        <label style={{ display: "grid", gap: "0.35rem" }}>
          Fee due date
          <input type="date" value={feeDueDate} onChange={(e) => setFeeDueDate(e.target.value)} required style={inputStyle} />
        </label>
      </div>

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
        {submitting ? "Saving..." : "Save student details"}
      </button>
    </form>
  );
}

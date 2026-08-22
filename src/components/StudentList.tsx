"use client";

import type { Student } from "@/lib/types";

type StudentListProps = {
  students: Student[];
  loading: boolean;
};

export function StudentList({ students, loading }: StudentListProps) {
  if (loading) {
    return <p>Loading students...</p>;
  }

  if (students.length === 0) {
    return <p>No students registered yet.</p>;
  }

  return (
    <div style={{ overflowX: "auto" }}>
      <table style={{ width: "100%", borderCollapse: "collapse", fontSize: "0.95rem" }}>
        <thead>
          <tr style={{ textAlign: "left", borderBottom: "2px solid #d9e2ec" }}>
            <th style={{ padding: "0.75rem 0.5rem" }}>Name</th>
            <th style={{ padding: "0.75rem 0.5rem" }}>Room</th>
            <th style={{ padding: "0.75rem 0.5rem" }}>Course</th>
            <th style={{ padding: "0.75rem 0.5rem" }}>Phone</th>
            <th style={{ padding: "0.75rem 0.5rem" }}>Parent</th>
            <th style={{ padding: "0.75rem 0.5rem" }}>Fee</th>
            <th style={{ padding: "0.75rem 0.5rem" }}>Due date</th>
          </tr>
        </thead>
        <tbody>
          {students.map((student) => (
            <tr key={student.id} style={{ borderBottom: "1px solid #e4e7eb" }}>
              <td style={{ padding: "0.75rem 0.5rem" }}>{student.name}</td>
              <td style={{ padding: "0.75rem 0.5rem" }}>{student.room}</td>
              <td style={{ padding: "0.75rem 0.5rem" }}>
                {student.course} · {student.year}
              </td>
              <td style={{ padding: "0.75rem 0.5rem" }}>{student.phone}</td>
              <td style={{ padding: "0.75rem 0.5rem" }}>
                {student.parentName}
                <br />
                <small style={{ color: "#52606d" }}>{student.parentPhone}</small>
              </td>
              <td style={{ padding: "0.75rem 0.5rem" }}>₹{student.feeAmount.toLocaleString()}</td>
              <td style={{ padding: "0.75rem 0.5rem" }}>{student.feeDueDate}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

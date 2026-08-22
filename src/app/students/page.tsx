"use client";

import { useCallback, useEffect, useState } from "react";
import { PageShell } from "@/components/PageShell";
import { StudentForm } from "@/components/StudentForm";
import { StudentList } from "@/components/StudentList";
import { fetchStudents } from "@/lib/api";
import type { Student } from "@/lib/types";
import { cardStyle } from "@/styles/shared";

export default function StudentsPage() {
  const [students, setStudents] = useState<Student[]>([]);
  const [loading, setLoading] = useState(true);

  const loadStudents = useCallback(async () => {
    const nextStudents = await fetchStudents();
    setStudents(nextStudents);
    setLoading(false);
  }, []);

  useEffect(() => {
    void loadStudents();
  }, [loadStudents]);

  return (
    <PageShell
      title="Student Details"
      description="Register hostel students with contact and fee information."
    >
      <section style={{ ...cardStyle, marginBottom: "1.5rem" }}>
        <h3 style={{ marginTop: 0 }}>New student entry</h3>
        <StudentForm onCreated={loadStudents} />
      </section>

      <section style={cardStyle}>
        <h3 style={{ marginTop: 0 }}>Registered students</h3>
        <StudentList students={students} loading={loading} />
      </section>
    </PageShell>
  );
}

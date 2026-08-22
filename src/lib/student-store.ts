import { randomUUID } from "crypto";
import type { FeeReminder, FeeReminderStatus, Student } from "./types";

const students: Student[] = [
  {
    id: randomUUID(),
    name: "Rahul Sharma",
    room: "A-101",
    phone: "9876543210",
    email: "rahul@example.com",
    course: "Computer Science",
    year: "2nd Year",
    parentName: "Anil Sharma",
    parentPhone: "9876501234",
    feeAmount: 8000,
    feeDueDate: daysFromNow(-2),
    createdAt: new Date().toISOString(),
  },
  {
    id: randomUUID(),
    name: "Priya Nair",
    room: "B-204",
    phone: "9123456780",
    email: "priya@example.com",
    course: "Electronics",
    year: "1st Year",
    parentName: "Lakshmi Nair",
    parentPhone: "9123409876",
    feeAmount: 7500,
    feeDueDate: daysFromNow(3),
    createdAt: new Date().toISOString(),
  },
];

function daysFromNow(days: number): string {
  const date = new Date();
  date.setDate(date.getDate() + days);
  return date.toISOString().slice(0, 10);
}

function startOfDay(date: Date): Date {
  return new Date(date.getFullYear(), date.getMonth(), date.getDate());
}

export function listStudents(): Student[] {
  return [...students].sort((a, b) => a.name.localeCompare(b.name));
}

export function createStudent(input: Omit<Student, "id" | "createdAt">): Student {
  const student: Student = {
    id: randomUUID(),
    name: input.name.trim(),
    room: input.room.trim(),
    phone: input.phone.trim(),
    email: input.email.trim(),
    course: input.course.trim(),
    year: input.year.trim(),
    parentName: input.parentName.trim(),
    parentPhone: input.parentPhone.trim(),
    feeAmount: input.feeAmount,
    feeDueDate: input.feeDueDate,
    createdAt: new Date().toISOString(),
  };

  students.unshift(student);
  return student;
}

export function updateStudent(
  id: string,
  input: Partial<Omit<Student, "id" | "createdAt">>,
): Student | null {
  const student = students.find((item) => item.id === id);
  if (!student) {
    return null;
  }

  if (input.name !== undefined) student.name = input.name.trim();
  if (input.room !== undefined) student.room = input.room.trim();
  if (input.phone !== undefined) student.phone = input.phone.trim();
  if (input.email !== undefined) student.email = input.email.trim();
  if (input.course !== undefined) student.course = input.course.trim();
  if (input.year !== undefined) student.year = input.year.trim();
  if (input.parentName !== undefined) student.parentName = input.parentName.trim();
  if (input.parentPhone !== undefined) student.parentPhone = input.parentPhone.trim();
  if (input.feeAmount !== undefined) student.feeAmount = input.feeAmount;
  if (input.feeDueDate !== undefined) student.feeDueDate = input.feeDueDate;

  return student;
}

export function getStudent(id: string): Student | null {
  return students.find((item) => item.id === id) ?? null;
}

export function getFeeReminderStatus(dueDate: string, today = new Date()): {
  status: FeeReminderStatus;
  daysUntilDue: number;
} {
  const due = startOfDay(new Date(dueDate));
  const current = startOfDay(today);
  const diffMs = due.getTime() - current.getTime();
  const daysUntilDue = Math.round(diffMs / (1000 * 60 * 60 * 24));

  if (daysUntilDue < 0) {
    return { status: "overdue", daysUntilDue };
  }

  if (daysUntilDue <= 7) {
    return { status: "due_soon", daysUntilDue };
  }

  if (daysUntilDue <= 30) {
    return { status: "upcoming", daysUntilDue };
  }

  return { status: "paid", daysUntilDue };
}

export function listFeeReminders(): FeeReminder[] {
  return listStudents()
    .map((student) => {
      const { status, daysUntilDue } = getFeeReminderStatus(student.feeDueDate);
      return { student, status, daysUntilDue };
    })
    .filter((reminder) => reminder.status !== "paid")
    .sort((a, b) => a.daysUntilDue - b.daysUntilDue);
}

export function markFeePaid(id: string): Student | null {
  const student = getStudent(id);
  if (!student) {
    return null;
  }

  const nextDue = new Date(student.feeDueDate);
  nextDue.setMonth(nextDue.getMonth() + 1);
  student.feeDueDate = nextDue.toISOString().slice(0, 10);
  return student;
}

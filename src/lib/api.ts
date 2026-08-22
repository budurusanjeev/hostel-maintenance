import type { FeeReminder, MaintenanceRequest, Student } from "./types";

const API_REQUESTS = "/api/requests";
const API_STUDENTS = "/api/students";
const API_FEE_REMINDERS = "/api/fees/reminders";

export async function fetchRequests(): Promise<MaintenanceRequest[]> {
  const response = await fetch(API_REQUESTS);
  const data = (await response.json()) as { requests: MaintenanceRequest[] };
  return data.requests;
}

export async function createMaintenanceRequest(input: {
  room: string;
  issue: string;
  priority: MaintenanceRequest["priority"];
}): Promise<MaintenanceRequest> {
  const response = await fetch(API_REQUESTS, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(input),
  });

  if (!response.ok) {
    const data = (await response.json()) as { error?: string };
    throw new Error(data.error ?? "Failed to create request.");
  }

  const data = (await response.json()) as { request: MaintenanceRequest };
  return data.request;
}

export async function updateMaintenanceRequestStatus(
  id: string,
  status: MaintenanceRequest["status"],
): Promise<MaintenanceRequest> {
  const response = await fetch(`${API_REQUESTS}/${id}`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ status }),
  });

  if (!response.ok) {
    const data = (await response.json()) as { error?: string };
    throw new Error(data.error ?? "Failed to update request.");
  }

  const data = (await response.json()) as { request: MaintenanceRequest };
  return data.request;
}

export async function fetchStudents(): Promise<Student[]> {
  const response = await fetch(API_STUDENTS);
  const data = (await response.json()) as { students: Student[] };
  return data.students;
}

export async function createStudent(input: {
  name: string;
  room: string;
  phone: string;
  email: string;
  course: string;
  year: string;
  parentName: string;
  parentPhone: string;
  feeAmount: number;
  feeDueDate: string;
}): Promise<Student> {
  const response = await fetch(API_STUDENTS, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(input),
  });

  if (!response.ok) {
    const data = (await response.json()) as { error?: string };
    throw new Error(data.error ?? "Failed to create student.");
  }

  const data = (await response.json()) as { student: Student };
  return data.student;
}

export async function markStudentFeePaid(id: string): Promise<Student> {
  const response = await fetch(`${API_STUDENTS}/${id}`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ action: "mark_paid" }),
  });

  if (!response.ok) {
    const data = (await response.json()) as { error?: string };
    throw new Error(data.error ?? "Failed to mark fee as paid.");
  }

  const data = (await response.json()) as { student: Student };
  return data.student;
}

export async function fetchFeeReminders(): Promise<FeeReminder[]> {
  const response = await fetch(API_FEE_REMINDERS);
  const data = (await response.json()) as { reminders: FeeReminder[] };
  return data.reminders;
}

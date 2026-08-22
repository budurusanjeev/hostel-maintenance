export type MaintenanceRequest = {
  id: string;
  room: string;
  issue: string;
  priority: "low" | "medium" | "high";
  status: "open" | "in_progress" | "resolved";
  createdAt: string;
};

export type Student = {
  id: string;
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
  createdAt: string;
};

export type FeeReminderStatus = "overdue" | "due_soon" | "upcoming" | "paid";

export type FeeReminder = {
  student: Student;
  status: FeeReminderStatus;
  daysUntilDue: number;
};

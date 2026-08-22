import { NextResponse } from "next/server";
import { listFeeReminders } from "@/lib/student-store";

export async function GET() {
  return NextResponse.json({ reminders: listFeeReminders() });
}

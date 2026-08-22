import { NextResponse } from "next/server";
import { createStudent, listStudents } from "@/lib/student-store";

export async function GET() {
  return NextResponse.json({ students: listStudents() });
}

export async function POST(request: Request) {
  const body = (await request.json()) as {
    name?: string;
    room?: string;
    phone?: string;
    email?: string;
    course?: string;
    year?: string;
    parentName?: string;
    parentPhone?: string;
    feeAmount?: number;
    feeDueDate?: string;
  };

  if (
    !body.name?.trim() ||
    !body.room?.trim() ||
    !body.phone?.trim() ||
    !body.course?.trim() ||
    !body.year?.trim() ||
    !body.parentName?.trim() ||
    !body.parentPhone?.trim() ||
    !body.feeDueDate?.trim()
  ) {
    return NextResponse.json(
      { error: "Name, room, phone, course, year, parent details, and fee due date are required." },
      { status: 400 },
    );
  }

  const feeAmount = Number(body.feeAmount);
  if (!Number.isFinite(feeAmount) || feeAmount <= 0) {
    return NextResponse.json({ error: "Fee amount must be a positive number." }, { status: 400 });
  }

  const student = createStudent({
    name: body.name,
    room: body.room,
    phone: body.phone,
    email: body.email ?? "",
    course: body.course,
    year: body.year,
    parentName: body.parentName,
    parentPhone: body.parentPhone,
    feeAmount,
    feeDueDate: body.feeDueDate,
  });

  return NextResponse.json({ student }, { status: 201 });
}

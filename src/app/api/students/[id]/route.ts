import { NextResponse } from "next/server";
import { markFeePaid, updateStudent } from "@/lib/student-store";

type RouteContext = {
  params: Promise<{ id: string }>;
};

export async function PATCH(request: Request, context: RouteContext) {
  const { id } = await context.params;
  const body = (await request.json()) as {
    action?: "mark_paid";
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

  if (body.action === "mark_paid") {
    const student = markFeePaid(id);
    if (!student) {
      return NextResponse.json({ error: "Student not found." }, { status: 404 });
    }
    return NextResponse.json({ student });
  }

  const student = updateStudent(id, body);
  if (!student) {
    return NextResponse.json({ error: "Student not found." }, { status: 404 });
  }

  return NextResponse.json({ student });
}

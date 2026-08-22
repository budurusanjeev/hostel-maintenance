import { NextResponse } from "next/server";
import { createRequest, listRequests } from "@/lib/store";
import type { MaintenanceRequest } from "@/lib/types";

export async function GET() {
  return NextResponse.json({ requests: listRequests() });
}

export async function POST(request: Request) {
  const body = (await request.json()) as {
    room?: string;
    issue?: string;
    priority?: MaintenanceRequest["priority"];
  };

  if (!body.room?.trim() || !body.issue?.trim()) {
    return NextResponse.json(
      { error: "Room and issue are required." },
      { status: 400 },
    );
  }

  const priority = body.priority ?? "medium";
  if (!["low", "medium", "high"].includes(priority)) {
    return NextResponse.json({ error: "Invalid priority." }, { status: 400 });
  }

  const created = createRequest({
    room: body.room,
    issue: body.issue,
    priority,
  });

  return NextResponse.json({ request: created }, { status: 201 });
}

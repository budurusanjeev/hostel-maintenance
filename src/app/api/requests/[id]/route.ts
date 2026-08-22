import { NextResponse } from "next/server";
import { updateRequestStatus } from "@/lib/store";
import type { MaintenanceRequest } from "@/lib/types";

type RouteContext = {
  params: Promise<{ id: string }>;
};

export async function PATCH(request: Request, context: RouteContext) {
  const { id } = await context.params;
  const body = (await request.json()) as {
    status?: MaintenanceRequest["status"];
  };

  if (!body.status || !["open", "in_progress", "resolved"].includes(body.status)) {
    return NextResponse.json({ error: "Invalid status." }, { status: 400 });
  }

  const updated = updateRequestStatus(id, body.status);
  if (!updated) {
    return NextResponse.json({ error: "Request not found." }, { status: 404 });
  }

  return NextResponse.json({ request: updated });
}

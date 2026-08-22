"use client";

import { useCallback, useEffect, useState } from "react";
import { MaintenanceForm } from "@/components/MaintenanceForm";
import { PageShell } from "@/components/PageShell";
import { RequestList } from "@/components/RequestList";
import { fetchRequests } from "@/lib/api";
import type { MaintenanceRequest } from "@/lib/types";
import { cardStyle } from "@/styles/shared";

export default function MaintenancePage() {
  const [requests, setRequests] = useState<MaintenanceRequest[]>([]);
  const [loading, setLoading] = useState(true);

  const loadRequests = useCallback(async () => {
    const nextRequests = await fetchRequests();
    setRequests(nextRequests);
    setLoading(false);
  }, []);

  useEffect(() => {
    void loadRequests();
  }, [loadRequests]);

  return (
    <PageShell
      title="Maintenance"
      description="Log issues and track repair status across hostel rooms."
    >
      <section style={{ ...cardStyle, marginBottom: "1.5rem" }}>
        <h3 style={{ marginTop: 0 }}>New maintenance request</h3>
        <MaintenanceForm onCreated={loadRequests} />
      </section>

      <section style={cardStyle}>
        <h3 style={{ marginTop: 0 }}>Open requests</h3>
        <RequestList requests={requests} loading={loading} onUpdated={loadRequests} />
      </section>
    </PageShell>
  );
}

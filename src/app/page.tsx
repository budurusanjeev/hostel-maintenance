"use client";

import { useCallback, useEffect, useState } from "react";
import { MaintenanceForm } from "@/components/MaintenanceForm";
import { RequestList } from "@/components/RequestList";
import { fetchRequests } from "@/lib/api";
import type { MaintenanceRequest } from "@/lib/types";
import { cardStyle } from "@/styles/shared";

export default function HomePage() {
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
    <main style={{ maxWidth: 960, margin: "0 auto", padding: "2rem 1.5rem" }}>
      <header style={{ marginBottom: "2rem" }}>
        <h1 style={{ margin: 0, fontSize: "2rem" }}>Hostel Maintenance</h1>
        <p style={{ marginTop: "0.5rem", color: "#52606d" }}>
          Log issues and track repair status across hostel rooms.
        </p>
      </header>

      <section style={{ ...cardStyle, marginBottom: "1.5rem" }}>
        <h2 style={{ marginTop: 0 }}>New maintenance request</h2>
        <MaintenanceForm onCreated={loadRequests} />
      </section>

      <section style={cardStyle}>
        <h2 style={{ marginTop: 0 }}>Open requests</h2>
        <RequestList requests={requests} loading={loading} onUpdated={loadRequests} />
      </section>
    </main>
  );
}

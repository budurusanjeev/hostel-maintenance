"use client";

import { useCallback, useEffect, useState } from "react";
import { FeeReminderList } from "@/components/FeeReminderList";
import { PageShell } from "@/components/PageShell";
import { fetchFeeReminders } from "@/lib/api";
import type { FeeReminder } from "@/lib/types";
import { cardStyle } from "@/styles/shared";

export default function FeesPage() {
  const [reminders, setReminders] = useState<FeeReminder[]>([]);
  const [loading, setLoading] = useState(true);

  const loadReminders = useCallback(async () => {
    const nextReminders = await fetchFeeReminders();
    setReminders(nextReminders);
    setLoading(false);
  }, []);

  useEffect(() => {
    void loadReminders();
  }, [loadReminders]);

  return (
    <PageShell
      title="Fee Date Reminders"
      description="Track overdue and upcoming hostel fee payments."
    >
      <section style={cardStyle}>
        <p style={{ marginTop: 0, color: "#52606d" }}>
          Reminders show students with fees overdue or due within the next 30 days.
        </p>
        <FeeReminderList reminders={reminders} loading={loading} onUpdated={loadReminders} />
      </section>
    </PageShell>
  );
}

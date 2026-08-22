import type { ReactNode } from "react";
import { AppNav } from "@/components/AppNav";

type PageShellProps = {
  title: string;
  description: string;
  children: ReactNode;
};

export function PageShell({ title, description, children }: PageShellProps) {
  return (
    <main style={{ maxWidth: 1100, margin: "0 auto", padding: "2rem 1.5rem" }}>
      <header style={{ marginBottom: "0.5rem" }}>
        <h1 style={{ margin: 0, fontSize: "2rem" }}>Hostel Management</h1>
        <p style={{ marginTop: "0.5rem", color: "#52606d" }}>{description}</p>
      </header>
      <AppNav />
      <h2 style={{ marginTop: 0, marginBottom: "1rem" }}>{title}</h2>
      {children}
    </main>
  );
}

"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

const links = [
  { href: "/", label: "Maintenance" },
  { href: "/students", label: "Student Details" },
  { href: "/fees", label: "Fee Reminders" },
];

export function AppNav() {
  const pathname = usePathname();

  return (
    <nav
      style={{
        display: "flex",
        gap: "0.5rem",
        flexWrap: "wrap",
        marginBottom: "1.5rem",
      }}
    >
      {links.map((link) => {
        const active = pathname === link.href;
        return (
          <Link
            key={link.href}
            href={link.href}
            style={{
              padding: "0.5rem 1rem",
              borderRadius: 8,
              textDecoration: "none",
              color: active ? "#fff" : "#1f2933",
              background: active ? "#2563eb" : "#e4e7eb",
              fontWeight: active ? 600 : 400,
            }}
          >
            {link.label}
          </Link>
        );
      })}
    </nav>
  );
}

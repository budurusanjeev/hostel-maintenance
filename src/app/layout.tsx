import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Hostel Management",
  description: "Manage hostel maintenance, student details, and fee reminders",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}

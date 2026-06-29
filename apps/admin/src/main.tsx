import React, { useEffect, useMemo, useState } from "react";
import { createRoot } from "react-dom/client";
import axios from "axios";
import { Activity, BookOpen, Shield, Users } from "lucide-react";
import { Bar, BarChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";
import "./styles.css";

const api = axios.create({
  baseURL: import.meta.env.VITE_ADMIN_API_URL ?? "http://localhost:8080/api/admin/v1"
});

type Dashboard = {
  analytics: { users: number; lessons: number; aiRequests: number; pronunciationAttempts: number };
  auditEvents: number;
  activeJobs: number;
  systemHealth: string;
};

function App() {
  const [token, setToken] = useState("");
  const [dashboard, setDashboard] = useState<Dashboard>();
  const [auditLogs, setAuditLogs] = useState<Array<Record<string, string>>>([]);

  useEffect(() => {
    if (!token) return;
    api.defaults.headers.common.Authorization = `Bearer ${token}`;
    api.get("/dashboard").then((response) => setDashboard(response.data.data));
    api.get("/audit-logs").then((response) => setAuditLogs(response.data.data));
  }, [token]);

  const chartData = useMemo(() => {
    if (!dashboard) return [];
    return [
      { name: "Users", value: dashboard.analytics.users },
      { name: "Lessons", value: dashboard.analytics.lessons },
      { name: "AI", value: dashboard.analytics.aiRequests },
      { name: "Speech", value: dashboard.analytics.pronunciationAttempts }
    ];
  }, [dashboard]);

  return (
    <main className="shell">
      <section className="hero">
        <div>
          <p className="eyebrow">WortMeister Admin</p>
          <h1>Enterprise Operations Portal</h1>
          <p>Analytics, audit logs, learning content operations, and system health in one focused dashboard.</p>
        </div>
        <input
          aria-label="Admin JWT"
          placeholder="Paste admin JWT"
          value={token}
          onChange={(event) => setToken(event.target.value)}
        />
      </section>

      <section className="metrics">
        <Metric icon={<Users />} label="Users" value={dashboard?.analytics.users ?? 0} />
        <Metric icon={<BookOpen />} label="Lessons" value={dashboard?.analytics.lessons ?? 0} />
        <Metric icon={<Activity />} label="AI Requests" value={dashboard?.analytics.aiRequests ?? 0} />
        <Metric icon={<Shield />} label="Audit Events" value={dashboard?.auditEvents ?? 0} />
      </section>

      <section className="panel">
        <h2>Analytics Dashboard</h2>
        <ResponsiveContainer width="100%" height={280}>
          <BarChart data={chartData}>
            <XAxis dataKey="name" stroke="#aeb4c2" />
            <YAxis stroke="#aeb4c2" />
            <Tooltip />
            <Bar dataKey="value" fill="#7FA36B" radius={[6, 6, 0, 0]} />
          </BarChart>
        </ResponsiveContainer>
      </section>

      <section className="panel">
        <h2>Activity Timeline</h2>
        {auditLogs.map((log) => (
          <article className="timeline" key={log.id}>
            <strong>{log.action}</strong>
            <span>{log.actor} · {log.resource}</span>
            <p>{log.metadata}</p>
          </article>
        ))}
        {!auditLogs.length ? <p className="muted">Paste an admin JWT to load audit activity.</p> : null}
      </section>
    </main>
  );
}

function Metric({ icon, label, value }: { icon: React.ReactNode; label: string; value: number }) {
  return (
    <article className="metric">
      {icon}
      <span>{label}</span>
      <strong>{value}</strong>
    </article>
  );
}

createRoot(document.getElementById("root")!).render(<App />);

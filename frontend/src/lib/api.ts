const BASE_URL = "/api";

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const res = await fetch(`${BASE_URL}${path}`, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });
  if (!res.ok) {
    const error = await res.json().catch(() => ({ error: res.statusText }));
    throw new Error(error.error || res.statusText);
  }
  if (res.status === 204) return undefined as T;
  return res.json();
}

export const api = {
  lists: {
    getAll: () => request<import("@/types").ReminderList[]>("/lists"),
    get: (id: number) => request<import("@/types").ReminderList>(`/lists/${id}`),
    create: (data: import("@/types").ReminderListRequest) =>
      request<import("@/types").ReminderList>("/lists", { method: "POST", body: JSON.stringify(data) }),
    update: (id: number, data: import("@/types").ReminderListRequest) =>
      request<import("@/types").ReminderList>(`/lists/${id}`, { method: "PUT", body: JSON.stringify(data) }),
    delete: (id: number) => request<void>(`/lists/${id}`, { method: "DELETE" }),
  },
  reminders: {
    getByList: (listId: number) =>
      request<import("@/types").Reminder[]>(`/lists/${listId}/reminders`),
    get: (id: number) => request<import("@/types").Reminder>(`/reminders/${id}`),
    create: (listId: number, data: import("@/types").ReminderRequest) =>
      request<import("@/types").Reminder>(`/lists/${listId}/reminders`, {
        method: "POST",
        body: JSON.stringify(data),
      }),
    update: (id: number, data: import("@/types").ReminderRequest) =>
      request<import("@/types").Reminder>(`/reminders/${id}`, { method: "PUT", body: JSON.stringify(data) }),
    delete: (id: number) => request<void>(`/reminders/${id}`, { method: "DELETE" }),
    toggleComplete: (id: number) =>
      request<import("@/types").Reminder>(`/reminders/${id}/complete`, { method: "PATCH" }),
    toggleFlag: (id: number) =>
      request<import("@/types").Reminder>(`/reminders/${id}/flag`, { method: "PATCH" }),
    search: (q: string) => request<import("@/types").Reminder[]>(`/reminders/search?q=${encodeURIComponent(q)}`),
  },
  smart: {
    today: () => request<import("@/types").Reminder[]>("/smart/today"),
    scheduled: () => request<import("@/types").Reminder[]>("/smart/scheduled"),
    all: () => request<import("@/types").Reminder[]>("/smart/all"),
    flagged: () => request<import("@/types").Reminder[]>("/smart/flagged"),
    completed: () => request<import("@/types").Reminder[]>("/smart/completed"),
    counts: () =>
      request<{ today: number; scheduled: number; all: number; flagged: number; completed: number }>("/smart/counts"),
  },
};

export const fetcher = <T>(path: string) => request<T>(path);

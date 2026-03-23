"use client";

import useSWR from "swr";
import { api } from "@/lib/api";
import { Reminder, ReminderList, ReminderRequest } from "@/types";
import { ReminderRow } from "./ReminderRow";

interface Props {
  query: string;
}

export function SearchResultView({ query }: Props) {
  const { data: reminders, mutate } = useSWR<Reminder[]>(
    query ? `/api/reminders/search?q=${query}` : null,
    () => api.reminders.search(query)
  );
  const { data: lists } = useSWR<ReminderList[]>("/api/lists", () => api.lists.getAll());
  const listMap = new Map(lists?.map((l) => [l.id, l]) || []);

  const handleToggleComplete = async (id: number) => {
    await api.reminders.toggleComplete(id);
    mutate();
  };

  const handleDelete = async (id: number) => {
    await api.reminders.delete(id);
    mutate();
  };

  const handleUpdate = async (id: number, data: ReminderRequest) => {
    await api.reminders.update(id, data);
    mutate();
  };

  return (
    <div className="flex-1 flex flex-col h-full overflow-hidden">
      <div className="px-6 pt-6 pb-2">
        <h1 className="text-[28px] font-bold" style={{ color: "#1D1D1F" }}>
          검색 결과
        </h1>
        <p className="text-[13px] mt-1" style={{ color: "#86868B" }}>
          &ldquo;{query}&rdquo;
        </p>
      </div>

      <div className="flex-1 overflow-y-auto px-6">
        {reminders && reminders.length === 0 && (
          <div className="flex items-center justify-center py-20">
            <p className="text-[15px]" style={{ color: "#AEAEB2" }}>
              검색 결과 없음
            </p>
          </div>
        )}

        {reminders?.map((reminder) => (
          <ReminderRow
            key={reminder.id}
            reminder={reminder}
            listColor={listMap.get(reminder.listId)?.color || "#007AFF"}
            onToggleComplete={handleToggleComplete}
            onDelete={handleDelete}
            onUpdate={handleUpdate}
          />
        ))}
      </div>
    </div>
  );
}

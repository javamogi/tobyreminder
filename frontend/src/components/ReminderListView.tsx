"use client";

import { useState } from "react";
import useSWR from "swr";
import { api } from "@/lib/api";
import { Reminder, ReminderList } from "@/types";
import { ReminderRow } from "./ReminderRow";

interface Props {
  list: ReminderList;
}

export function ReminderListView({ list }: Props) {
  const { data: reminders, mutate } = useSWR<Reminder[]>(
    `/api/lists/${list.id}/reminders`,
    () => api.reminders.getByList(list.id)
  );
  const [isAdding, setIsAdding] = useState(false);
  const [newTitle, setNewTitle] = useState("");

  const handleAdd = async () => {
    if (!newTitle.trim()) {
      setIsAdding(false);
      return;
    }
    await api.reminders.create(list.id, { title: newTitle.trim() });
    setNewTitle("");
    mutate();
  };

  const handleToggleComplete = async (id: number) => {
    await api.reminders.toggleComplete(id);
    mutate();
  };

  const handleDelete = async (id: number) => {
    await api.reminders.delete(id);
    mutate();
  };

  const handleUpdate = async (id: number, data: { title: string; notes?: string }) => {
    await api.reminders.update(id, data);
    mutate();
  };

  return (
    <div className="flex-1 flex flex-col h-full overflow-hidden">
      <div className="px-6 pt-6 pb-2">
        <h1 className="text-[28px] font-bold" style={{ color: list.color }}>
          {list.name}
        </h1>
      </div>

      <div className="flex-1 overflow-y-auto px-6">
        {reminders && reminders.length === 0 && !isAdding && (
          <div className="flex items-center justify-center py-20">
            <p className="text-[15px]" style={{ color: "#AEAEB2" }}>
              리마인더 없음
            </p>
          </div>
        )}

        {reminders?.map((reminder) => (
          <ReminderRow
            key={reminder.id}
            reminder={reminder}
            listColor={list.color}
            onToggleComplete={handleToggleComplete}
            onDelete={handleDelete}
            onUpdate={handleUpdate}
          />
        ))}

        {isAdding && (
          <div className="flex items-center gap-3 py-2">
            <span
              className="w-[22px] h-[22px] rounded-full border-[1.5px] flex-shrink-0"
              style={{ borderColor: list.color }}
            />
            <input
              autoFocus
              className="flex-1 text-[15px] outline-none bg-transparent"
              placeholder="새 리마인더"
              value={newTitle}
              onChange={(e) => setNewTitle(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  handleAdd();
                  setNewTitle("");
                }
                if (e.key === "Escape") {
                  setIsAdding(false);
                  setNewTitle("");
                }
              }}
              onBlur={() => {
                handleAdd();
                setIsAdding(false);
              }}
            />
          </div>
        )}
      </div>

      <div className="px-6 py-3 border-t" style={{ borderColor: "rgba(0,0,0,0.1)" }}>
        <button
          onClick={() => setIsAdding(true)}
          className="flex items-center gap-1 text-[13px] font-medium cursor-pointer"
          style={{ color: list.color }}
        >
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
            <path d="M8 3v10M3 8h10" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
          </svg>
          리마인더 추가
        </button>
      </div>
    </div>
  );
}

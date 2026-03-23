"use client";

import { useState } from "react";
import { Reminder } from "@/types";

interface Props {
  reminder: Reminder;
  onClose: () => void;
  onUpdate: (id: number, data: { title: string; notes?: string }) => void;
  onDelete: (id: number) => void;
}

export function ReminderDetail({ reminder, onClose, onUpdate, onDelete }: Props) {
  const [title, setTitle] = useState(reminder.title);
  const [notes, setNotes] = useState(reminder.notes || "");

  const handleSave = () => {
    if (title.trim() && (title !== reminder.title || notes !== (reminder.notes || ""))) {
      onUpdate(reminder.id, { title: title.trim(), notes: notes || undefined });
    }
    onClose();
  };

  return (
    <div
      className="ml-[34px] mb-2 p-3 rounded-lg"
      style={{ backgroundColor: "rgba(0,0,0,0.03)", border: "0.5px solid rgba(0,0,0,0.1)" }}
    >
      <input
        className="w-full text-[15px] font-medium bg-transparent outline-none mb-2"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        onKeyDown={(e) => { if (e.key === "Enter") handleSave(); }}
      />
      <textarea
        className="w-full text-[13px] bg-transparent outline-none resize-none"
        style={{ color: "#86868B" }}
        rows={2}
        placeholder="메모"
        value={notes}
        onChange={(e) => setNotes(e.target.value)}
      />
      <div className="flex items-center justify-between mt-2 pt-2 border-t" style={{ borderColor: "rgba(0,0,0,0.1)" }}>
        <button
          onClick={() => { onDelete(reminder.id); onClose(); }}
          className="text-[12px] px-2 py-1 rounded cursor-pointer"
          style={{ color: "#FF3B30" }}
        >
          삭제
        </button>
        <button
          onClick={handleSave}
          className="text-[12px] px-3 py-1 rounded cursor-pointer"
          style={{ color: "#007AFF" }}
        >
          완료
        </button>
      </div>
    </div>
  );
}

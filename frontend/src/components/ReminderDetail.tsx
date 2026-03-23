"use client";

import { useState } from "react";
import { Reminder, Priority, ReminderRequest } from "@/types";
import { api } from "@/lib/api";

interface Props {
  reminder: Reminder;
  onClose: () => void;
  onUpdate: (id: number, data: ReminderRequest) => void;
  onDelete: (id: number) => void;
}

const priorityOptions: { value: Priority; label: string }[] = [
  { value: "NONE", label: "없음" },
  { value: "LOW", label: "낮음" },
  { value: "MEDIUM", label: "보통" },
  { value: "HIGH", label: "높음" },
];

export function ReminderDetail({ reminder, onClose, onUpdate, onDelete }: Props) {
  const [title, setTitle] = useState(reminder.title);
  const [notes, setNotes] = useState(reminder.notes || "");
  const [dueDateEnabled, setDueDateEnabled] = useState(!!reminder.dueDate);
  const [dueDate, setDueDate] = useState(reminder.dueDate || "");
  const [dueTimeEnabled, setDueTimeEnabled] = useState(!!reminder.dueTime);
  const [dueTime, setDueTime] = useState(reminder.dueTime || "");
  const [priority, setPriority] = useState<Priority>(reminder.priority || "NONE");
  const [flagged, setFlagged] = useState(reminder.flagged);

  const handleSave = () => {
    onUpdate(reminder.id, {
      title: title.trim() || reminder.title,
      notes: notes || undefined,
      dueDate: dueDateEnabled && dueDate ? dueDate : null,
      dueTime: dueTimeEnabled && dueTime ? dueTime : null,
      priority,
      flagged,
    });
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
        className="w-full text-[13px] bg-transparent outline-none resize-none mb-3"
        style={{ color: "#86868B" }}
        rows={2}
        placeholder="메모"
        value={notes}
        onChange={(e) => setNotes(e.target.value)}
      />

      <div className="space-y-2 text-[13px]" style={{ borderTop: "0.5px solid rgba(0,0,0,0.1)" }}>
        {/* 날짜 */}
        <div className="flex items-center justify-between pt-2">
          <span>날짜</span>
          <div className="flex items-center gap-2">
            {dueDateEnabled && (
              <input
                type="date"
                className="text-[13px] bg-transparent outline-none"
                style={{ color: "#007AFF" }}
                value={dueDate}
                onChange={(e) => setDueDate(e.target.value)}
              />
            )}
            <ToggleSwitch checked={dueDateEnabled} onChange={(v) => { setDueDateEnabled(v); if (!v) setDueDate(""); }} />
          </div>
        </div>

        {/* 시간 */}
        <div className="flex items-center justify-between">
          <span>시간</span>
          <div className="flex items-center gap-2">
            {dueTimeEnabled && (
              <input
                type="time"
                className="text-[13px] bg-transparent outline-none"
                style={{ color: "#007AFF" }}
                value={dueTime}
                onChange={(e) => setDueTime(e.target.value)}
              />
            )}
            <ToggleSwitch checked={dueTimeEnabled} onChange={(v) => { setDueTimeEnabled(v); if (!v) setDueTime(""); }} />
          </div>
        </div>

        {/* 우선순위 */}
        <div className="flex items-center justify-between">
          <span>우선순위</span>
          <select
            className="text-[13px] bg-transparent outline-none cursor-pointer"
            style={{ color: "#007AFF" }}
            value={priority}
            onChange={(e) => setPriority(e.target.value as Priority)}
          >
            {priorityOptions.map((opt) => (
              <option key={opt.value} value={opt.value}>{opt.label}</option>
            ))}
          </select>
        </div>

        {/* 플래그 */}
        <div className="flex items-center justify-between">
          <span>플래그</span>
          <ToggleSwitch checked={flagged} onChange={setFlagged} />
        </div>
      </div>

      <div className="flex items-center justify-between mt-3 pt-2 border-t" style={{ borderColor: "rgba(0,0,0,0.1)" }}>
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

function ToggleSwitch({ checked, onChange }: { checked: boolean; onChange: (v: boolean) => void }) {
  return (
    <button
      onClick={() => onChange(!checked)}
      className="relative w-[42px] h-[26px] rounded-[13px] transition-colors duration-200 cursor-pointer flex-shrink-0"
      style={{ backgroundColor: checked ? "#34C759" : "#E5E5EA" }}
    >
      <span
        className="absolute top-[2px] w-[22px] h-[22px] rounded-full bg-white shadow transition-transform duration-200"
        style={{ transform: checked ? "translateX(18px)" : "translateX(2px)" }}
      />
    </button>
  );
}

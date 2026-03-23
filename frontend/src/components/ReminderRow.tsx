"use client";

import { useState } from "react";
import { Reminder } from "@/types";
import { ReminderDetail } from "./ReminderDetail";
import { useStore } from "@/store/useStore";

interface Props {
  reminder: Reminder;
  listColor: string;
  onToggleComplete: (id: number) => void;
  onDelete: (id: number) => void;
  onUpdate: (id: number, data: { title: string; notes?: string }) => void;
}

export function ReminderRow({ reminder, listColor, onToggleComplete, onDelete, onUpdate }: Props) {
  const { editingReminderId, setEditingReminderId } = useStore();
  const isEditing = editingReminderId === reminder.id;
  const [isHovered, setIsHovered] = useState(false);

  const priorityLabel = (p: string) => {
    switch (p) {
      case "LOW": return "!";
      case "MEDIUM": return "!!";
      case "HIGH": return "!!!";
      default: return null;
    }
  };

  return (
    <div>
      <div
        className="flex items-start gap-3 py-2 group"
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
      >
        <button
          onClick={() => onToggleComplete(reminder.id)}
          className="mt-0.5 flex-shrink-0 cursor-pointer"
        >
          <span
            className="w-[22px] h-[22px] rounded-full border-[1.5px] flex items-center justify-center transition-colors"
            style={{
              borderColor: listColor,
              backgroundColor: reminder.completed ? listColor : isHovered ? `${listColor}20` : "transparent",
            }}
          >
            {reminder.completed && (
              <svg width="12" height="12" viewBox="0 0 12 12" fill="none">
                <path d="M2.5 6l2.5 2.5 4.5-5" stroke="white" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
              </svg>
            )}
          </span>
        </button>

        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-2">
            <span
              className="text-[15px] truncate"
              style={{
                color: reminder.completed ? "#AEAEB2" : "#1D1D1F",
                textDecoration: reminder.completed ? "line-through" : "none",
              }}
            >
              {reminder.title}
            </span>
          </div>

          {reminder.notes && (
            <p className="text-[13px] truncate mt-0.5" style={{ color: "#86868B" }}>
              {reminder.notes}
            </p>
          )}

          <div className="flex items-center gap-2 mt-0.5">
            {reminder.dueDate && (
              <span
                className="text-[12px] flex items-center gap-0.5"
                style={{
                  color: new Date(reminder.dueDate) < new Date(new Date().toDateString())
                    ? "#FF3B30" : "#86868B",
                }}
              >
                📅 {formatDate(reminder.dueDate)}
              </span>
            )}
            {priorityLabel(reminder.priority) && (
              <span className="text-[12px] font-medium" style={{ color: "#007AFF" }}>
                {priorityLabel(reminder.priority)}
              </span>
            )}
            {reminder.flagged && (
              <span className="text-[12px]" style={{ color: "#FF9500" }}>🚩</span>
            )}
          </div>
        </div>

        {isHovered && !isEditing && (
          <button
            onClick={() => setEditingReminderId(reminder.id)}
            className="text-[13px] w-6 h-6 rounded-full flex items-center justify-center flex-shrink-0 cursor-pointer"
            style={{ color: "#007AFF", backgroundColor: "rgba(0,122,255,0.1)" }}
          >
            ⓘ
          </button>
        )}
      </div>

      {isEditing && (
        <ReminderDetail
          reminder={reminder}
          onClose={() => setEditingReminderId(null)}
          onUpdate={onUpdate}
          onDelete={onDelete}
        />
      )}

      <div
        className="ml-[34px]"
        style={{ borderBottom: "0.5px solid rgba(0,0,0,0.1)" }}
      />
    </div>
  );
}

function formatDate(dateStr: string): string {
  const date = new Date(dateStr + "T00:00:00");
  const month = date.getMonth() + 1;
  const day = date.getDate();
  return `${month}월 ${day}일`;
}

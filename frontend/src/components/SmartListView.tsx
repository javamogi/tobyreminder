"use client";

import useSWR from "swr";
import { api } from "@/lib/api";
import { Reminder, ReminderList, ReminderRequest } from "@/types";
import { SmartListType } from "@/store/useStore";
import { ReminderRow } from "./ReminderRow";

const smartListLabels: Record<SmartListType, string> = {
  today: "오늘",
  scheduled: "예정",
  all: "전체",
  flagged: "플래그",
  completed: "완료됨",
};

const smartListColors: Record<SmartListType, string> = {
  today: "#007AFF",
  scheduled: "#FF3B30",
  all: "#8E8E93",
  flagged: "#FF9500",
  completed: "#8E8E93",
};

interface Props {
  type: SmartListType;
}

export function SmartListView({ type }: Props) {
  const { data: reminders, mutate } = useSWR<Reminder[]>(
    `/api/smart/${type}`,
    () => api.smart[type]()
  );
  const { data: lists } = useSWR<ReminderList[]>("/api/lists", () =>
    api.lists.getAll()
  );

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

  const grouped = groupReminders(type, reminders || [], listMap);

  return (
    <div className="flex-1 flex flex-col h-full overflow-hidden">
      <div className="px-6 pt-6 pb-2">
        <h1
          className="text-[28px] font-bold"
          style={{ color: smartListColors[type] }}
        >
          {smartListLabels[type]}
        </h1>
      </div>

      <div className="flex-1 overflow-y-auto px-6">
        {reminders && reminders.length === 0 && (
          <div className="flex items-center justify-center py-20">
            <p className="text-[15px]" style={{ color: "#AEAEB2" }}>
              리마인더 없음
            </p>
          </div>
        )}

        {grouped.map((group) => (
          <div key={group.label} className="mb-4">
            <div className="flex items-center gap-2 mb-1">
              {group.color && (
                <span
                  className="w-3 h-3 rounded-full"
                  style={{ backgroundColor: group.color }}
                />
              )}
              <h3
                className="text-[13px] font-semibold"
                style={{ color: group.labelColor || "#1D1D1F" }}
              >
                {group.label}
              </h3>
            </div>
            {group.items.map((reminder) => (
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
        ))}
      </div>
    </div>
  );
}

interface Group {
  label: string;
  color?: string;
  labelColor?: string;
  items: Reminder[];
}

function groupReminders(
  type: SmartListType,
  reminders: Reminder[],
  listMap: Map<number, ReminderList>
): Group[] {
  if (reminders.length === 0) return [];

  if (type === "scheduled") {
    return groupByDate(reminders);
  }

  if (type === "completed") {
    return groupByCompletedDate(reminders);
  }

  return groupByList(reminders, listMap);
}

function groupByList(
  reminders: Reminder[],
  listMap: Map<number, ReminderList>
): Group[] {
  const map = new Map<number, Reminder[]>();
  for (const r of reminders) {
    const arr = map.get(r.listId) || [];
    arr.push(r);
    map.set(r.listId, arr);
  }
  return Array.from(map.entries()).map(([listId, items]) => {
    const list = listMap.get(listId);
    return {
      label: list?.name || "알 수 없는 리스트",
      color: list?.color,
      items,
    };
  });
}

function groupByDate(reminders: Reminder[]): Group[] {
  const today = new Date().toISOString().split("T")[0];
  const map = new Map<string, Reminder[]>();
  for (const r of reminders) {
    const key = r.dueDate || "날짜 없음";
    const arr = map.get(key) || [];
    arr.push(r);
    map.set(key, arr);
  }
  return Array.from(map.entries()).map(([date, items]) => ({
    label: formatDateLabel(date),
    labelColor: date < today ? "#FF3B30" : undefined,
    items,
  }));
}

function groupByCompletedDate(reminders: Reminder[]): Group[] {
  const map = new Map<string, Reminder[]>();
  for (const r of reminders) {
    const key = r.completedAt ? r.completedAt.split("T")[0] : "알 수 없음";
    const arr = map.get(key) || [];
    arr.push(r);
    map.set(key, arr);
  }
  return Array.from(map.entries()).map(([date, items]) => ({
    label: formatDateLabel(date),
    items,
  }));
}

function formatDateLabel(dateStr: string): string {
  if (dateStr === "날짜 없음" || dateStr === "알 수 없음") return dateStr;
  const today = new Date();
  const date = new Date(dateStr + "T00:00:00");
  const todayStr = today.toISOString().split("T")[0];
  const tomorrowStr = new Date(today.getTime() + 86400000)
    .toISOString()
    .split("T")[0];

  if (dateStr === todayStr) return "오늘";
  if (dateStr === tomorrowStr) return "내일";
  if (dateStr < todayStr) return `지남 — ${date.getMonth() + 1}월 ${date.getDate()}일`;
  return `${date.getMonth() + 1}월 ${date.getDate()}일`;
}

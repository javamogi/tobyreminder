"use client";

import { useState } from "react";
import useSWR from "swr";
import { api } from "@/lib/api";
import { ReminderList } from "@/types";
import { useStore, SmartListType } from "@/store/useStore";
import { SmartListGrid } from "./SmartListGrid";

export function Sidebar() {
  const { selectedListId, smartListType, selectList, selectSmartList } = useStore();
  const { data: lists, mutate } = useSWR<ReminderList[]>("/api/lists", () => api.lists.getAll());
  const [isAdding, setIsAdding] = useState(false);
  const [newListName, setNewListName] = useState("");

  const handleAddList = async () => {
    if (!newListName.trim()) {
      setIsAdding(false);
      return;
    }
    await api.lists.create({ name: newListName.trim() });
    setNewListName("");
    setIsAdding(false);
    mutate();
  };

  return (
    <aside className="w-[280px] min-w-[280px] h-full flex flex-col border-r"
      style={{ backgroundColor: "#F5F5F7", borderColor: "rgba(0,0,0,0.1)" }}>
      <div className="flex-1 overflow-y-auto p-3">
        <SmartListGrid
          selectedType={smartListType}
          onSelect={(type: SmartListType) => selectSmartList(type)}
        />

        <div className="mt-4">
          <h3 className="px-2 mb-1 text-[11px] font-semibold uppercase"
            style={{ color: "#86868B" }}>
            내 리스트
          </h3>
          <ul>
            {lists?.map((list) => (
              <li
                key={list.id}
                onClick={() => selectList(list.id)}
                className="flex items-center gap-2 px-2 py-1.5 rounded-md cursor-pointer transition-colors duration-150"
                style={{
                  backgroundColor: selectedListId === list.id ? "rgba(0,122,255,0.12)" : "transparent",
                }}
                onMouseEnter={(e) => {
                  if (selectedListId !== list.id)
                    e.currentTarget.style.backgroundColor = "rgba(0,0,0,0.04)";
                }}
                onMouseLeave={(e) => {
                  e.currentTarget.style.backgroundColor =
                    selectedListId === list.id ? "rgba(0,122,255,0.12)" : "transparent";
                }}
              >
                <span
                  className="w-3 h-3 rounded-full flex-shrink-0"
                  style={{ backgroundColor: list.color }}
                />
                <span className="text-[13px] flex-1 truncate">{list.name}</span>
                <span className="text-[13px]" style={{ color: "#86868B" }}>
                  {list.reminderCount > 0 ? list.reminderCount : ""}
                </span>
              </li>
            ))}
          </ul>

          {isAdding && (
            <div className="px-2 mt-1">
              <input
                autoFocus
                className="w-full px-2 py-1 text-[13px] rounded border outline-none"
                style={{ borderColor: "rgba(0,0,0,0.1)" }}
                placeholder="리스트 이름"
                value={newListName}
                onChange={(e) => setNewListName(e.target.value)}
                onKeyDown={(e) => {
                  if (e.key === "Enter") handleAddList();
                  if (e.key === "Escape") { setIsAdding(false); setNewListName(""); }
                }}
                onBlur={handleAddList}
              />
            </div>
          )}
        </div>
      </div>

      <div className="p-3 border-t" style={{ borderColor: "rgba(0,0,0,0.1)" }}>
        <button
          onClick={() => setIsAdding(true)}
          className="flex items-center gap-1 text-[13px] font-medium cursor-pointer"
          style={{ color: "#007AFF" }}
        >
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
            <path d="M8 3v10M3 8h10" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
          </svg>
          리스트 추가
        </button>
      </div>
    </aside>
  );
}

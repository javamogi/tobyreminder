"use client";

import useSWR from "swr";
import { api } from "@/lib/api";
import { SmartListType } from "@/store/useStore";

interface SmartListCounts {
  today: number;
  scheduled: number;
  all: number;
  flagged: number;
  completed: number;
}

const smartLists: { type: SmartListType; label: string; icon: string; color: string }[] = [
  { type: "today", label: "오늘", icon: "📅", color: "#007AFF" },
  { type: "scheduled", label: "예정", icon: "📋", color: "#FF3B30" },
  { type: "all", label: "전체", icon: "📁", color: "#8E8E93" },
  { type: "flagged", label: "플래그", icon: "🚩", color: "#FF9500" },
  { type: "completed", label: "완료됨", icon: "✅", color: "#8E8E93" },
];

interface Props {
  selectedType: SmartListType | null;
  onSelect: (type: SmartListType) => void;
}

export function SmartListGrid({ selectedType, onSelect }: Props) {
  const { data: counts } = useSWR<SmartListCounts>("/api/smart/counts", () => api.smart.counts(), {
    onError: () => {},
  });

  return (
    <div className="grid grid-cols-2 gap-2.5">
      {smartLists.map((item) => (
        <button
          key={item.type}
          onClick={() => onSelect(item.type)}
          className="flex flex-col p-3 rounded-xl text-left cursor-pointer transition-colors duration-100"
          style={{
            backgroundColor: selectedType === item.type ? "rgba(0,122,255,0.12)" : "rgba(255,255,255,0.8)",
            boxShadow: "0 1px 3px rgba(0,0,0,0.08)",
          }}
          onMouseEnter={(e) => {
            if (selectedType !== item.type)
              e.currentTarget.style.backgroundColor = "rgba(0,0,0,0.04)";
          }}
          onMouseLeave={(e) => {
            e.currentTarget.style.backgroundColor =
              selectedType === item.type ? "rgba(0,122,255,0.12)" : "rgba(255,255,255,0.8)";
          }}
        >
          <div className="flex items-center justify-between w-full">
            <span
              className="w-7 h-7 rounded-full flex items-center justify-center text-sm"
              style={{ backgroundColor: item.color }}
            >
              <span className="text-white text-xs">{item.icon}</span>
            </span>
            <span className="text-[24px] font-bold" style={{ color: "#1D1D1F" }}>
              {counts?.[item.type] ?? 0}
            </span>
          </div>
          <span className="text-[11px] font-medium mt-1" style={{ color: "#86868B" }}>
            {item.label}
          </span>
        </button>
      ))}
    </div>
  );
}

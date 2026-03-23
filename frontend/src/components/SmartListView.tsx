"use client";

import { SmartListType } from "@/store/useStore";

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
  return (
    <div className="flex-1 flex flex-col h-full overflow-hidden">
      <div className="px-6 pt-6 pb-2">
        <h1 className="text-[28px] font-bold" style={{ color: smartListColors[type] }}>
          {smartListLabels[type]}
        </h1>
      </div>
      <div className="flex-1 flex items-center justify-center">
        <p className="text-[15px]" style={{ color: "#AEAEB2" }}>
          스마트 리스트 기능은 Phase 4에서 구현됩니다
        </p>
      </div>
    </div>
  );
}

"use client";

import useSWR from "swr";
import { api } from "@/lib/api";
import { useStore } from "@/store/useStore";
import { ReminderList as ReminderListType } from "@/types";
import { ReminderListView } from "./ReminderListView";
import { SmartListView } from "./SmartListView";
import { SearchResultView } from "./SearchResultView";

export function ContentArea() {
  const { selectedListId, viewType, smartListType, searchQuery } = useStore();
  const { data: lists } = useSWR<ReminderListType[]>("/api/lists", () => api.lists.getAll());

  if (viewType === "search" && searchQuery) {
    return <SearchResultView query={searchQuery} />;
  }

  if (viewType === "smart" && smartListType) {
    return <SmartListView type={smartListType} />;
  }

  if (selectedListId) {
    const list = lists?.find((l) => l.id === selectedListId);
    if (list) {
      return <ReminderListView list={list} />;
    }
  }

  return (
    <div className="flex-1 flex items-center justify-center">
      <p className="text-[15px]" style={{ color: "#AEAEB2" }}>
        리스트를 선택하세요
      </p>
    </div>
  );
}

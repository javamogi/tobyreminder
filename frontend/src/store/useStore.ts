"use client";

import { create } from "zustand";

export type ViewType = "list" | "smart" | "search";
export type SmartListType = "today" | "scheduled" | "all" | "flagged" | "completed";

interface AppState {
  selectedListId: number | null;
  viewType: ViewType;
  smartListType: SmartListType | null;
  searchQuery: string;
  editingReminderId: number | null;

  selectList: (id: number) => void;
  selectSmartList: (type: SmartListType) => void;
  setSearchQuery: (query: string) => void;
  setEditingReminderId: (id: number | null) => void;
  clearSelection: () => void;
}

export const useStore = create<AppState>((set) => ({
  selectedListId: null,
  viewType: "list",
  smartListType: null,
  searchQuery: "",
  editingReminderId: null,

  selectList: (id) =>
    set({ selectedListId: id, viewType: "list", smartListType: null, searchQuery: "", editingReminderId: null }),

  selectSmartList: (type) =>
    set({ selectedListId: null, viewType: "smart", smartListType: type, searchQuery: "", editingReminderId: null }),

  setSearchQuery: (query) =>
    set({
      searchQuery: query,
      viewType: query ? "search" : "list",
      ...(query ? { selectedListId: null, smartListType: null } : {}),
    }),

  setEditingReminderId: (id) => set({ editingReminderId: id }),

  clearSelection: () =>
    set({ selectedListId: null, viewType: "list", smartListType: null, searchQuery: "", editingReminderId: null }),
}));

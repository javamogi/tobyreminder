export interface ReminderList {
  id: number;
  name: string;
  color: string;
  icon: string;
  displayOrder: number;
  reminderCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface Reminder {
  id: number;
  listId: number;
  title: string;
  notes: string | null;
  completed: boolean;
  completedAt: string | null;
  dueDate: string | null;
  dueTime: string | null;
  priority: Priority;
  flagged: boolean;
  displayOrder: number;
  createdAt: string;
  updatedAt: string;
}

export type Priority = "NONE" | "LOW" | "MEDIUM" | "HIGH";

export interface ReminderListRequest {
  name: string;
  color?: string;
  icon?: string;
}

export interface ReminderRequest {
  title: string;
  notes?: string;
  dueDate?: string | null;
  dueTime?: string | null;
  priority?: Priority;
  flagged?: boolean;
}

"use client";

import { SWRConfig } from "swr";
import { swrConfig } from "@/lib/swr";
import { Sidebar } from "@/components/Sidebar";
import { ContentArea } from "@/components/ContentArea";

export default function Home() {
  return (
    <SWRConfig value={swrConfig}>
      <div className="flex h-full">
        <Sidebar />
        <ContentArea />
      </div>
    </SWRConfig>
  );
}

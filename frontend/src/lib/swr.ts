"use client";

import { SWRConfig } from "swr";
import { fetcher } from "./api";

export const swrConfig = {
  fetcher,
  revalidateOnFocus: true,
  dedupingInterval: 2000,
};

export { SWRConfig };

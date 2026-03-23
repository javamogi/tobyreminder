"use client";

interface Props {
  query: string;
}

export function SearchResultView({ query }: Props) {
  return (
    <div className="flex-1 flex flex-col h-full overflow-hidden">
      <div className="px-6 pt-6 pb-2">
        <h1 className="text-[28px] font-bold" style={{ color: "#1D1D1F" }}>
          검색: {query}
        </h1>
      </div>
      <div className="flex-1 flex items-center justify-center">
        <p className="text-[15px]" style={{ color: "#AEAEB2" }}>
          검색 기능은 Phase 5에서 구현됩니다
        </p>
      </div>
    </div>
  );
}

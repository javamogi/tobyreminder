# Apple Reminders Web - Tasks

> 총 **88개** 작업 | 완료: 0/88

---

## Phase 1 — 백엔드 기본 CRUD API

### 1.1 프로젝트 설정
- [ ] `application.properties` 설정 (H2 인메모리, JPA ddl-auto, H2 Console 활성화)
- [ ] CORS 설정 클래스 (`http://localhost:3000` 허용)
- [ ] JPA Auditing 활성화 (`@EnableJpaAuditing`, `@CreatedDate`, `@LastModifiedDate`)

### 1.2 엔티티 & 리포지토리
- [ ] `Priority` enum 생성 (NONE, LOW, MEDIUM, HIGH)
- [ ] `ReminderList` 엔티티 (id, name, color, icon, displayOrder, createdAt, updatedAt)
- [ ] `Reminder` 엔티티 (id, title, notes, isCompleted, completedAt, displayOrder, createdAt, updatedAt)
- [ ] `ReminderList` ↔ `Reminder` 연관관계 매핑 (OneToMany / ManyToOne)
- [ ] `ReminderListRepository` (JpaRepository)
- [ ] `ReminderRepository` (JpaRepository)

### 1.3 DTO
- [ ] `ReminderListRequest` (생성/수정 요청)
- [ ] `ReminderListResponse` (응답 — 리마인더 카운트 포함)
- [ ] `ReminderRequest` (생성/수정 요청)
- [ ] `ReminderResponse` (응답)

### 1.4 리스트 API
- [ ] `ReminderListService` 서비스 클래스
- [ ] `POST /api/lists` — 리스트 생성
- [ ] `GET /api/lists` — 전체 리스트 조회 (미완료 리마인더 카운트 포함)
- [ ] `GET /api/lists/{id}` — 리스트 상세 조회
- [ ] `PUT /api/lists/{id}` — 리스트 수정 (이름, 색상, 아이콘)
- [ ] `DELETE /api/lists/{id}` — 리스트 삭제 (cascade 리마인더 삭제)

### 1.5 리마인더 API
- [ ] `ReminderService` 서비스 클래스
- [ ] `POST /api/lists/{listId}/reminders` — 리마인더 생성
- [ ] `GET /api/lists/{listId}/reminders` — 리스트 내 리마인더 조회
- [ ] `GET /api/reminders/{id}` — 리마인더 상세 조회
- [ ] `PUT /api/reminders/{id}` — 리마인더 수정
- [ ] `DELETE /api/reminders/{id}` — 리마인더 삭제
- [ ] `PATCH /api/reminders/{id}/complete` — 완료 토글

### 1.6 예외 처리
- [ ] 글로벌 예외 핸들러 (`@RestControllerAdvice`) — 404, 400 응답

---

## Phase 2 — 프론트엔드 초기 설정 + 기본 레이아웃

### 2.1 프로젝트 초기 설정
- [ ] `frontend/` 디렉토리에 Next.js 프로젝트 생성 (App Router, TypeScript)
- [ ] Tailwind CSS 설정 — Apple 색상 팔레트, 폰트 스택 커스텀
- [ ] `next.config.js` rewrites 설정 (API 프록시 → `localhost:8080`)
- [ ] API fetch wrapper 유틸리티 (base URL, 에러 처리)
- [ ] SWR Provider 설정
- [ ] TypeScript 타입 정의 (`ReminderList`, `Reminder`, API 응답)
- [ ] Zustand 스토어 (선택된 리스트 ID, UI 상태)

### 2.2 사이드바
- [ ] `Sidebar` 컴포넌트 — 280px 고정, `#F5F5F7` 배경
- [ ] "내 리스트" 섹션 헤더 (11px, Semibold, 대문자)
- [ ] 리스트 행 — 색상 원(●) + 이름 + 미완료 카운트
- [ ] 리스트 선택 → 하이라이트 (`rgba(0,122,255,0.12)`)
- [ ] "+ 리스트 추가" 버튼 → 인라인 입력 → API 호출로 생성

### 2.3 콘텐츠 영역
- [ ] 툴바 — 리스트 이름 (28px Bold, 리스트 색상)
- [ ] 리마인더 목록 — 체크박스(○) + 제목 + 인덴트 구분선
- [ ] 체크박스 클릭 → 완료 토글 API → 목록에서 제거
- [ ] "+ 리마인더 추가" — 하단 인라인 입력, Enter 생성, 연속 입력
- [ ] 빈 상태 ("리마인더 없음")

### 2.4 리마인더 인라인 편집
- [ ] 리마인더 행 hover 시 ⓘ 버튼 표시
- [ ] ⓘ 클릭 → 인라인 상세 편집 확장 (제목, 메모 수정)
- [ ] 리마인더 삭제 버튼 + 확인

---

## Phase 3 — 리마인더 상세 속성

### 3.1 Backend 엔티티 확장
- [ ] `Reminder`에 dueDate(LocalDate), dueTime(LocalTime), priority(Priority enum), isFlagged(Boolean) 추가
- [ ] DTO에 새 필드 반영
- [ ] `PATCH /api/reminders/{id}/flag` — 플래그 토글 API

### 3.2 인라인 상세 편집 확장
- [ ] 날짜 토글 (ON/OFF) + 캘린더 Date Picker
- [ ] 시간 토글 (ON/OFF) + Time Picker
- [ ] 우선순위 드롭다운 (없음 / 낮음 / 보통 / 높음)
- [ ] 플래그 토글 스위치 (iOS 스타일 Green/Gray)
- [ ] 리스트 이동 드롭다운

### 3.3 리마인더 행 메타 정보
- [ ] 메모 미리보기 (13px, Secondary, 1줄 truncate)
- [ ] 마감일 뱃지 (📅 + 날짜텍스트, overdue 빨간색)
- [ ] 우선순위 뱃지 (`!` / `!!` / `!!!`, Blue)
- [ ] 플래그 아이콘 (🚩, Orange)

---

## Phase 4 — 스마트 리스트

### 4.1 Backend API
- [ ] `GET /api/smart/today` — dueDate = 오늘, 미완료
- [ ] `GET /api/smart/scheduled` — dueDate 있음, 미완료, 날짜 오름차순
- [ ] `GET /api/smart/all` — 미완료 전체
- [ ] `GET /api/smart/flagged` — 플래그 + 미완료
- [ ] `GET /api/smart/completed` — 완료된 전체
- [ ] `GET /api/smart/counts` — 각 스마트 리스트 카운트 일괄 조회

### 4.2 사이드바 스마트 리스트 그리드
- [ ] 2열 그리드 카드 (오늘/예정/전체/플래그/완료)
- [ ] 카드: 원형 아이콘 + 카운트(Bold) + 라벨
- [ ] 카드 색상 — 오늘(Blue), 예정(Red), 전체(Gray), 플래그(Orange), 완료(Gray)

### 4.3 스마트 리스트 콘텐츠 뷰
- [ ] 오늘 뷰 — 리스트별 그룹핑
- [ ] 예정 뷰 — 날짜별 그룹핑 (overdue 상단 빨간색)
- [ ] 전체 뷰 — 리스트별 그룹핑
- [ ] 플래그 뷰 — 리스트별 그룹핑
- [ ] 완료 뷰 — 완료 날짜별 그룹핑

---

## Phase 5 — 검색 + 정렬 + 리스트 관리

### 5.1 Backend
- [ ] `GET /api/reminders/search?q={keyword}` — title/notes LIKE 검색
- [ ] 리마인더 조회 API 정렬 파라미터 (`sort=dueDate|createdAt|priority|title`)
- [ ] `PATCH /api/lists/reorder` — 리스트 순서 변경
- [ ] `PATCH /api/reminders/reorder` — 리마인더 순서 변경

### 5.2 검색 UI
- [ ] 사이드바 상단 검색 필드 (둥근 검색바, 돋보기 아이콘)
- [ ] debounce (300ms) → 검색 API 호출
- [ ] 콘텐츠 영역 검색 결과 목록 전환
- [ ] 검색 결과 없음 빈 상태

### 5.3 정렬 UI
- [ ] 툴바 `···` 메뉴 → 정렬 옵션 드롭다운
- [ ] 마감일 / 생성일 / 우선순위 / 제목 기준 정렬

### 5.4 리스트 관리 UI
- [ ] 리스트 이름/색상 수정 (색상 팔레트 팝오버)
- [ ] 리스트 삭제 확인 다이얼로그
- [ ] 리스트 우클릭 컨텍스트 메뉴

---

## Phase 6 — 하위 리마인더 + 반복

### 6.1 Backend
- [ ] `Reminder` 엔티티 parent self-referencing ManyToOne 추가
- [ ] children OneToMany 매핑
- [ ] `POST /api/reminders/{parentId}/subtasks` — 하위 리마인더 생성
- [ ] 부모 조회 시 하위 리마인더 포함 응답
- [ ] `recurrenceRule` 필드 추가 (DAILY/WEEKLY/MONTHLY/YEARLY)

### 6.2 하위 리마인더 UI
- [ ] 인라인 상세 편집 "하위 리마인더" 섹션
- [ ] 하위 리마인더 인라인 추가
- [ ] 하위 리마인더 목록 (체크박스 + 제목)
- [ ] 하위 리마인더 완료 토글
- [ ] 리마인더 행에 하위 카운트 표시 ("0/3")

### 6.3 반복 UI
- [ ] 인라인 상세 편집 반복 드롭다운 (안 함/매일/매주/매월/매년)
- [ ] 리마인더 행 반복 아이콘 (🔄)

---

## Phase 7 — 드래그 앤 드롭 + 애니메이션 + 키보드 단축키

### 7.1 드래그 앤 드롭
- [ ] 사이드바 리스트 DnD 순서 변경 → reorder API
- [ ] 리마인더 목록 DnD 순서 변경 → reorder API
- [ ] 드래그 시각 피드백 (scale 1.02, shadow, 드롭 위치선)

### 7.2 애니메이션
- [ ] 체크박스 완료 (draw 0.2s → fade out 0.4s)
- [ ] 리마인더 추가/삭제 높이 애니메이션
- [ ] 사이드바 hover/선택 배경색 전환
- [ ] 인라인 상세 확장/축소 애니메이션
- [ ] 토글 스위치 슬라이드 애니메이션

### 7.3 키보드 단축키
- [ ] `⌘ + N` — 새 리마인더 추가
- [ ] `Enter` — 생성 확인 / 연속 추가
- [ ] `Escape` — 편집 취소 / 팝오버 닫기
- [ ] `Delete / Backspace` — 선택 리마인더 삭제
- [ ] `⌘ + F` — 검색 포커스
- [ ] `↑ / ↓` — 리마인더 탐색
- [ ] `⌘ + .` — 플래그 토글

---

## Phase 8 — 다크 모드 + 반응형 + 마무리

### 8.1 다크 모드
- [ ] Tailwind dark mode 설정 (`class` 전략)
- [ ] 전체 컴포넌트 `dark:` variant 적용 (spec.md 색상 팔레트)
- [ ] 시스템 설정 감지 (`prefers-color-scheme`) + 수동 토글

### 8.2 반응형 레이아웃
- [ ] Desktop (≥1024px) — 사이드바 + 콘텐츠
- [ ] Tablet (768~1023px) — 사이드바 오버레이, 상세는 모달
- [ ] Mobile (<768px) — 네비게이션 스택

### 8.3 완성도
- [ ] 사이드바 드래그 리사이즈 (220px ~ 400px)
- [ ] 컨텍스트 메뉴 (우클릭) — 리스트/리마인더
- [ ] 에러 처리 UI (토스트 알림)
- [ ] 빈 상태 UI 다듬기
- [ ] 로딩 스켈레톤 (사이드바, 리마인더 목록)
- [ ] favicon, 페이지 타이틀 설정

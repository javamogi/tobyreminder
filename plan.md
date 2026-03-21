# Apple Reminders Web - 개발 계획

> 단순한 기능부터 시작하여 점진적으로 기능을 추가하는 방식으로 진행한다.
> 각 Phase는 이전 Phase가 완료된 상태에서 동작하는 완성된 애플리케이션을 결과물로 갖는다.

---

## 기술 스택 요약

| 구분 | 기술 | 버전/비고 |
|------|------|----------|
| **Backend** | Spring Boot | 4.0.4 |
| **Language** | Java | 25 |
| **ORM** | Spring Data JPA | Hibernate |
| **Database** | H2 | 인메모리 (개발/데모), 파일 모드 전환 가능 |
| **Build** | Gradle | Kotlin DSL |
| **Frontend** | Next.js | Latest (App Router) |
| **Language** | TypeScript | Strict mode |
| **Styling** | Tailwind CSS | Apple 디자인 커스텀 |
| **상태관리** | Zustand | 경량, 리스트/리마인더 스토어 |
| **API 통신** | SWR | 캐싱, 자동 revalidation |
| **Monorepo** | 단일 Git repo | `backend/` (Spring Boot) + `frontend/` (Next.js) |

---

## Phase 1 — 백엔드 기본 CRUD API

> 리스트와 리마인더의 기본 CRUD를 백엔드에서 완성한다.
> Frontend 없이 API만으로 동작을 검증한다.

### 기술 포인트
- Spring Data JPA 엔티티 + Repository 패턴
- REST Controller + DTO (요청/응답 분리)
- H2 Console 활성화하여 데이터 확인
- Bean Validation (`@NotBlank`, `@NotNull`) 적용
- 글로벌 예외 처리 (`@RestControllerAdvice`)
- CORS 설정 (Frontend 연동 대비)

### 작업 목록

**엔티티 & 리포지토리**
- [ ] `ReminderList` 엔티티 — id, name, color, icon, displayOrder, createdAt, updatedAt
- [ ] `Reminder` 엔티티 — id, title, notes, isCompleted, completedAt, displayOrder, createdAt, updatedAt
- [ ] `ReminderList` ↔ `Reminder` 연관관계 (OneToMany / ManyToOne)
- [ ] `ReminderListRepository`, `ReminderRepository` (JpaRepository)
- [ ] Auditing 설정 (`@CreatedDate`, `@LastModifiedDate`)

**리스트 API**
- [ ] `POST /api/lists` — 리스트 생성
- [ ] `GET /api/lists` — 전체 리스트 조회 (리마인더 카운트 포함)
- [ ] `GET /api/lists/{id}` — 리스트 상세 조회
- [ ] `PUT /api/lists/{id}` — 리스트 수정 (이름, 색상, 아이콘)
- [ ] `DELETE /api/lists/{id}` — 리스트 삭제 (cascade 리마인더 삭제)

**리마인더 API**
- [ ] `POST /api/lists/{listId}/reminders` — 리마인더 생성 (제목 필수)
- [ ] `GET /api/lists/{listId}/reminders` — 리스트 내 리마인더 조회
- [ ] `GET /api/reminders/{id}` — 리마인더 상세 조회
- [ ] `PUT /api/reminders/{id}` — 리마인더 수정
- [ ] `DELETE /api/reminders/{id}` — 리마인더 삭제
- [ ] `PATCH /api/reminders/{id}/complete` — 완료 토글

**공통**
- [ ] 요청/응답 DTO 클래스
- [ ] 글로벌 예외 핸들러 (404 Not Found, 400 Bad Request)
- [ ] CORS 설정 (`http://localhost:3000` 허용)
- [ ] H2 Console 설정 (`/h2-console`)

---

## Phase 2 — 프론트엔드 초기 설정 + 기본 레이아웃

> Next.js 프로젝트를 생성하고 Apple Reminders 스타일의 사이드바 + 콘텐츠 레이아웃을 구현한다.
> Phase 1의 API와 연동하여 리스트/리마인더 기본 CRUD가 동작하는 화면을 만든다.

### 기술 포인트
- Next.js App Router (`app/` 디렉토리)
- TypeScript strict mode
- Tailwind CSS + Apple 디자인 토큰 (색상, 폰트, 간격) 설정
- Zustand 스토어 (선택된 리스트, UI 상태)
- SWR로 API 호출 + 캐싱
- API Proxy: `next.config.js`의 `rewrites`로 백엔드 프록시

### 작업 목록

**프로젝트 초기 설정**
- [ ] `frontend/` 디렉토리에 Next.js 프로젝트 생성 (`create-next-app`)
- [ ] Tailwind CSS 설정 — Apple 색상 팔레트, 폰트 스택 커스텀
- [ ] API 클라이언트 유틸리티 (base URL, fetch wrapper)
- [ ] SWR Provider 설정
- [ ] TypeScript 타입 정의 (`ReminderList`, `Reminder`)

**사이드바 (Sidebar)**
- [ ] 사이드바 컴포넌트 — 280px 고정 너비, `#F5F5F7` 배경
- [ ] "내 리스트" 섹션 — API에서 리스트 목록 조회하여 표시
- [ ] 리스트 행: 색상 원(●) + 이름 + 미완료 리마인더 카운트
- [ ] 리스트 선택 → 하이라이트 + 콘텐츠 영역 전환
- [ ] "+ 리스트 추가" 버튼 → 인라인 입력 필드 → 리스트 생성 API 호출

**콘텐츠 영역 (Content Area)**
- [ ] 툴바: 선택된 리스트 이름 (볼드, 리스트 색상)
- [ ] 리마인더 목록 표시 — 체크박스(○) + 제목
- [ ] 리마인더 완료 토글 — 체크박스 클릭 → API 호출 → 목록에서 제거
- [ ] "+ 리마인더 추가" — 하단 인라인 입력, Enter로 생성
- [ ] 빈 상태 표시 ("리마인더 없음")

**리마인더 인라인 편집**
- [ ] 리마인더 클릭 시 ⓘ 버튼 표시
- [ ] ⓘ 클릭 → 인라인 상세 편집 영역 확장 (제목, 메모 편집)
- [ ] 리마인더 삭제 기능

---

## Phase 3 — 리마인더 상세 속성 (마감일, 우선순위, 플래그)

> 리마인더에 마감일, 우선순위, 플래그 속성을 추가한다.
> 인라인 상세 편집 패널을 확장하여 모든 속성을 편집할 수 있도록 한다.

### 기술 포인트
- Backend: `Reminder` 엔티티에 `dueDate`, `dueTime`, `priority`, `isFlagged` 필드 추가
- `Priority` enum (NONE, LOW, MEDIUM, HIGH)
- Frontend: Date Picker, 드롭다운, 토글 스위치 컴포넌트

### 작업 목록

**Backend**
- [ ] `Reminder` 엔티티 확장 — dueDate(LocalDate), dueTime(LocalTime), priority(Enum), isFlagged(Boolean)
- [ ] `PATCH /api/reminders/{id}/flag` — 플래그 토글 API
- [ ] 리마인더 수정 API에 새 필드 반영

**Frontend — 인라인 상세 편집 확장**
- [ ] 날짜 토글 + 캘린더 Date Picker
- [ ] 시간 토글 + Time Picker
- [ ] 우선순위 드롭다운 (없음 / 낮음 / 보통 / 높음)
- [ ] 플래그 토글 스위치 (iOS 스타일)
- [ ] 리스트 이동 드롭다운 (소속 리스트 변경)

**Frontend — 리마인더 행 메타 정보 표시**
- [ ] 메모 미리보기 (1줄, 회색 작은 텍스트)
- [ ] 마감일 뱃지 (📅 + 날짜, 지난 날짜는 빨간색)
- [ ] 우선순위 뱃지 (`!` / `!!` / `!!!`)
- [ ] 플래그 아이콘 (🚩)

---

## Phase 4 — 스마트 리스트

> 오늘, 예정, 전체, 플래그, 완료 스마트 리스트를 구현한다.
> 사이드바에 스마트 리스트 그리드 카드를 추가한다.

### 기술 포인트
- Backend: JPA 쿼리 메서드 / `@Query`로 필터링 조건 구현
- 날짜 비교: `LocalDate.now()` 기준
- Frontend: 스마트 리스트별 그룹핑 로직 (리스트별, 날짜별)

### 작업 목록

**Backend — 스마트 리스트 API**
- [ ] `GET /api/smart/today` — `dueDate = today AND isCompleted = false`
- [ ] `GET /api/smart/scheduled` — `dueDate IS NOT NULL AND isCompleted = false` (날짜 오름차순)
- [ ] `GET /api/smart/all` — `isCompleted = false`
- [ ] `GET /api/smart/flagged` — `isFlagged = true AND isCompleted = false`
- [ ] `GET /api/smart/completed` — `isCompleted = true`
- [ ] 각 스마트 리스트 카운트 조회 API — `GET /api/smart/counts`

**Frontend — 사이드바 스마트 리스트 그리드**
- [ ] 2열 그리드 카드 레이아웃 (오늘, 예정, 전체, 플래그, 완료)
- [ ] 각 카드: 원형 아이콘 + 카운트 (볼드) + 라벨
- [ ] 카드 색상: 오늘(Blue), 예정(Red), 전체(Gray), 플래그(Orange), 완료(Gray)

**Frontend — 스마트 리스트별 콘텐츠 뷰**
- [ ] 오늘: 리스트별 그룹핑 (섹션 헤더 = 리스트 이름 + 색상)
- [ ] 예정: 날짜별 그룹핑 (overdue 빨간색 상단 표시)
- [ ] 전체: 리스트별 그룹핑
- [ ] 플래그: 리스트별 그룹핑
- [ ] 완료: 완료 날짜별 그룹핑

---

## Phase 5 — 검색 + 정렬 + 리스트 관리 강화

> 검색 기능과 정렬 옵션을 추가하고, 리스트 편집(색상, 아이콘) 기능을 보강한다.

### 기술 포인트
- Backend: JPA `LIKE` 쿼리로 제목/메모 검색
- Frontend: debounce 적용한 실시간 검색
- 정렬: Backend 쿼리 파라미터 (`?sort=dueDate,asc`) 또는 Frontend 정렬

### 작업 목록

**Backend**
- [ ] `GET /api/reminders/search?q={keyword}` — title, notes LIKE 검색
- [ ] 리마인더 조회 API에 정렬 파라미터 지원 (`sort=dueDate|createdAt|priority|title`)
- [ ] `PATCH /api/lists/reorder` — 리스트 순서 변경
- [ ] `PATCH /api/reminders/reorder` — 리마인더 순서 변경

**Frontend — 검색**
- [ ] 사이드바 상단 검색 필드 (Apple 스타일 둥근 검색바)
- [ ] 입력 시 debounce (300ms) → 검색 API 호출
- [ ] 콘텐츠 영역이 검색 결과 목록으로 전환
- [ ] 검색 결과 없음 빈 상태

**Frontend — 정렬**
- [ ] 툴바 `···` 메뉴 → 정렬 옵션 드롭다운
- [ ] 마감일 / 생성일 / 우선순위 / 제목 기준 정렬

**Frontend — 리스트 관리**
- [ ] 리스트 수정: 이름, 색상 변경 (색상 팔레트 팝오버)
- [ ] 리스트 삭제 확인 다이얼로그
- [ ] 리스트 우클릭 컨텍스트 메뉴

---

## Phase 6 — 하위 리마인더 + 반복

> 하위 리마인더(subtask)와 반복 설정 기능을 추가한다.

### 기술 포인트
- Backend: `Reminder` self-referencing 관계 (`parent_id` FK)
- 하위 리마인더 조회: `@Query` 또는 엔티티 그래프
- 반복 규칙: `recurrenceRule` 문자열 (DAILY / WEEKLY / MONTHLY / YEARLY)

### 작업 목록

**Backend**
- [ ] `Reminder` 엔티티에 `parent` self-referencing ManyToOne 관계 추가
- [ ] `children` OneToMany 매핑
- [ ] 하위 리마인더 생성 API — `POST /api/reminders/{parentId}/subtasks`
- [ ] 부모 리마인더 조회 시 하위 리마인더 포함
- [ ] `recurrenceRule` 필드 추가 및 수정 API 반영

**Frontend — 하위 리마인더**
- [ ] 인라인 상세 편집에 "하위 리마인더" 섹션
- [ ] 하위 리마인더 추가 (인라인 입력)
- [ ] 하위 리마인더 목록 표시 (체크박스 + 제목)
- [ ] 하위 리마인더 완료 토글
- [ ] 리마인더 행에 하위 리마인더 카운트 표시 (예: "0/3")

**Frontend — 반복**
- [ ] 인라인 상세 편집에 반복 드롭다운 (안 함 / 매일 / 매주 / 매월 / 매년)
- [ ] 리마인더 행에 반복 아이콘(🔄) 표시

---

## Phase 7 — 드래그 앤 드롭 + 애니메이션 + 키보드 단축키

> 드래그 앤 드롭 정렬, Apple 스타일 애니메이션, 키보드 단축키를 추가한다.

### 기술 포인트
- 드래그 앤 드롭: `@dnd-kit/core` 또는 `react-beautiful-dnd` 대안
- 애니메이션: CSS transition + `framer-motion` (선택)
- 키보드 이벤트: `useEffect` + `keydown` 리스너

### 작업 목록

**드래그 앤 드롭**
- [ ] 사이드바 리스트 드래그 앤 드롭 순서 변경 → reorder API 호출
- [ ] 리마인더 목록 드래그 앤 드롭 순서 변경 → reorder API 호출
- [ ] 드래그 중 시각적 피드백 (scale 1.02, shadow 강화, 드롭 위치 표시선)

**애니메이션**
- [ ] 체크박스 완료 애니메이션 (체크마크 draw 0.2s → 행 fade out 0.4s)
- [ ] 리마인더 추가/삭제 시 높이 애니메이션
- [ ] 사이드바 hover/선택 배경색 전환 (0.15s ease)
- [ ] 인라인 상세 편집 확장/축소 애니메이션
- [ ] 토글 스위치 슬라이드 애니메이션

**키보드 단축키**
- [ ] `⌘ + N` — 새 리마인더 추가
- [ ] `Enter` — 리마인더 생성 확인 / 연속 추가
- [ ] `Escape` — 편집 취소 / 팝오버 닫기
- [ ] `Delete / Backspace` — 선택된 리마인더 삭제
- [ ] `⌘ + F` — 검색 필드 포커스
- [ ] `↑ / ↓` — 리마인더 목록 탐색
- [ ] `⌘ + .` — 플래그 토글

---

## Phase 8 — 다크 모드 + 반응형 + 마무리

> 다크 모드, 반응형 레이아웃, 최종 완성도를 높인다.

### 기술 포인트
- 다크 모드: Tailwind `dark:` variant + `prefers-color-scheme` 미디어 쿼리
- 반응형: Tailwind breakpoints (`sm`, `md`, `lg`)
- 사이드바 리사이즈: CSS resize 또는 드래그 핸들 구현

### 작업 목록

**다크 모드**
- [ ] Tailwind dark mode 설정 (`class` 전략)
- [ ] 모든 컴포넌트에 `dark:` variant 적용 (spec.md 6.5 색상 팔레트 기준)
- [ ] 시스템 설정 감지 (`prefers-color-scheme`) + 수동 토글

**반응형 레이아웃**
- [ ] Desktop (≥1024px): 사이드바 + 콘텐츠 (기본)
- [ ] Tablet (768px~1023px): 사이드바 오버레이 모드 (토글 버튼), 상세는 모달
- [ ] Mobile (<768px): 네비게이션 스택 (사이드바 → 리스트 → 상세)

**완성도**
- [ ] 사이드바 드래그 리사이즈 (최소 220px ~ 최대 400px)
- [ ] 컨텍스트 메뉴 (우클릭) — 리스트: 이름 변경/색상/삭제, 리마인더: 편집/마감일/플래그/삭제
- [ ] 에러 처리 UI (API 실패 시 토스트 알림)
- [ ] 빈 상태 UI 다듬기
- [ ] 로딩 스켈레톤 (사이드바, 리마인더 목록)
- [ ] `favicon`, 페이지 타이틀 설정

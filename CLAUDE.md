## 코딩 관례

### 패키지 구조
```
toby.ai.tobyreminder
├── domain/              # 엔티티
├── repository/          # JPA Repository
├── service/
│   ├── ports/in/        # Service 인터페이스
│   └── Default*.java    # Service 구현 클래스
├── controller/          # REST Controller
└── dto/                 # 요청/응답 DTO
```

### Service 계층
- 인터페이스는 `service/ports/in` 패키지에 정의
- 구현 클래스는 `service` 패키지에 `Default` 접두사 (예: `DefaultReminderListService`)
- `Impl` 접미사 사용 금지

### 테스트
- 기능 추가/수정 시 반드시 테스트 함께 작성
- 도메인 엔티티 테스트는 순수 단위 테스트
- ServiceTest는 `@SpringBootTest` + `@Transactional` 통합 테스트 — Mock 사용 금지

### 코드 스타일
- 불필요한 주석, docstring 추가 금지
- 간결하고 명확한 코드 우선ㅔ

### 참고 문서
- spec.md: 기능 명세
- plan.md: 개발 계획
- tasks.md: 구현 태스크 체크리스트

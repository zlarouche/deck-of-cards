<!-- 6f682bc7-e59e-4b61-b097-0b2ffea57b85 27958be7-9077-48a3-86ab-85f83577acc4 -->
# Deck of Cards Game - Senior Developer Solution

## Architecture Overview

**Backend**: Spring Boot REST API (Java 17+)

**Frontend**: React + TypeScript

**Structure**: Monorepo with separate backend/frontend modules

**Build Tools**: Maven (backend), npm/pnpm (frontend)

**Testing**: JUnit 5 (backend), Jest (frontend)

**Documentation**: Swagger/OpenAPI, README

## Project Structure

```
deck-of-cards-game/
├── backend/                    # Spring Boot REST API
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/goto/cards/
│   │   │   │   ├── CardsApplication.java
│   │   │   │   ├── controller/        # REST controllers
│   │   │   │   ├── service/           # Business logic
│   │   │   │   ├── model/             # Domain models (Card, Deck, Game, Player)
│   │   │   │   ├── repository/        # In-memory data storage
│   │   │   │   ├── dto/               # Data Transfer Objects
│   │   │   │   ├── exception/         # Custom exceptions
│   │   │   │   └── util/              # Utilities (custom shuffle)
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/                      # Unit tests
│   ├── pom.xml
│   └── Dockerfile
├── frontend/                   # React + TypeScript
│   ├── src/
│   │   ├── components/        # React components
│   │   ├── services/          # API client
│   │   ├── types/             # TypeScript types
│   │   ├── hooks/             # Custom React hooks
│   │   └── App.tsx
│   ├── package.json
│   └── Dockerfile
├── docker-compose.yml          # Orchestration
└── README.md                   # Comprehensive documentation

```

## Implementation Plan

### Phase 1: Backend Core Domain Models

**Files to create:**

- `backend/src/main/java/com/goto/cards/model/Card.java` - Card entity (suit, faceValue, value)
- `backend/src/main/java/com/goto/cards/model/Deck.java` - Standard 52-card deck
- `backend/src/main/java/com/goto/cards/model/Game.java` - Game with shoe (multiple decks) and players
- `backend/src/main/java/com/goto/cards/model/Player.java` - Player with hand of cards
- `backend/src/main/java/com/goto/cards/model/enums/Suit.java` - Hearts, Spades, Clubs, Diamonds
- `backend/src/main/java/com/goto/cards/model/enums/FaceValue.java` - Ace, 2-10, Jack, Queen, King

**Design decisions:**

- Immutable Card objects
- Deck as a collection of Cards with factory method for standard deck
- Game manages shoe (List<Card>) and players (Map<String, Player>)
- Player identified by name (String)

### Phase 2: Custom Shuffle Implementation

**File:** `backend/src/main/java/com/goto/cards/util/ShuffleUtil.java`

**Algorithm:** Fisher-Yates shuffle

- Explain: Industry-standard unbiased shuffle algorithm
- O(n) time complexity
- Uses SecureRandom for better randomness (vs Random)
- No library shuffle dependencies

### Phase 3: Repository Layer (In-Memory)

**Files:**

- `backend/src/main/java/com/goto/cards/repository/GameRepository.java`
- `backend/src/main/java/com/goto/cards/repository/DeckRepository.java`

**Design:**

- In-memory storage (ConcurrentHashMap) for games and decks
- Thread-safe for concurrent access
- Simple IDs (UUID) for games and decks

### Phase 4: Service Layer (Business Logic)

**Files:**

- `backend/src/main/java/com/goto/cards/service/GameService.java`
- `backend/src/main/java/com/goto/cards/service/DeckService.java`

**Operations:**

- createGame() / deleteGame()
- createDeck() 
- addDeckToGame(gameId, deckId)
- addPlayer() / removePlayer()
- dealCards(playerName, count)
- getPlayerCards(playerName)
- getPlayersSorted() - sorted by hand value descending
- getUndealtCardsBySuit()
- getUndealtCardsCount() - sorted by suit and value
- shuffleGameDeck(gameId)

**Validation:**

- Deck cannot be removed once added
- Cannot deal more cards than available
- Player must exist in game
- Proper error messages

### Phase 5: REST API Controllers

**Files:**

- `backend/src/main/java/com/goto/cards/controller/GameController.java`
- `backend/src/main/java/com/goto/cards/controller/DeckController.java`

**API Design:**

```
POST   /api/games                    # Create game
DELETE /api/games/{gameId}           # Delete game
POST   /api/decks                    # Create deck
POST   /api/games/{gameId}/decks     # Add deck to game
POST   /api/games/{gameId}/players   # Add player
DELETE /api/games/{gameId}/players/{playerName}  # Remove player
POST   /api/games/{gameId}/deal      # Deal cards (body: playerName, count)
GET    /api/games/{gameId}/players/{playerName}/cards  # Get player cards
GET    /api/games/{gameId}/players   # Get players sorted by hand value
GET    /api/games/{gameId}/undealt/suits  # Get undealt cards by suit
GET    /api/games/{gameId}/undealt/cards  # Get undealt cards count
POST   /api/games/{gameId}/shuffle   # Shuffle game deck
```

**Features:**

- Proper HTTP status codes (200, 201, 404, 400)
- DTOs for request/response
- Exception handling with @ControllerAdvice
- Swagger/OpenAPI documentation

### Phase 6: Error Handling

**Files:**

- `backend/src/main/java/com/goto/cards/exception/GameNotFoundException.java`
- `backend/src/main/java/com/goto/cards/exception/PlayerNotFoundException.java`
- `backend/src/main/java/com/goto/cards/exception/GlobalExceptionHandler.java`

**Design:**

- Custom exceptions for business logic errors
- Consistent error response format
- Proper HTTP status codes

### Phase 7: Testing

**Files:**

- Unit tests for services (GameServiceTest, DeckServiceTest)
- Unit tests for shuffle algorithm
- Integration tests for controllers (MockMvc)
- Test edge cases (empty deck, invalid operations)

### Phase 8: Frontend - TypeScript Types & API Client

**Files:**

- `frontend/src/types/index.ts` - TypeScript interfaces matching backend DTOs
- `frontend/src/services/api.ts` - Axios-based API client with typed methods

### Phase 9: Frontend - React Components

**Files:**

- `frontend/src/components/GameManager.tsx` - Game creation/deletion
- `frontend/src/components/DeckManager.tsx` - Deck creation
- `frontend/src/components/PlayerManager.tsx` - Add/remove players
- `frontend/src/components/CardDealer.tsx` - Deal cards interface
- `frontend/src/components/PlayerHand.tsx` - Display player's cards
- `frontend/src/components/PlayersLeaderboard.tsx` - Sorted players by hand value
- `frontend/src/components/UndealtCardsView.tsx` - Undealt cards statistics
- `frontend/src/components/ShuffleButton.tsx` - Shuffle game deck
- `frontend/src/App.tsx` - Main app orchestrating all components

**UI Framework:** Material-UI for professional, responsive design

**State Management:**

- React Context API for game state
- Custom hooks for API calls
- Optimistic UI updates where appropriate

### Phase 10: Docker & Deployment

**Files:**

- `backend/Dockerfile` - Multi-stage build for Spring Boot
- `frontend/Dockerfile` - Multi-stage build for React
- `docker-compose.yml` - Orchestrate backend and frontend
- `.dockerignore` files

### Phase 11: Documentation

**Files:**

- `README.md` - Setup instructions, architecture overview, API documentation
- Swagger UI at `/swagger-ui.html`
- Code comments for complex logic
- JavaDoc for public APIs

## Key Design Decisions to Explain in Interview

1. **Spring Boot**: Enterprise-grade, production-ready, extensive ecosystem
2. **Fisher-Yates Shuffle**: Unbiased, O(n), well-understood algorithm
3. **In-Memory Storage**: Appropriate for assignment scope, easy to extend to database
4. **RESTful Design**: Resource-based URLs, proper HTTP verbs, stateless
5. **DTOs**: Separation of domain models from API contracts
6. **Service Layer**: Business logic separation, testable, maintainable
7. **React + TypeScript**: Type safety, modern tooling, industry standard
8. **Material-UI**: Professional UI with minimal custom CSS
9. **Docker**: Reproducible deployments, easy setup
10. **Testing Strategy**: Unit tests for business logic, integration tests for API

## Tradeoffs Made

1. **In-Memory vs Database**: Chose in-memory for simplicity, but structure allows easy migration to JPA/DB
2. **No Authentication**: Assignment doesn't require it, but structure supports adding Spring Security
3. **Monorepo vs Separate Repos**: Monorepo simplifies development and deployment for this scope
4. **Material-UI vs Custom CSS**: Faster development, professional look, but adds bundle size

## Time Estimate

- Backend: ~4-5 hours
- Frontend: ~3-4 hours  
- Testing: ~1-2 hours
- Docker & Documentation: ~1 hour
- **Total: ~9-12 hours**

### To-dos

- [x] Set up project structure: Maven Spring Boot backend and React TypeScript frontend with package.json
- [x] Implement domain models: Card, Deck, Game, Player with enums for Suit and FaceValue
- [x] Implement custom Fisher-Yates shuffle algorithm in ShuffleUtil
- [x] Create repository layer (in-memory) for games and decks with thread-safe storage
- [x] Implement service layer with all business logic operations (create game, deal cards, shuffle, etc.)
- [x] Create REST API controllers with proper endpoints, DTOs, and error handling
- [x] Add Swagger/OpenAPI documentation and exception handling with @ControllerAdvice
- [x] Write unit tests for services and integration tests for controllers
- [x] Create TypeScript types and API client service for frontend
- [x] Build React components for all operations (GameManager, DeckManager, PlayerManager, CardDealer, etc.)
- [x] Style frontend with Material-UI and ensure all operations have visual representation
- [x] Create Dockerfiles for backend and frontend, plus docker-compose.yml
- [x] Write comprehensive README with setup instructions, architecture explanation, and API documentation
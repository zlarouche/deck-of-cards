# Deck of Cards Game - Interview Study Guide

**Created for:** Senior Developer Technical Interview  
**Time Available:** Until Monday 8am submission + few days for review  
**Interview Duration:** 1 hour

---

## Table of Contents

1. [Quick Reference: Key Concepts](#quick-reference)
2. [Phase 1: Core Algorithms & Data Structures](#phase-1)
3. [Phase 2: Domain Models & Business Logic](#phase-2)
4. [Phase 3: API Design & Architecture](#phase-3)
5. [Phase 4: Testing Strategy](#phase-4)
6. [Phase 5: Frontend Architecture](#phase-5)
7. [Common Interview Questions](#interview-questions)
8. [Practice Explanations](#practice-explanations)
9. [Risk Areas & How to Handle Them](#risk-areas)
10. [Time Complexity Cheat Sheet](#time-complexity)

---

## Quick Reference: Key Concepts {#quick-reference}

### Technology Stack
- **Backend:** Spring Boot 3.2.0, Java 17, Maven
- **Frontend:** React 18, TypeScript, Material-UI
- **Storage:** In-memory (ConcurrentHashMap)
- **Testing:** JUnit 5, MockMvc
- **Documentation:** Swagger/OpenAPI

### Architecture Layers
```
Controller → Service → Repository → Model
     ↓         ↓          ↓
   DTOs    Business   In-Memory
           Logic      Storage
```

### Key Files Location
- **Shuffle Algorithm:** `backend/src/main/java/com/gotocompany/cards/util/ShuffleUtil.java`
- **Core Business Logic:** `backend/src/main/java/com/gotocompany/cards/service/GameService.java`
- **Domain Models:** `backend/src/main/java/com/gotocompany/cards/model/`
- **API Endpoints:** `backend/src/main/java/com/gotocompany/cards/controller/GameController.java`
- **Tests:** `backend/src/test/java/com/gotocompany/cards/`

---

## Phase 1: Core Algorithms & Data Structures {#phase-1}

### 1.1 Fisher-Yates Shuffle Algorithm

**File:** `ShuffleUtil.java`

**Why Fisher-Yates?**
- Industry-standard unbiased shuffle algorithm
- Guarantees each permutation has equal probability (1/n!)
- O(n) time complexity - optimal for shuffling
- Requirements explicitly stated: "do not use library-provided shuffle"

**How It Works:**
```java
for (int i = cards.size() - 1; i > 0; i--) {
    int j = random.nextInt(i + 1);  // Random from 0 to i (inclusive)
    // Swap cards at positions i and j
    Card temp = cards.get(i);
    cards.set(i, cards.get(j));
    cards.set(j, temp);
}
```

**Step-by-Step Explanation:**
1. Start from the last element (index n-1)
2. Pick a random index j from 0 to i (inclusive)
3. Swap elements at positions i and j
4. Move to previous element (i-1) and repeat
5. Continue until i = 1 (first element)

**Example with 5 cards [A, B, C, D, E]:**
- i=4: pick j (0-4), swap with position 4
- i=3: pick j (0-3), swap with position 3
- i=2: pick j (0-2), swap with position 2
- i=1: pick j (0-1), swap with position 1
- Done! Each card had equal chance to end up anywhere

**Why SecureRandom vs Random?**
- `SecureRandom` provides cryptographic-quality randomness
- Better for fairness in card games where predictability is a concern
- More suitable for production systems requiring high-quality randomness

**Practice Exercise:**
Try shuffling [1, 2, 3] by hand using Fisher-Yates. List all possible outcomes.

### 1.2 Data Structure Choices

**ConcurrentHashMap (GameRepository, DeckRepository)**
- **Why?** Thread-safe for concurrent access
- **Trade-off:** In-memory storage (data lost on restart)
- **Alternative:** Could use database (JPA/Hibernate) for persistence

**ArrayList for Shoe (List<Card>)**
- **Why?** 
  - Order matters (cards dealt from front)
  - Allows duplicates (multiple decks)
  - O(1) access by index
  - Efficient removal from front (index 0)
- **Dealing:** `shoe.remove(0)` removes from front (top of deck)

**HashMap for Players (Map<String, Player>)**
- **Why?** O(1) lookup by player name
- **Key:** Player name (String)
- **Value:** Player object

**LinkedHashMap for Card Counts**
- **Why?** Preserves insertion order
- Used in `getUndealtCardsCount()` to maintain suit order (Hearts, Spades, Clubs, Diamonds)
- Maintains face value order (King → Ace)

---

## Phase 2: Domain Models & Business Logic {#phase-2}

### 2.1 Domain Model Relationships

```
Game
├── id: String (UUID)
├── shoe: List<Card> (game deck from all added decks)
├── players: Map<String, Player>
└── addedDeckIds: Set<String> (track which decks added)

Deck
├── id: String (UUID)
└── cards: List<Card> (52 cards, standard deck)

Player
├── name: String
└── hand: List<Card>

Card
├── suit: Suit (HEARTS, SPADES, CLUBS, DIAMONDS)
├── faceValue: FaceValue (ACE, 2-10, JACK, QUEEN, KING)
└── value: int (for scoring: Ace=1, 2-10=face, Jack=11, Queen=12, King=13)
```

### 2.2 Card Flow Through System

```
1. Deck created → 52 cards generated
2. Deck added to Game → Cards copied to Game.shoe
3. Game.shuffle() → Cards in shoe randomized
4. dealCards() → Cards removed from shoe (front), added to Player.hand
```

**Important:** Once deck added to game, it cannot be removed (per requirements).

### 2.3 Key Business Logic Methods

#### `dealCards(String gameId, String playerName, int count)`

**Location:** `GameService.java` → calls `Game.dealCards()`

**Logic:**
1. Validate player exists
2. Validate count > 0
3. Validate enough cards available (count <= shoe.size())
4. Remove cards from front of shoe: `shoe.remove(0)`
5. Add cards to player's hand
6. Return list of dealt cards

**Edge Cases:**
- If shoe becomes empty mid-deal, stop and return cards dealt so far
- If count > available cards, throw `IllegalStateException`

**Why remove from front (index 0)?**
- Simulates dealing from top of physical deck
- After shuffle, front cards are randomly positioned
- Standard card game behavior

#### `getPlayersSorted(String gameId)`

**Logic:**
```java
return game.getPlayers().stream()
    .sorted(Comparator.comparing(Player::getHandValue).reversed())
    .collect(Collectors.toList());
```

**What it does:**
1. Get all players from game
2. Sort by `handValue` (total of all card values)
3. Reverse order (descending: highest first)
4. Return sorted list

**Time Complexity:** O(n log n) where n = number of players

**Hand Value Calculation:**
- Each card has a value (Ace=1, 2-10=face, Jack=11, Queen=12, King=13)
- Sum all card values in player's hand
- Example: 10 + King = 10 + 13 = 23

#### `getUndealtCardsBySuit(String gameId)`

**Returns:** `Map<Suit, Integer>` - count of undealt cards per suit

**Logic:**
1. Get all cards in shoe (undealt)
2. Iterate through cards
3. Count by suit
4. Return map with all suits (even if count = 0)

#### `getUndealtCardsCount(String gameId)`

**Returns:** `Map<Suit, Map<FaceValue, Integer>>` - detailed count by suit and face value

**Sorting:**
- Suits: Hearts, Spades, Clubs, Diamonds (enum order)
- Face Values: King, Queen, Jack, 10, 9, 8, 7, 6, 5, 4, 3, 2, Ace
- Uses `LinkedHashMap` to preserve order

**Why LinkedHashMap?**
- Maintains insertion order
- Ensures consistent output format
- Makes frontend display predictable

### 2.4 Immutability Patterns

**Card:** Immutable (final fields, no setters)
- **Why?** Prevents accidental modification
- Ensures thread safety
- Cards should not change once created

**Game.shoe:** Returns copy in `getShoe()`
- **Why?** Prevents external modification
- Maintains encapsulation
- `return new ArrayList<>(shoe);`

**Player.hand:** Returns copy in `getHand()`
- **Why?** Same reason - encapsulation

**Deck.cards:** Returns copy in `getCards()`
- **Why?** Prevents external modification of deck

---

## Phase 3: API Design & Architecture {#phase-3}

### 3.1 REST API Endpoints

**Base URL:** `/api`

| Method | Endpoint | Purpose | Status Code |
|--------|----------|---------|-------------|
| POST | `/games` | Create game | 201 Created |
| DELETE | `/games/{gameId}` | Delete game | 204 No Content |
| POST | `/decks` | Create deck | 201 Created |
| POST | `/games/{gameId}/decks` | Add deck to game | 200 OK |
| POST | `/games/{gameId}/players` | Add player | 200 OK |
| DELETE | `/games/{gameId}/players/{playerName}` | Remove player | 204 No Content |
| POST | `/games/{gameId}/deal` | Deal cards | 200 OK |
| GET | `/games/{gameId}/players/{playerName}/cards` | Get player cards | 200 OK |
| GET | `/games/{gameId}/players` | Get players sorted | 200 OK |
| GET | `/games/{gameId}/undealt/suits` | Get undealt by suit | 200 OK |
| GET | `/games/{gameId}/undealt/cards` | Get undealt details | 200 OK |
| POST | `/games/{gameId}/shuffle` | Shuffle deck | 200 OK |

### 3.2 REST Design Principles

**Resource-Based URLs:**
- `/games` - collection of games
- `/games/{id}` - specific game
- `/games/{id}/players` - players in a game (nested resource)
- `/games/{id}/players/{name}` - specific player (nested resource)

**HTTP Verbs:**
- GET - retrieve data (idempotent, safe)
- POST - create resource or perform action
- DELETE - remove resource (idempotent)

**Why POST for shuffle?**
- Shuffle is an action/operation, not a resource
- Modifies game state
- Could be PUT, but POST is more common for actions

### 3.3 DTOs (Data Transfer Objects)

**Why Separate DTOs from Domain Models?**
1. **API Contract:** DTOs define what clients see
2. **Evolution:** Can change API without changing domain
3. **Security:** Hide internal fields
4. **Serialization:** Control JSON structure
5. **Validation:** Add validation annotations (@NotNull, @NotBlank)

**DTO Examples:**
- `CardDto` - represents Card in API
- `PlayerDto` - represents Player with hand value
- `CreateGameResponse` - response with gameId
- `DealCardsRequest` - request with playerName and count

**Conversion Location:**
- Controllers convert between Domain Models and DTOs
- Service layer works only with Domain Models

### 3.4 Exception Handling

**GlobalExceptionHandler** (`@RestControllerAdvice`)

**Handles:**
- `IllegalArgumentException` → 400 Bad Request
- `IllegalStateException` → 409 Conflict
- `MethodArgumentNotValidException` → 400 Bad Request (validation errors)
- `Exception` → 500 Internal Server Error (catch-all)

**Error Response Format:**
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Game not found: abc-123",
  "path": "/api/games/abc-123/players"
}
```

**Why Global Handler?**
- Consistent error responses
- Centralized error handling
- No need for try-catch in every controller

### 3.5 Validation

**Request Validation:**
- `@Valid` annotation on request bodies
- Validation annotations on DTOs:
  - `@NotNull` - field cannot be null
  - `@NotBlank` - string cannot be blank
  - `@Min/@Max` - numeric ranges

**Example:**
```java
@PostMapping("/{gameId}/deal")
public ResponseEntity<List<CardDto>> dealCards(
    @PathVariable String gameId,
    @Valid @RequestBody DealCardsRequest request) {
    // request is validated automatically
}
```

---

## Phase 4: Testing Strategy {#phase-4}

### 4.1 Test Types

**Unit Tests:**
- Service layer tests (`GameServiceTest`, `DeckServiceTest`)
- Utility tests (`ShuffleUtilTest`)
- Test business logic in isolation

**Integration Tests:**
- Controller tests (`GameControllerTest`, `DeckControllerTest`)
- Use MockMvc to test HTTP endpoints
- Test full request/response cycle

### 4.2 Key Test Scenarios

#### Shuffle Tests (`ShuffleUtilTest`)
1. **Empty list:** Should handle gracefully
2. **Single card:** Should not crash
3. **Maintains all cards:** Shuffled deck has same cards (different order)
4. **Changes order:** After shuffle, order should be different (high probability)
5. **Size preserved:** Shuffle doesn't add/remove cards

#### Game Service Tests (`GameServiceTest`)
1. **Create/Delete game**
2. **Add deck to game**
3. **Multiple decks:** Can add multiple decks (shoe size increases)
4. **Duplicate deck:** Cannot add same deck twice
5. **Add/Remove players**
6. **Deal cards:** Correct number dealt, removed from shoe
7. **Deal more than available:** Should throw exception
8. **Player sorting:** Players sorted by hand value descending
9. **Undealt cards:** Correct counts by suit and face value

#### Controller Tests
1. **HTTP status codes:** Correct status returned
2. **Request validation:** Invalid requests return 400
3. **Error handling:** Exceptions return proper error responses
4. **Response format:** DTOs correctly serialized

### 4.3 Testing Best Practices

**Arrange-Act-Assert Pattern:**
```java
@Test
void testDealCards() {
    // Arrange
    Game game = gameService.createGame();
    Deck deck = deckService.createDeck();
    gameService.addDeckToGame(game.getId(), deck.getId());
    gameService.addPlayer(game.getId(), "Alice");
    
    // Act
    List<Card> cards = gameService.dealCards(game.getId(), "Alice", 5);
    
    // Assert
    assertEquals(5, cards.size());
    assertEquals(47, game.getShoeSize());
}
```

**Edge Cases to Test:**
- Empty collections
- Null inputs
- Invalid inputs (negative counts, empty strings)
- Boundary conditions (dealing all cards, dealing 0 cards)

---

## Phase 5: Frontend Architecture {#phase-5}

### 5.1 Technology Choices

**React + TypeScript:**
- Type safety at compile time
- Better IDE support
- Industry standard
- Catches errors before runtime

**Material-UI:**
- Professional UI components
- Consistent design system
- Faster development
- Trade-off: Larger bundle size

**Context API:**
- Simple state management for this use case
- No Redux needed (overkill for this scope)
- Global game state accessible to all components

**Axios:**
- HTTP client for API calls
- Better than fetch (automatic JSON parsing, interceptors)

### 5.2 Component Structure

**Main Components:**
- `GameManager` - Create/delete games
- `DeckManager` - Create decks, add to game
- `PlayerManager` - Add/remove players
- `CardDealer` - Deal cards interface
- `PlayerHand` - Display player's cards
- `PlayersLeaderboard` - Sorted players
- `UndealtCardsView` - Undealt cards statistics
- `ShuffleButton` - Shuffle game deck

**State Management:**
- `GameContext` - Provides current gameId and refresh function
- Components consume context to get/update game state
- API calls in service layer (`api.ts`)

### 5.3 API Client Pattern

**Location:** `frontend/src/services/api.ts`

**Pattern:**
- Typed methods for each endpoint
- Centralized error handling
- Base URL configuration
- Returns typed responses (TypeScript interfaces)

**Example:**
```typescript
export const createGame = async (): Promise<CreateGameResponse> => {
  const response = await axios.post<CreateGameResponse>(`${API_BASE_URL}/games`);
  return response.data;
};
```

---

## Common Interview Questions {#interview-questions}

### Algorithm Questions

**Q: Explain the Fisher-Yates shuffle algorithm.**
**A:**
- Fisher-Yates is an unbiased shuffle algorithm
- Iterates from last element to first
- At each position i, picks random index j from 0 to i
- Swaps elements at i and j
- Time complexity: O(n), Space: O(1)
- Each permutation has equal probability (1/n!)

**Q: Why did you choose Fisher-Yates over other algorithms?**
**A:**
- Industry standard for unbiased shuffling
- Optimal time complexity O(n)
- Well-understood and proven algorithm
- Requirements explicitly said no library shuffle, so needed custom implementation

**Q: What's the time complexity of [operation]?**
**A:**
- `shuffle()`: O(n) where n = number of cards
- `dealCards()`: O(k) where k = number of cards to deal
- `getPlayersSorted()`: O(n log n) where n = number of players
- `getUndealtCardsCount()`: O(m) where m = number of undealt cards
- `addDeckToGame()`: O(d) where d = deck size (52)

### Architecture Questions

**Q: Why did you use in-memory storage instead of a database?**
**A:**
- Appropriate for assignment scope (MVP/prototype)
- Faster development and testing
- Easy to extend to database later (JPA/Hibernate)
- Structure supports migration (Repository pattern)
- Trade-off: Data lost on restart, but acceptable for this use case

**Q: How would you extend this to use a database?**
**A:**
1. Replace repositories with JPA repositories
2. Add `@Entity` annotations to models
3. Use Spring Data JPA (`JpaRepository`)
4. Add `application.properties` with database config
5. Consider transactions for multi-step operations
6. Add database migrations (Flyway/Liquibase)

**Q: Why separate DTOs from domain models?**
**A:**
- API contract independence (can change API without changing domain)
- Security (hide internal fields)
- Control serialization
- Validation at API boundary
- Allows API versioning

**Q: Why use a service layer?**
**A:**
- Separation of concerns
- Business logic in one place (reusable)
- Easier to test (mock repositories)
- Can be used by multiple controllers
- Maintainable and extensible

### Design Decision Questions

**Q: Why Spring Boot?**
**A:**
- Enterprise-grade framework
- Built-in REST support, validation, error handling
- Extensive ecosystem
- Easy to extend (security, database, etc.)
- Well-documented, industry standard

**Q: Why React + TypeScript?**
**A:**
- Type safety catches errors at compile time
- Modern tooling and ecosystem
- Industry standard
- Better IDE support
- Easier refactoring

**Q: Why Material-UI?**
**A:**
- Professional UI with minimal custom CSS
- Faster development
- Consistent design system
- Trade-off: Larger bundle size, but worth it for speed

**Q: What trade-offs did you make?**
**A:**
1. **In-Memory vs Database:** Chose in-memory for simplicity, but structure allows easy migration
2. **No Authentication:** Assignment doesn't require it, but structure supports Spring Security
3. **Material-UI vs Custom CSS:** Faster development, but adds bundle size
4. **Context API vs Redux:** Simpler for this use case, but Redux better for complex state

### Extension Questions

**Q: How would you add authentication?**
**A:**
1. Add Spring Security dependency
2. Configure security (JWT or session-based)
3. Add user entity and repository
4. Secure endpoints with `@PreAuthorize`
5. Frontend: Add login component, store token, send in headers

**Q: How would you make this production-ready?**
**A:**
1. Database persistence (PostgreSQL/MySQL)
2. Authentication and authorization
3. Logging (SLF4J + Logback)
4. Monitoring (Micrometer + Prometheus)
5. Error tracking (Sentry)
6. API rate limiting
7. Input sanitization
8. Comprehensive testing (unit + integration + e2e)
9. CI/CD pipeline
10. Docker deployment
11. Health checks
12. Documentation (OpenAPI/Swagger)

**Q: How would you handle concurrent access?**
**A:**
- Already handled: `ConcurrentHashMap` in repositories (thread-safe)
- For database: Use transactions and locking
- Consider optimistic locking (version field)
- Or pessimistic locking for critical operations
- For high concurrency: Consider distributed cache (Redis)

---

## Practice Explanations {#practice-explanations}

### Practice 1: Explain Shuffling

**Try explaining this out loud:**

"The shuffle implementation uses the Fisher-Yates algorithm, which is an industry-standard unbiased shuffle. The algorithm works by iterating from the last card to the first. At each position, it picks a random card from the remaining unshuffled portion and swaps it with the current card. This ensures each possible arrangement has equal probability.

I used SecureRandom instead of the regular Random class because it provides cryptographic-quality randomness, which is important for fairness in card games. The time complexity is O(n) where n is the number of cards, which is optimal.

The algorithm modifies the list in-place, so no extra memory is needed beyond a temporary variable for swapping. This makes it space-efficient as well."

### Practice 2: Explain Card Dealing

**Try explaining this out loud:**

"When dealing cards, the system removes cards from the front of the shoe, which simulates dealing from the top of a physical deck. The shoe is implemented as an ArrayList, and we use `remove(0)` to take cards from the front.

Before dealing, we validate that the player exists in the game, that the count is positive, and that there are enough cards available. If a player requests more cards than are available, we throw an IllegalStateException with a clear error message.

The cards are then added to the player's hand, and we return the list of dealt cards to the caller. If the shoe becomes empty mid-deal, we stop and return whatever cards we were able to deal."

### Practice 3: Explain Architecture

**Try explaining this out loud:**

"The architecture follows a layered approach with clear separation of concerns. At the top, we have Controllers that handle HTTP requests and responses, converting between DTOs and domain models. Below that, the Service layer contains all business logic and orchestrates operations. The Repository layer provides data access, currently using in-memory storage with ConcurrentHashMap for thread safety.

This layered approach makes the code maintainable, testable, and extensible. For example, if we wanted to add a database, we'd only need to change the repository implementations, not the service or controller layers.

The domain models are kept separate from the API contracts using DTOs, which allows the API to evolve independently from the internal domain model. This is important for long-term maintainability."

### Practice 4: Explain Testing Strategy

**Try explaining this out loud:**

"I wrote comprehensive tests at multiple levels. Unit tests cover the service layer and utility classes, testing business logic in isolation. Integration tests use MockMvc to test the full HTTP request/response cycle for controllers.

For the shuffle algorithm, I tested edge cases like empty lists and single cards, verified that all cards are preserved after shuffling, and confirmed that the order changes. For the game service, I tested all operations including edge cases like dealing more cards than available, adding duplicate decks, and handling non-existent players.

The tests follow the Arrange-Act-Assert pattern for clarity, and I made sure to test both happy paths and error cases to ensure robust error handling."

---

## Risk Areas & How to Handle Them {#risk-areas}

### Risk 1: Deep Technical Questions

**Scenario:** "What's the mathematical proof that Fisher-Yates is unbiased?"

**How to Handle:**
- Admit if you don't know the exact proof
- Explain what you do know: "I know it's proven to be unbiased and is the industry standard"
- Show understanding: "The key is that each card has equal probability of ending up in any position"
- Redirect: "I can explain how the algorithm works step by step if that's helpful"

### Risk 2: Extension Questions You Haven't Thought About

**Scenario:** "How would you add WebSocket support for real-time updates?"

**How to Handle:**
- Think out loud: "I'd need to add Spring WebSocket dependency"
- Break it down: "Set up WebSocket configuration, create message handlers"
- Admit uncertainty: "I haven't implemented WebSockets in Spring Boot before, but I know the general approach"
- Show learning mindset: "I'd research Spring WebSocket documentation and best practices"

### Risk 3: Code Review Questions

**Scenario:** "I noticed you're using `shoe.remove(0)` which is O(n). Why not use a Queue?"

**How to Handle:**
- Acknowledge the point: "That's a good observation"
- Explain your reasoning: "I chose ArrayList for simplicity and because for this use case, the performance difference is negligible"
- Show you understand: "You're right that Queue would be more semantically correct and potentially more efficient"
- Show flexibility: "For a production system with high throughput, I'd definitely consider using a Queue"

### Risk 4: "Did You Build This Yourself?"

**How to Handle (Honest Approach):**
- "I used AI assistance to accelerate development, similar to pair programming"
- "I designed the architecture and understand all the design decisions"
- "I can explain any part of the codebase and would be comfortable extending it"
- "Using modern development tools is part of being a senior developer"

**Alternative (If Comfortable):**
- "I collaborated with AI tools to generate the initial implementation"
- "I then reviewed, tested, and refined the code to ensure it meets production standards"
- "I understand the codebase thoroughly and can explain all design decisions"

### Risk 5: Questions About Technologies You're Less Familiar With

**Scenario:** "Explain how Spring Boot's dependency injection works."

**How to Handle:**
- Explain what you know: "Spring uses reflection and annotations to manage object creation and dependencies"
- Be specific: "The @Service and @Repository annotations tell Spring to manage these as beans"
- Admit limits: "I'm not an expert on the internals of Spring's DI container"
- Show learning: "I'd need to research the exact implementation details, but I understand the concepts and how to use it"

---

## Time Complexity Cheat Sheet {#time-complexity}

| Operation | Time Complexity | Space Complexity | Notes |
|-----------|----------------|------------------|-------|
| `shuffle()` | O(n) | O(1) | n = number of cards, in-place |
| `dealCards(count)` | O(k) | O(k) | k = count, creates list of k cards |
| `addDeckToGame()` | O(d) | O(d) | d = deck size (52), copies cards |
| `getPlayersSorted()` | O(n log n) | O(n) | n = number of players |
| `getUndealtCardsBySuit()` | O(m) | O(1) | m = undealt cards, constant space for 4 suits |
| `getUndealtCardsCount()` | O(m) | O(1) | m = undealt cards, constant space (fixed suits/values) |
| `getPlayerCards()` | O(h) | O(h) | h = hand size, returns copy |
| `addPlayer()` | O(1) | O(1) | HashMap insertion |
| `removePlayer()` | O(1) | O(1) | HashMap removal |

**Data Structure Operations:**
- `ArrayList.remove(0)`: O(n) - must shift all elements
- `HashMap.get/put`: O(1) average case
- `ConcurrentHashMap.get/put`: O(1) average case, thread-safe
- `List.sort()`: O(n log n)

---

## Final Study Checklist

### Before Submission (Monday 8am)
- [ ] Read through entire codebase at least once
- [ ] Run the application locally
- [ ] Test all endpoints via Swagger
- [ ] Run all tests and understand what they verify
- [ ] Practice explaining Fisher-Yates algorithm
- [ ] Practice explaining architecture decisions
- [ ] Review time complexities
- [ ] Review design decisions document

### Before Interview
- [ ] Re-read this study guide
- [ ] Practice explaining each major component
- [ ] Review common interview questions
- [ ] Prepare answers for extension questions
- [ ] Think about what you'd change/improve
- [ ] Review test files to understand test strategy
- [ ] Be ready to discuss trade-offs

### During Interview
- [ ] Take your time - think before answering
- [ ] Ask clarifying questions if needed
- [ ] Draw diagrams if helpful (architecture, data flow)
- [ ] Admit when you don't know something
- [ ] Show your thought process
- [ ] Be honest about using AI assistance if asked

---

## Key Takeaways

1. **Understand the "Why":** Not just what the code does, but why decisions were made
2. **Know the Trade-offs:** Every decision has pros and cons
3. **Think About Extensions:** How would you improve this? What's missing?
4. **Practice Explanations:** Being able to explain clearly is as important as coding
5. **Be Honest:** It's okay to not know everything, but show you can learn

---

## Additional Resources

- **Spring Boot Docs:** https://spring.io/projects/spring-boot
- **Fisher-Yates Shuffle:** https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
- **REST API Design:** https://restfulapi.net/
- **React + TypeScript:** https://react-typescript-cheatsheet.netlify.app/

---

**Good luck with your interview! Remember: Understanding and being able to explain your code is what makes a senior developer, not just writing it.**


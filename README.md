# Deck of Cards Game

A full-stack application for managing card games with multiple players, decks, and card dealing operations. Built with Spring Boot (Java) backend and React TypeScript frontend.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Setup Instructions](#setup-instructions)
  - [Prerequisites](#prerequisites)
  - [Installing Prerequisites](#installing-prerequisites)
    - [Java 17](#java-17)
    - [Maven](#maven)
    - [Node.js and npm](#nodejs-and-npm)
    - [Docker and Docker Compose](#docker-and-docker-compose)
  - [Local Development Setup](#local-development-setup)
  - [Docker Setup](#docker-setup)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Docker Deployment](#docker-deployment)

## Overview

This repository contains the full deliverable for the deck-of-cards assignment: a Spring Boot REST API paired with a React/TypeScript frontend. Together they model multi-player games, support combining multiple 52-card decks into a shoe, enforce the shuffle/deal constraints described in the prompt, and expose all required operations with intuitive UI flows. The implementation is production-ready in structure, with clear separation of concerns, test coverage for critical paths, and Docker-based local deployment.

## Architecture

### Backend Architecture

The backend follows a layered architecture:

```
┌─────────────────┐
│   Controllers   │  REST API endpoints
├─────────────────┤
│    Services     │  Business logic
├─────────────────┤
│  Repositories   │  Data access (in-memory)
├─────────────────┤
│     Models      │  Domain entities
└─────────────────┘
```

**Key Components:**
- **Controllers**: Handle HTTP requests/responses, validation, DTOs
- **Services**: Contain business logic and orchestrate operations
- **Repositories**: In-memory storage using ConcurrentHashMap for thread safety
- **Models**: Domain entities (Card, Deck, Game, Player)
- **Utilities**: Custom shuffle implementation (Fisher-Yates)

### Frontend Architecture

The frontend uses React with TypeScript and Material-UI:

- **Components**: Modular React components for each feature
- **Context API**: Global state management for game state
- **Services**: API client with typed methods
- **Types**: TypeScript interfaces matching backend DTOs

Both layers communicate through typed DTOs generated to mirror the REST resources, keeping the contract consistent and making it straightforward to discuss backend and frontend behavior together during review.

## Features

**Backend**
- `POST /games`, `GET /games`, `DELETE /games/{id}` cover the full game lifecycle and expose multi-game management.
- `POST /decks` creates a standard 52-card deck; `POST /games/{id}/decks` merges decks into a shoe; `GET /decks/unassigned` and `GET /games/{id}/decks` expose deck assignment state.
- `POST /games/{id}/players` and `DELETE /games/{id}/players/{name}` manage players; player hands and totals persist per game.
- `POST /games/{id}/deal` deals 1..n cards while validating remaining supply; a 53rd request on a single deck correctly returns no card.
- `GET /games/{id}/players` returns the leaderboard sorted by face-value hand totals.
- `GET /games/{id}/players/{name}/cards` exposes each player’s current hand.
- `GET /games/{id}/undealt/suits` and `GET /games/{id}/undealt/cards` provide remaining card statistics as required.
- `POST /games/{id}/shuffle` performs a SecureRandom-backed Fisher–Yates shuffle and can be invoked at any time.

**Frontend**
- Workflow-driven UI for creating games, generating decks, assigning them to shoes, and listing existing games.
- Player management dashboard with live hand totals, sorted leaderboard, and contextual actions.
- Deal cards panel supporting player/quantity selection with feedback on completion.
- Player hand viewer with suit-aware styling and card metadata.
- Undealt cards explorer (suit view and card-by-card breakdown).
- Global shuffle action with loading indicator and confirmation.

## Technology Stack

### Backend

- **Java 17**: Modern Java features
- **Spring Boot 3.2.0**: Framework for REST API
- **Maven**: Build tool and dependency management
- **SpringDoc OpenAPI**: API documentation (Swagger UI)
- **JUnit 5**: Testing framework
- **Lombok**: Reducing boilerplate code

### Frontend

- **React 18**: UI library
- **TypeScript**: Type safety
- **Material-UI (MUI)**: Component library
- **Axios**: HTTP client
- **React Context API**: State management

### DevOps

- **Docker**: Containerization
- **Docker Compose**: Multi-container orchestration
- **Nginx**: Frontend web server

## Setup Instructions

### Prerequisites

The following tools are required to run this application:

- **Java 17** or higher
- **Maven 3.6** or higher
- **Node.js 18** or higher
- **npm** (comes with Node.js) or yarn
- **Docker** and **Docker Compose** (optional, for containerized deployment)

### Installing Prerequisites

#### Java 17

**Windows:**
1. Download Java 17 from [Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or [Adoptium](https://adoptium.net/)
2. Run the installer and follow the installation wizard
3. Verify installation:
   ```bash
   java -version
   ```

**macOS:**
```bash
# Using Homebrew
brew install openjdk@17

# Add to PATH
echo 'export PATH="/usr/local/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

# Verify installation
java -version
```

**Linux (Ubuntu/Debian):**
```bash
# Update package index
sudo apt update

# Install OpenJDK 17
sudo apt install openjdk-17-jdk

# Verify installation
java -version
```

#### Maven

**Windows:**
1. Download Maven from [Apache Maven](https://maven.apache.org/download.cgi) (Binary zip archive)
2. Extract to a directory (e.g., `C:\Program Files\Apache\maven`)
3. Add Maven `bin` directory to your PATH environment variable:
   - Open System Properties → Environment Variables
   - Add `C:\Program Files\Apache\maven\bin` to PATH
4. Verify installation:
   ```bash
   mvn --version
   ```

**macOS:**
```bash
# Using Homebrew
brew install maven

# Verify installation
mvn --version
```

**Linux (Ubuntu/Debian):**
```bash
# Install Maven
sudo apt install maven

# Verify installation
mvn --version
```

#### Node.js and npm

**Windows:**
1. Download Node.js from [nodejs.org](https://nodejs.org/) (LTS version recommended)
2. Run the installer and follow the installation wizard
3. Verify installation:
   ```bash
   node --version
   npm --version
   ```

**macOS:**
```bash
# Using Homebrew
brew install node

# Verify installation
node --version
npm --version
```

**Linux (Ubuntu/Debian):**
```bash
# Install Node.js 18.x
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt install -y nodejs

# Verify installation
node --version
npm --version
```

#### Docker and Docker Compose

**Windows:**
1. Download Docker Desktop from [docker.com](https://www.docker.com/products/docker-desktop/)
2. Run the installer and follow the installation wizard
3. Restart your computer if prompted
4. Launch Docker Desktop
5. Verify installation:
   ```bash
   docker --version
   docker-compose --version
   ```

**macOS:**
1. Download Docker Desktop for Mac from [docker.com](https://www.docker.com/products/docker-desktop/)
2. Open the `.dmg` file and drag Docker to Applications
3. Launch Docker Desktop from Applications
4. Verify installation:
   ```bash
   docker --version
   docker-compose --version
   ```

**Linux (Ubuntu/Debian):**
```bash
# Update package index
sudo apt update

# Install Docker
sudo apt install docker.io docker-compose

# Add your user to the docker group (to run without sudo)
sudo usermod -aG docker $USER

# Log out and log back in for group changes to take effect

# Verify installation
docker --version
docker-compose --version
```

**Note:** After installing Docker on Linux, you may need to start the Docker service:
```bash
sudo systemctl start docker
sudo systemctl enable docker
```

### Local Development Setup

#### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. The API will be available at `http://localhost:8080`
5. Swagger UI will be available at `http://localhost:8080/swagger-ui.html`

#### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. (Optional) Configure the API base URL by creating a `.env` file with `REACT_APP_API_URL=http://localhost:8080/api` if the backend is not running on the default host/port.

4. Start the development server:
   ```bash
   npm start
   ```

5. The application will be available at `http://localhost:3000`

### Docker Setup

1. Build and start all services:
   ```bash
   docker-compose up --build
   ```

2. Access the application:
   - Frontend: `http://localhost:3000`
   - Backend API: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

3. Stop the services:
   ```bash
   docker-compose down
   ```

## API Documentation

### Base URL

```
http://localhost:8080/api
```

### Endpoints

#### Games

- **POST** `/games` - Create a new game
  - Response: `{ "gameId": "string" }`
- **GET** `/games` - List all active games with deck and player counts
- **DELETE** `/games/{gameId}` - Delete a game

#### Decks

- **POST** `/decks` - Create a new deck
  - Response: `{ "deckId": "string" }`
- **GET** `/decks/unassigned` - Fetch deck IDs not yet associated with a game
- **POST** `/games/{gameId}/decks` - Add a deck to a game
  - Request Body: `{ "deckId": "string" }`
- **GET** `/games/{gameId}/decks` - Retrieve deck IDs already assigned to the game

#### Players

- **POST** `/games/{gameId}/players` - Add a player to a game
  - Request Body: `{ "playerName": "string" }`

- **DELETE** `/games/{gameId}/players/{playerName}` - Remove a player from a game

- **GET** `/games/{gameId}/players` - Get all players sorted by hand value
  - Response: `[{ "name": "string", "hand": [...], "handValue": number, "handSize": number }]`

#### Cards

- **POST** `/games/{gameId}/deal` - Deal cards to a player
  - Request Body: `{ "playerName": "string", "count": number }`
  - Response: `[{ "suit": "HEARTS", "faceValue": "ACE", "value": 1, "displayName": "Ace of Hearts" }]`

- **GET** `/games/{gameId}/players/{playerName}/cards` - Get player's cards
  - Response: `[{ "suit": "HEARTS", "faceValue": "ACE", "value": 1, "displayName": "Ace of Hearts" }]`

#### Deck Statistics

- **GET** `/games/{gameId}/undealt/suits` - Get undealt cards by suit
  - Response: `{ "suitCounts": { "HEARTS": 13, "SPADES": 13, ... } }`

- **GET** `/games/{gameId}/undealt/cards` - Get detailed undealt cards count
  - Response: `{ "cardCounts": { "HEARTS": { "KING": 1, "QUEEN": 1, ... }, ... } }`

#### Shuffle

- **POST** `/games/{gameId}/shuffle` - Shuffle the game deck

### Swagger UI

Interactive API documentation is available at:
```
http://localhost:8080/swagger-ui.html
```

## Testing

### Backend Tests

Run backend tests:
```bash
cd backend
mvn test
```

**Test Coverage**:
- Unit + integration coverage across services (`GameService`, `DeckService`), utility shuffle code, and MockMvc controller flows (`GameController`, `DeckController`).
- Validates deck addition rules, duplication guards, card dealing edge cases, sorting, shuffle behavior, and error responses.
- Last verified: `mvn test` (2025-11-09) → 35 tests, all passing.

**Key Test Scenarios**:
- Game creation and deletion
- Deck creation and addition to games
- Player management
- Card dealing (including edge cases)
- Shuffle algorithm correctness
- Sorting players by hand value

### Frontend Tests

No automated React tests are defined yet; `npm test` reports “No tests found.”

If you add component tests (Jest + Testing Library), run them with:
```bash
cd frontend
npm test
```

## Docker Deployment

### Building Images

**Backend**:
```bash
cd backend
docker build -t cards-game-backend .
```

**Frontend**:
```bash
cd frontend
docker build -t cards-game-frontend .
```

### Running with Docker Compose

```bash
docker-compose up --build
```

This will:
1. Build both backend and frontend images
2. Start backend container on port 8080
3. Start frontend container on port 3000
4. Configure networking between containers

## Project Structure

```
deck-of-cards-game/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/goto/cards/
│   │   │   │   ├── controller/     # REST controllers
│   │   │   │   ├── service/        # Business logic
│   │   │   │   ├── repository/     # Data access
│   │   │   │   ├── model/          # Domain models
│   │   │   │   ├── dto/            # Data Transfer Objects
│   │   │   │   ├── exception/      # Exception handling
│   │   │   │   ├── config/         # Configuration
│   │   │   │   └── util/           # Utilities
│   │   │   └── resources/
│   │   └── test/                   # Tests
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── components/             # React components
│   │   ├── services/               # API client
│   │   ├── types/                  # TypeScript types
│   │   ├── context/                # React context
│   │   └── App.tsx
│   ├── public/
│   └── package.json
├── docker-compose.yml
└── README.md
```

## Design Decisions

1. **Spring Boot Framework**  
   I leaned on Spring Boot for the backend because it’s an industry-standard framework with a deep ecosystem. It gives me first-class REST support, validation, and error handling out of the box, and leaves the door open for layering in database access, security, or messaging later.

2. **Fisher-Yates Shuffle Algorithm**  
   The requirements ruled out library shuffles, so I implemented the classic Fisher-Yates routine using `SecureRandom` for better randomness. It’s O(n), space efficient, and guarantees every permutation is equally likely.
   ```java
   for (int i = cards.size() - 1; i > 0; i--) {
       int j = random.nextInt(i + 1);
       Card temp = cards.get(i);
       cards.set(i, cards.get(j));
       cards.set(j, temp);
   }
   ```

3. **In-Memory Storage**  
   For the assignment scope I kept persistence simple with `ConcurrentHashMap`-backed repositories. They’re thread-safe, easy to reason about, and let the rest of the layers focus on domain rules. The trade-off is obvious—state disappears on restart—but it keeps the prototype fast to iterate.

   **Migration path:**  
   - Keep business logic in the domain models (`Game`, `Player`, `Deck`, `Card`).  
   - Introduce JPA entities plus Spring Data repositories (`interface GameRepository extends JpaRepository<GameEntity, UUID>`).  
   - Map entities ↔ domain inside services so the domain stays testable.  
   - Use PostgreSQL with Flyway/Liquibase migrations, `@EntityGraph` for eager loads, and transactional service methods (`dealCards`, `addDeckToGame`) to maintain consistency.

4. **RESTful API Design**  
   Everything is expressed as resources with predictable URLs and HTTP verbs: `POST /api/games` to create, `GET /api/games/{id}/players` to dig into nested resources, `DELETE /api/games/{id}` to clean up. Sticking to REST doctrine keeps the API easy to use and easy to document.

5. **DTOs (Data Transfer Objects)**  
   Controllers convert between DTOs and domain objects. That separation keeps the API contract stable, hides internal fields, and gives me a natural place to apply validation annotations before anything hits the service layer.

6. **Service Layer Pattern**  
   All the business rules live inside services. Controllers stay thin, services are unit-test friendly, and the domain logic can be reused if we ever add another interface (CLI, batch jobs, etc.).

7. **React with TypeScript**  
   TypeScript catches shape issues before they reach the browser and makes refactors safer. Paired with React 18 it delivers modern developer ergonomics and familiar tooling for reviewers.

8. **Material-UI**  
   MUI let me ship a polished UI quickly. The trade-off is additional bundle weight, but for this assignment the speed of development and consistent design language were worth it.

9. **Context API for State Management**  
   Global state needs are modest, so React Context keeps things lightweight. If the app grows, dropping in Redux or another store is straightforward thanks to the service/component boundaries already in place.

10. **Docker & Docker Compose**  
    Docker files ensure anyone can spin up the stack with a single command. It also mirrors how I’d package the services for a production environment.

## Future Enhancements

While the current implementation satisfies the assignment brief, here’s how I’d evolve it for production:

- **Database integration:** Swap the in-memory repositories for PostgreSQL/MySQL, add JPA entities and Flyway/Liquibase migrations, and cover persistence with Testcontainers-backed integration tests.  
- **Authentication & authorization:** Introduce Spring Security plus JWT (or session management) so only authenticated users can manipulate games.  
- **Real-time updates:** Layer in WebSockets or Server-Sent Events to broadcast game changes instantly to connected clients.  
- **Game rule engines:** Extend the domain layer to support specific titles (Blackjack, Poker) with scoring and betting logic.  
- **Durable persistence:** Persist game snapshots so sessions survive restarts or can be resumed later.  
- **Caching:** Use Redis for hot reads (undealt counts, leaderboard) to improve response times under load.  
- **Monitoring & logging:** Wire up structured logging and observability (e.g., ELK stack, Prometheus/Grafana) for operational insight.  
- **CI/CD pipeline:** Automate tests, container builds, and deployments to keep releases repeatable and safe.


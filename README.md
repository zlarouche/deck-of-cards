<<<<<<< HEAD
# deck-of-cards
=======
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
- [Design Decisions](#design-decisions)
- [Testing](#testing)
- [Docker Deployment](#docker-deployment)

## Overview

This application provides a REST API and web-based frontend for managing deck of cards games. It supports:

- Creating and managing multiple games
- Creating standard 52-card decks
- Adding multiple decks to create a "shoe" (game deck)
- Managing players in games
- Dealing cards to players
- Viewing player hands and leaderboards
- Tracking undealt cards
- Shuffling game decks using a custom Fisher-Yates algorithm

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

## Features

### Backend Features

1. **Game Management**
   - Create and delete games
   - Each game maintains its own shoe and players

2. **Deck Management**
   - Create standard 52-card decks
   - Add decks to games (once added, cannot be removed)
   - Support for multiple decks in a single game

3. **Player Management**
   - Add and remove players from games
   - Track player hands and hand values

4. **Card Dealing**
   - Deal cards from the game deck to players
   - Validates available cards before dealing
   - Returns empty list when no cards available

5. **Card Statistics**
   - View undealt cards by suit
   - View detailed count of each card remaining
   - Sorted by suit and face value (King to Ace)

6. **Shuffling**
   - Custom Fisher-Yates shuffle algorithm
   - Can be called at any time
   - Uses SecureRandom for cryptographic-quality randomness

### Frontend Features

1. **Game Management UI**
   - Create and delete games
   - Display current game ID

2. **Deck Management UI**
   - Create new decks
   - Add decks to current game
   - Visual indication of added decks

3. **Player Management UI**
   - Add and remove players
   - List all players with hand values

4. **Card Dealing UI**
   - Select player and number of cards
   - Deal cards with visual feedback

5. **Player Hand Viewer**
   - View cards in a player's hand
   - Display card values and suits with color coding

6. **Leaderboard**
   - Players sorted by hand value (descending)
   - Visual ranking indicators

7. **Undealt Cards View**
   - Tabbed interface for suit counts and detailed counts
   - Visual representation with color-coded suits

8. **Shuffle Button**
   - One-click shuffle with loading indicator

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

3. Start the development server:
   ```bash
   npm start
   ```

4. The application will be available at `http://localhost:3000`

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

- **DELETE** `/games/{gameId}` - Delete a game

#### Decks

- **POST** `/decks` - Create a new deck
  - Response: `{ "deckId": "string" }`

- **POST** `/games/{gameId}/decks` - Add a deck to a game
  - Request Body: `{ "deckId": "string" }`

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

## Design Decisions

### 1. Spring Boot Framework

**Decision**: Use Spring Boot for the backend.

**Rationale**:
- Enterprise-grade framework with extensive ecosystem
- Built-in support for REST APIs, validation, and error handling
- Easy to extend with databases, security, and other features
- Well-documented and widely used in industry

### 2. Fisher-Yates Shuffle Algorithm

**Decision**: Implement custom Fisher-Yates shuffle instead of using library shuffle.

**Rationale**:
- Requirements explicitly state not to use library-provided shuffle
- Fisher-Yates is the industry-standard unbiased shuffle algorithm
- O(n) time complexity
- Uses SecureRandom for better randomness quality

**Implementation**:
```java
for (int i = cards.size() - 1; i > 0; i--) {
    int j = random.nextInt(i + 1);
    // Swap cards at positions i and j
}
```

### 3. In-Memory Storage

**Decision**: Use in-memory storage (ConcurrentHashMap) instead of a database.

**Rationale**:
- Appropriate for the assignment scope
- Thread-safe with ConcurrentHashMap
- Easy to extend to database later (JPA/Hibernate)
- Fast for development and testing

**Trade-off**: Data is lost on server restart, but this is acceptable for the assignment scope.

### 4. RESTful API Design

**Decision**: Follow REST principles with resource-based URLs.

**Rationale**:
- Standard approach for web APIs
- Intuitive and self-documenting
- Proper use of HTTP verbs (GET, POST, DELETE)
- Stateless design

**Examples**:
- `POST /api/games` - Create resource
- `GET /api/games/{id}/players` - Get sub-resource
- `DELETE /api/games/{id}` - Delete resource

### 5. DTOs (Data Transfer Objects)

**Decision**: Separate domain models from API contracts using DTOs.

**Rationale**:
- Decouples internal domain model from API
- Allows API evolution without changing domain models
- Better control over serialization
- Security (hide internal fields)

### 6. Service Layer Pattern

**Decision**: Separate business logic into service layer.

**Rationale**:
- Separation of concerns
- Easier to test business logic
- Reusable across different controllers
- Maintainable and extensible

### 7. React with TypeScript

**Decision**: Use React with TypeScript for the frontend.

**Rationale**:
- Type safety catches errors at compile time
- Modern tooling and ecosystem
- Industry standard
- Better IDE support and refactoring

### 8. Material-UI

**Decision**: Use Material-UI component library.

**Rationale**:
- Professional, polished UI out of the box
- Consistent design system
- Responsive components
- Faster development than custom CSS

**Trade-off**: Adds bundle size, but provides significant development speedup.

### 9. Context API for State Management

**Decision**: Use React Context API instead of Redux.

**Rationale**:
- Simpler for this use case
- No additional dependencies
- Sufficient for global game state
- Easier to understand and maintain

### 10. Docker & Docker Compose

**Decision**: Provide Docker containers for easy deployment.

**Rationale**:
- Reproducible deployments
- Easy setup for reviewers
- Production-ready approach
- Isolates dependencies

## Testing

### Backend Tests

Run backend tests:
```bash
cd backend
mvn test
```

**Test Coverage**:
- Unit tests for services (GameService, DeckService)
- Unit tests for shuffle algorithm
- Integration tests for controllers (MockMvc)

**Key Test Scenarios**:
- Game creation and deletion
- Deck creation and addition to games
- Player management
- Card dealing (including edge cases)
- Shuffle algorithm correctness
- Sorting players by hand value

### Frontend Tests

Run frontend tests:
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

## Future Enhancements

While this implementation meets all assignment requirements, potential enhancements for a production system:

1. **Database Integration**: Replace in-memory storage with PostgreSQL/MySQL
2. **Authentication**: Add user authentication and authorization
3. **WebSockets**: Real-time updates for multiplayer games
4. **Game Rules**: Implement specific card game rules (Blackjack, Poker, etc.)
5. **Persistence**: Save game state to database
6. **Caching**: Add Redis for performance
7. **Monitoring**: Add logging and monitoring (ELK stack)
8. **CI/CD**: Automated testing and deployment pipelines

## License

This project is created for a technical assignment.

## Author

Created as part of a Senior Software Developer technical assessment.

>>>>>>> 9883163 (Add initial project structure with backend and frontend components for a Deck of Cards game. Includes REST API setup with Spring Boot, React frontend, Docker configurations, and basic game logic.)

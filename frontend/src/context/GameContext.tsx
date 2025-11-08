import React, { createContext, useContext, useState, ReactNode } from 'react';

interface GameContextType {
  gameId: string | null;
  setGameId: (id: string | null) => void;
  decks: string[];
  addDeck: (deckId: string) => void;
  refreshTrigger: number;
  triggerRefresh: () => void;
}

const GameContext = createContext<GameContextType | undefined>(undefined);

export const GameProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [gameId, setGameId] = useState<string | null>(null);
  const [decks, setDecks] = useState<string[]>([]);
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  const addDeck = (deckId: string) => {
    setDecks([...decks, deckId]);
  };

  const triggerRefresh = () => {
    setRefreshTrigger(prev => prev + 1);
  };

  return (
    <GameContext.Provider value={{ gameId, setGameId, decks, addDeck, refreshTrigger, triggerRefresh }}>
      {children}
    </GameContext.Provider>
  );
};

export const useGame = () => {
  const context = useContext(GameContext);
  if (!context) {
    throw new Error('useGame must be used within a GameProvider');
  }
  return context;
};


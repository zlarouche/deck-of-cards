import React, { createContext, useContext, useState, ReactNode } from 'react';

interface GameContextType {
  gameId: string | null;
  setGameId: (id: string | null) => void;
  gameName: string | null;
  setGameName: (name: string | null) => void;
  decks: string[];
  addDeck: (deckId: string) => void;
  refreshTrigger: number;
  triggerRefresh: () => void;
  clearGameDecks: () => void;
  replaceDecks: (deckIds: string[]) => void;
}

const GameContext = createContext<GameContextType | undefined>(undefined);

export const GameProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [gameId, setGameId] = useState<string | null>(null);
  const [gameName, setGameName] = useState<string | null>(null);
  const [decks, setDecks] = useState<string[]>([]);
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  const addDeck = (deckId: string) => {
    setDecks(prev => [...prev, deckId]);
  };

  const replaceDecks = (deckIds: string[]) => {
    setDecks(deckIds);
  };

  const triggerRefresh = () => {
    setRefreshTrigger(prev => prev + 1);
  };

  const clearGameDecks = () => {
    setDecks([]);
  } 

  return (
    <GameContext.Provider value={{ gameId, setGameId, gameName, setGameName, decks, addDeck, refreshTrigger, triggerRefresh, clearGameDecks, replaceDecks }}>
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


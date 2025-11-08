import React, { useState, useEffect } from 'react';
import {
  Box,
  Button,
  Card,
  CardContent,
  Typography,
  Alert,
  List,
  ListItem,
  ListItemText,
} from '@mui/material';
import { createDeck, addDeckToGame, getAddedDeckIds, getUnassignedDeckIds } from '../services/api';
import { useGame } from '../context/GameContext';

const DeckManager: React.FC = () => {
  const { gameId, decks, addDeck, triggerRefresh, refreshTrigger, replaceDecks } = useGame();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [unassignedDecks, setUnassignedDecks] = useState<string[]>([]);

  const handleCreateDeck = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await createDeck();
      setUnassignedDecks((prev) => [...prev, response.deckId]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to create deck');
    } finally {
      setLoading(false);
    }
  };

  const handleAddDeckToGame = async (deckId: string) => {
    if (!gameId) {
      setError('Please create a game first');
      return;
    }
    setLoading(true);
    setError(null);
    try {
      await addDeckToGame(gameId, { deckId });
      addDeck(deckId);
      triggerRefresh();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to add deck to game');
    } finally {
      setLoading(false);
    }
  };

  const loadDecks = async () => {
    setLoading(true);
    setError(null);
    try {
      const [availableDeckIds, addedDeckIds] = await Promise.all([
        getUnassignedDeckIds(),
        gameId ? getAddedDeckIds(gameId) : Promise.resolve<string[]>([]),
      ]);
      setUnassignedDecks(availableDeckIds);
      replaceDecks(addedDeckIds);
    } catch (err: any) {
      setError(err?.response?.data?.message || 'Failed to load decks');
      setUnassignedDecks([]);
      if (!gameId) {
        replaceDecks([]);
      }
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadDecks();
  }, [gameId, refreshTrigger]);

  return (
    <Card>
      <CardContent>
        <Typography variant="h5" gutterBottom>
          Deck Management
        </Typography>

        {error && (
          <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        <Box sx={{ mb: 2 }}>
          <Button
            variant="contained"
            color="primary"
            onClick={handleCreateDeck}
            disabled={loading}
            sx={{ mb: 2 }}
          >
            Create New Deck
          </Button>
        </Box>

        {unassignedDecks.length > 0 && (
          <Box>
            <Typography variant="h6" gutterBottom>
              Available Decks
            </Typography>
            <List>
              {unassignedDecks.map((deckId) => (
                <ListItem
                  key={deckId}
                  secondaryAction={
                    <Button
                      variant="outlined"
                      size="small"
                      onClick={() => handleAddDeckToGame(deckId)}
                      disabled={loading || !gameId || decks.includes(deckId)}
                    >
                      {decks.includes(deckId) ? 'Added' : 'Add to Game'}
                    </Button>
                  }
                >
                  <ListItemText primary={deckId} />
                </ListItem>
              ))}
            </List>
          </Box>
        )}

        {decks.length > 0 && (
          <Box sx={{ mt: 2 }}>
            <Typography variant="h6" gutterBottom>
              Decks in Game
            </Typography>
            <Typography variant="body2" color="text.secondary">
              {decks.length} deck(s) added to game
            </Typography>
          </Box>
        )}
      </CardContent>
    </Card>
  );
};

export default DeckManager;


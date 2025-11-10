import React, { useState, useEffect } from 'react';
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Divider,
  List,
  ListItem,
  ListItemText,
  Stack,
  Typography,
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
      <CardContent sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
        <Typography variant="h5">Deck Management</Typography>

        {error && (
          <Alert severity="error" onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        <Stack
          direction={{ xs: 'column', sm: 'row' }}
          spacing={2}
          alignItems={{ xs: 'stretch', sm: 'center' }}
        >
          <Button
            variant="contained"
            color="primary"
            onClick={handleCreateDeck}
            disabled={loading}
          >
            Create New Deck
          </Button>
          <Typography variant="body2" color="text.secondary">
            Create decks and assign them to the active game.
          </Typography>
        </Stack>

        <Divider />

        <Box>
          <Typography variant="h6" gutterBottom>
            Available Decks
          </Typography>
          {unassignedDecks.length === 0 ? (
            <Typography variant="body2" color="text.secondary">
              No unassigned decks. Create a deck to add it to the list.
            </Typography>
          ) : (
            <List disablePadding>
              {unassignedDecks.map((deckId) => (
                <ListItem
                  key={deckId}
                  divider
                  secondaryAction={
                    <Button
                      variant="outlined"
                      size="small"
                      onClick={() => handleAddDeckToGame(deckId)}
                      disabled={loading || !gameId || decks.includes(deckId)}
                    >
                      Add to Game
                    </Button>
                  }
                >
                  <ListItemText primary={deckId} />
                </ListItem>
              ))}
            </List>
          )}
        </Box>

        <Divider />

        <Box>
          <Typography variant="h6" gutterBottom>
            Decks in Selected Game
          </Typography>
          {decks.length === 0 ? (
            <Typography variant="body2" color="text.secondary">
              No decks have been added to the active game yet.
            </Typography>
          ) : (
            <List disablePadding>
              {decks.map((deckId) => (
                <ListItem key={deckId} divider>
                  <ListItemText primary={`Deck ID: ${deckId}`} />
                </ListItem>
              ))}
            </List>
          )}
        </Box>
      </CardContent>
    </Card>
  );
};

export default DeckManager;


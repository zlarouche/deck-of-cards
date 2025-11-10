import React, { useState, useEffect } from 'react';
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Divider,
  List,
  ListItem,
  ListItemText,
  Stack,
  Typography,
} from '@mui/material';
import { createGame, deleteGame, getGames } from '../services/api';
import { useGame } from '../context/GameContext';
import { Game } from '../types';

const GameManager: React.FC = () => {
  const { gameId, setGameId, triggerRefresh, replaceDecks, refreshTrigger } = useGame();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [gameList, setGameList] = useState<Game[]>([]);
  const [selectedGame, setSelectedGame] = useState<string | null>(null);

  const handleCreateGame = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await createGame();
      setGameId(response.gameId);
      triggerRefresh();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to create game');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteGame = async () => {
    if (!gameId) return;
    setLoading(true);
    setError(null);
    try {
      await deleteGame(gameId);
      setGameId(null);
      replaceDecks([]);
      setDeleteDialogOpen(false);
      triggerRefresh();
      await handleGetGames();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to delete game');
    } finally {
      setLoading(false);
    }
  };

  const handleGetGames = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await getGames();
      setGameList(response);
      if (response.length === 0) {
        setSelectedGame(null);
      }
    }
    catch (err: any) {
      setError(err.response?.data?.message || 'Failed to get games');
    }
    finally {
      setLoading(false);
    }
  }

  const handleSelectGame = async (gameId: string) => {
    if (!gameId) return;
    setLoading(true);
    setError(null);
    try {
      setGameId(gameId);
      triggerRefresh();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to select game');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    handleGetGames();
  }, [refreshTrigger]);

  useEffect(() => {
    if (selectedGame) {
      handleSelectGame(selectedGame);
    }
  }, [selectedGame]);

  return (
    <Card>
      <CardContent sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
        <Typography variant="h5">Game Management</Typography>

        {error && (
          <Alert severity="error" onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} alignItems="flex-start">
          <Button
            variant="contained"
            color="primary"
            onClick={handleCreateGame}
            disabled={loading}
          >
            Create New Game
          </Button>

            {gameId && (
              <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1} alignItems={{ xs: 'flex-start', sm: 'center' }}>
                <Typography variant="body1">
                  Active Game: <strong>{gameId}</strong>
                </Typography>
                <Button
                  variant="outlined"
                  color="error"
                  onClick={() => setDeleteDialogOpen(true)}
                  disabled={loading}
                >
                  Delete Game
                </Button>
              </Stack>
            )}
        </Stack>

        <Divider />

        <Box>
          {gameList.length === 0 ? (
            <Typography variant="body2" color="text.secondary">
              No games available yet. Create one to get started.
            </Typography>
          ) : (
            <List disablePadding>
              {gameList.map((game) => (
                <ListItem
                  key={game.id}
                  divider
                  secondaryAction={
                    <Button
                      variant={game.id === gameId ? 'contained' : 'outlined'}
                      color="primary"
                      size="small"
                      onClick={() => setSelectedGame(game.id)}
                      disabled={loading || game.id === gameId}
                    >
                      {game.id === gameId ? 'Selected' : 'Select'}
                    </Button>
                  }
                >
                  <ListItemText
                    primary={
                      <Typography variant="subtitle1" fontWeight={600}>
                        {game.id}
                      </Typography>
                    }
                    secondary={
                      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} sx={{ mt: 0.5 }}>
                        <Typography variant="body2" color="text.secondary">
                          Shoe Size: <strong>{game.shoeSize}</strong>
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                          Players: <strong>{game.playerCount}</strong>
                        </Typography>
                      </Stack>
                    }
                  />
                </ListItem>
              ))}
            </List>
          )}
        </Box>

        <Dialog open={deleteDialogOpen} onClose={() => setDeleteDialogOpen(false)}>
          <DialogTitle>Delete Game</DialogTitle>
          <DialogContent>
            <Typography>
              Are you sure you want to delete this game? This action cannot be undone.
            </Typography>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setDeleteDialogOpen(false)}>Cancel</Button>
            <Button onClick={handleDeleteGame} color="error" variant="contained">
              Delete
            </Button>
          </DialogActions>
        </Dialog>
      </CardContent>
    </Card>
  );
};

export default GameManager;


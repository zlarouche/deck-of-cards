import React, { useState, useEffect, useCallback } from 'react';
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
  TextField,
  Typography,
} from '@mui/material';
import { createGame, deleteGame, getGames, resetGame } from '../services/api';
import { useGame } from '../context/GameContext';
import { Game } from '../types';

const GameManager: React.FC = () => {
  const { gameId, gameName, setGameId, setGameName, triggerRefresh, replaceDecks, refreshTrigger } = useGame();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [gameList, setGameList] = useState<Game[]>([]);
  const [success, setSuccess] = useState<string | null>(null);
  const [newGameName, setNewGameName] = useState('');

  const handleCreateGame = async () => {
    setError(null);
    setSuccess(null);
    const trimmedName = newGameName.trim();
    if (!trimmedName) {
      setError('Please enter a game name.');
      return;
    }
    setLoading(true);
    try {
      const response = await createGame(trimmedName);
      setGameId(response.gameId);
      setGameName(response.name);
      setNewGameName('');
      triggerRefresh();
      setSuccess(`Created game "${response.name}".`);
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
    setSuccess(null);
    try {
      await deleteGame(gameId);
      setGameId(null);
      setGameName(null);
      replaceDecks([]);
      setDeleteDialogOpen(false);
      triggerRefresh();
      await handleGetGames();
      setSuccess('Game deleted.');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to delete game');
    } finally {
      setLoading(false);
    }
  };

  const handleGetGames = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await getGames();
      setGameList(response);
      if (gameId) {
        const activeGame = response.find((game) => game.id === gameId);
        setGameName(activeGame ? activeGame.name : null);
      } else {
        setGameName(null);
      }
    }
    catch (err: any) {
      setError(err.response?.data?.message || 'Failed to get games');
    }
    finally {
      setLoading(false);
    }
  }, [gameId, setGameName]);

  const handleSelectGame = async (gameToSelect: Game) => {
    if (!gameToSelect?.id) return;
    setLoading(true);
    setError(null);
    setSuccess(null);
    try {
      setGameId(gameToSelect.id);
      setGameName(gameToSelect.name);
      triggerRefresh();
      setSuccess(`Switched to game "${gameToSelect.name}".`);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to select game');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    handleGetGames();
  }, [handleGetGames, refreshTrigger]);

  const handleResetGame = async () => {
    if (!gameId) return;
    setLoading(true);
    setError(null);
    setSuccess(null);
    try {
      await resetGame(gameId);
      triggerRefresh();
      setSuccess('Game reset: all player cards returned to the shoe.');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to reset game');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card>
      <CardContent sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
        <Typography variant="h5">Game Management</Typography>

        {error && (
          <Alert severity="error" onClose={() => setError(null)}>
            {error}
          </Alert>
        )}
        {success && (
          <Alert severity="success" onClose={() => setSuccess(null)}>
            {success}
          </Alert>
        )}

        <Stack spacing={2}>
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} alignItems={{ xs: 'stretch', sm: 'center' }}>
            <TextField
              label="Game Name"
              value={newGameName}
              onChange={(event) => setNewGameName(event.target.value)}
              size="small"
              required
              fullWidth
              disabled={loading}
              placeholder="Enter a game name"
              autoComplete="off"
              sx={{ minWidth: { sm: 240 } }}
            />
            <Button
              variant="contained"
              color="primary"
              onClick={handleCreateGame}
              disabled={loading || newGameName.trim().length === 0}
              sx={{ alignSelf: { xs: 'stretch', sm: 'center' } }}
            >
              Create New Game
            </Button>
          </Stack>

          <Typography variant="body2" color="text.secondary">
            Name a game to create it, then manage it below.
          </Typography>

          {gameId && (
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1} alignItems={{ xs: 'flex-start', sm: 'center' }}>
              <Typography variant="body1">
                Active Game: <strong>{gameName ?? gameId}</strong>
              </Typography>
              <Button
                variant="outlined"
                color="primary"
                onClick={handleResetGame}
                disabled={loading}
              >
                Reset Game
              </Button>
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
                      onClick={() => handleSelectGame(game)}
                      disabled={loading || game.id === gameId}
                    >
                      {game.id === gameId ? 'Selected' : 'Select'}
                    </Button>
                  }
                >
                  <ListItemText
                    primary={
                      <Typography variant="subtitle1" fontWeight={600}>
                        {game.name ?? game.id}
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


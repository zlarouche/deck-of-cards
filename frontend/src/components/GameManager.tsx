import React, { useState } from 'react';
import {
  Box,
  Button,
  Card,
  CardContent,
  Typography,
  Alert,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from '@mui/material';
import { createGame, deleteGame } from '../services/api';
import { useGame } from '../context/GameContext';

const GameManager: React.FC = () => {
  const { gameId, setGameId } = useGame();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);

  const handleCreateGame = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await createGame();
      setGameId(response.gameId);
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
      setDeleteDialogOpen(false);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to delete game');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card>
      <CardContent>
        <Typography variant="h5" gutterBottom>
          Game Management
        </Typography>
        
        {error && (
          <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        {gameId ? (
          <Box>
            <Typography variant="body1" sx={{ mb: 2 }}>
              Game ID: <strong>{gameId}</strong>
            </Typography>
            <Button
              variant="contained"
              color="error"
              onClick={() => setDeleteDialogOpen(true)}
              disabled={loading}
            >
              Delete Game
            </Button>
          </Box>
        ) : (
          <Button
            variant="contained"
            color="primary"
            onClick={handleCreateGame}
            disabled={loading}
          >
            Create New Game
          </Button>
        )}

        <Dialog open={deleteDialogOpen} onClose={() => setDeleteDialogOpen(false)}>
          <DialogTitle>Delete Game</DialogTitle>
          <DialogContent>
            <Typography>Are you sure you want to delete this game? This action cannot be undone.</Typography>
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


import React, { useState } from 'react';
import {
  Box,
  Button,
  Card,
  CardContent,
  Typography,
  Alert,
  CircularProgress,
} from '@mui/material';
import ShuffleIcon from '@mui/icons-material/Shuffle';
import { shuffleGameDeck } from '../services/api';
import { useGame } from '../context/GameContext';

const ShuffleButton: React.FC = () => {
  const { gameId, triggerRefresh } = useGame();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  const handleShuffle = async () => {
    if (!gameId) {
      setError('Please create a game first');
      return;
    }
    setLoading(true);
    setError(null);
    setSuccess(false);
    try {
      await shuffleGameDeck(gameId);
      setSuccess(true);
      triggerRefresh();
      setTimeout(() => setSuccess(false), 3000);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to shuffle deck');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card>
      <CardContent>
        <Typography variant="h5" gutterBottom>
          Shuffle Deck
        </Typography>

        {error && (
          <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        {success && (
          <Alert severity="success" sx={{ mb: 2 }}>
            Deck shuffled successfully!
          </Alert>
        )}

        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <Button
            variant="contained"
            color="primary"
            startIcon={loading ? <CircularProgress size={20} /> : <ShuffleIcon />}
            onClick={handleShuffle}
            disabled={loading || !gameId}
            size="large"
          >
            {loading ? 'Shuffling...' : 'Shuffle Game Deck'}
          </Button>
          <Typography variant="body2" color="text.secondary">
            Uses Fisher-Yates shuffle algorithm
          </Typography>
        </Box>
      </CardContent>
    </Card>
  );
};

export default ShuffleButton;


import React, { useState } from 'react';
import {
  Alert,
  Button,
  Card,
  CardContent,
  CircularProgress,
  Stack,
  Typography,
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
      <CardContent sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
        <Typography variant="h5">Shuffle Deck</Typography>

        {error && (
          <Alert severity="error" onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        {success && (
          <Alert severity="success">
            Deck shuffled successfully!
          </Alert>
        )}

        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} alignItems={{ xs: 'stretch', sm: 'center' }}>
          <Button
            variant="contained"
            color="primary"
            startIcon={loading ? <CircularProgress size={20} /> : <ShuffleIcon />}
            onClick={handleShuffle}
            disabled={loading || !gameId}
            size="large"
          >
            {loading ? 'Shuffling...' : 'Shuffle Game Shoe'}
          </Button>
          <Typography variant="body2" color="text.secondary">
            Shuffling resets the shoe to a random order without affecting player hands.
          </Typography>
        </Stack>
      </CardContent>
    </Card>
  );
};

export default ShuffleButton;


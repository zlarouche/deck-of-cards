import React, { useState, useEffect } from 'react';
import {
  Box,
  Button,
  Card,
  CardContent,
  Typography,
  Alert,
  TextField,
  MenuItem,
  Select,
  FormControl,
  InputLabel,
} from '@mui/material';
import { dealCards, getPlayersSorted } from '../services/api';
import { useGame } from '../context/GameContext';

const CardDealer: React.FC = () => {
  const { gameId, refreshTrigger, triggerRefresh } = useGame();
  const [selectedPlayer, setSelectedPlayer] = useState('');
  const [count, setCount] = useState(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [players, setPlayers] = useState<any[]>([]);

  const loadPlayers = async () => {
    if (!gameId) return;
    try {
      const data = await getPlayersSorted(gameId);
      setPlayers(data);
      if (data.length > 0 && !selectedPlayer) {
        setSelectedPlayer(data[0].name);
      }
    } catch (err) {
      // Ignore errors when loading
    }
  };

  useEffect(() => {
    loadPlayers();
  }, [gameId, refreshTrigger]);

  const handleDealCards = async () => {
    if (!gameId) {
      setError('Please create a game first');
      return;
    }
    if (!selectedPlayer) {
      setError('Please select a player');
      return;
    }
    setLoading(true);
    setError(null);
    setSuccess(null);
    try {
      const dealtCards = await dealCards(gameId, {
        playerName: selectedPlayer,
        count: count,
      });
      setSuccess(`Dealt ${dealtCards.length} card(s) to ${selectedPlayer}`);
      triggerRefresh();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to deal cards');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card>
      <CardContent>
        <Typography variant="h5" gutterBottom>
          Deal Cards
        </Typography>

        {error && (
          <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        {success && (
          <Alert severity="success" sx={{ mb: 2 }} onClose={() => setSuccess(null)}>
            {success}
          </Alert>
        )}

        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
          <FormControl fullWidth>
            <InputLabel>Player</InputLabel>
            <Select
              value={selectedPlayer}
              onChange={(e) => setSelectedPlayer(e.target.value)}
              disabled={loading || !gameId || players.length === 0}
              label="Player"
            >
              {players.map((player) => (
                <MenuItem key={player.name} value={player.name}>
                  {player.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <TextField
            label="Number of Cards"
            type="number"
            value={count}
            onChange={(e) => setCount(parseInt(e.target.value) || 1)}
            inputProps={{ min: 1, max: 52 }}
            disabled={loading || !gameId}
            fullWidth
          />

          <Button
            variant="contained"
            color="primary"
            onClick={handleDealCards}
            disabled={loading || !gameId || players.length === 0}
          >
            Deal Cards
          </Button>
        </Box>
      </CardContent>
    </Card>
  );
};

export default CardDealer;


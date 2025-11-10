import React, { useState, useEffect } from 'react';
import {
  Alert,
  Card,
  CardContent,
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  Stack,
  Typography,
} from '@mui/material';
import { getPlayerCards, getPlayersSorted } from '../services/api';
import { useGame } from '../context/GameContext';
import { Card as CardType } from '../types';

const PlayerHand: React.FC = () => {
  const { gameId, refreshTrigger } = useGame();
  const [selectedPlayer, setSelectedPlayer] = useState('');
  const [cards, setCards] = useState<CardType[]>([]);
  const [players, setPlayers] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);

  const loadPlayers = async () => {
    if (!gameId) return;
    try {
      const data = await getPlayersSorted(gameId);
      setPlayers(data);
      if (data.length === 0) {
        setSelectedPlayer('');
        setCards([]);
      }
      if (data.length > 0 && !selectedPlayer) {
        setSelectedPlayer(data[0].name);
      }
    } catch (err) {
      // Ignore errors when loading
    }
  };

  const loadPlayerCards = async () => {
    if (!gameId || !selectedPlayer) return;
    setLoading(true);
    try {
      const data = await getPlayerCards(gameId, selectedPlayer);
      setCards(data);
    } catch (err) {
      setCards([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPlayers();
  }, [gameId, refreshTrigger]);

  useEffect(() => {
    loadPlayerCards();
  }, [selectedPlayer, refreshTrigger]);

  const getCardColor = (suit: string) => {
    switch (suit) {
      case 'HEARTS':
      case 'DIAMONDS':
        return '#d32f2f';
      case 'SPADES':
      case 'CLUBS':
        return '#000000';
      default:
        return '#666666';
    }
  };

  return (
    <Card>
      <CardContent sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
        <Typography variant="h5">Player's Hand</Typography>

        {players.length === 0 ? (
          <Alert severity="info">Add players to view their hands.</Alert>
        ) : (
          <Stack spacing={3}>
            <FormControl fullWidth>
              <InputLabel>Player</InputLabel>
              <Select
                value={selectedPlayer}
                onChange={(e) => setSelectedPlayer(e.target.value)}
                label="Player"
              >
                {players.map((player) => (
                  <MenuItem key={player.name} value={player.name}>
                    {player.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            {cards.length === 0 ? (
              <Alert severity="info">Selected player has no cards yet.</Alert>
            ) : (
              <Stack spacing={2}>
                <Typography variant="body2" color="text.secondary">
                  {cards.length} card(s) • Total Value:{' '}
                  {cards.reduce((sum, card) => sum + card.value, 0)}
                </Typography>
                <Grid container spacing={1}>
                  {cards.map((card, index) => (
                    <Grid item xs={6} sm={4} md={3} key={`${card.displayName}-${index}`}>
                      <Paper
                        elevation={2}
                        sx={{
                          p: 1.5,
                          textAlign: 'center',
                          border: `1px solid ${getCardColor(card.suit)}`,
                        }}
                      >
                        <Typography
                          variant="body2"
                          sx={{ fontWeight: 600, color: getCardColor(card.suit) }}
                        >
                          {card.displayName}
                        </Typography>
                        <Typography variant="caption" color="text.secondary">
                          Value: {card.value}
                        </Typography>
                      </Paper>
                    </Grid>
                  ))}
                </Grid>
              </Stack>
            )}
          </Stack>
        )}
      </CardContent>
    </Card>
  );
};

export default PlayerHand;


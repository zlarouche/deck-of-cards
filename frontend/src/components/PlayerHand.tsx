import React, { useState, useEffect } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Alert,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Grid,
  Paper,
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
      <CardContent>
        <Typography variant="h5" gutterBottom>
          Player's Hand
        </Typography>

        {players.length === 0 ? (
          <Alert severity="info">No players in the game</Alert>
        ) : (
          <>
            <FormControl fullWidth sx={{ mb: 2 }}>
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
              <Alert severity="info">No cards in hand</Alert>
            ) : (
              <Box>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  {cards.length} card(s) - Total Value: {cards.reduce((sum, card) => sum + card.value, 0)}
                </Typography>
                <Grid container spacing={1}>
                  {cards.map((card, index) => (
                    <Grid item xs={6} sm={4} md={3} key={index}>
                      <Paper
                        elevation={2}
                        sx={{
                          p: 1,
                          textAlign: 'center',
                          border: `1px solid ${getCardColor(card.suit)}`,
                        }}
                      >
                        <Typography
                          variant="body2"
                          sx={{ fontWeight: 'bold', color: getCardColor(card.suit) }}
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
              </Box>
            )}
          </>
        )}
      </CardContent>
    </Card>
  );
};

export default PlayerHand;


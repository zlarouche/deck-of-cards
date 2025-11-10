import React, { useState, useEffect } from 'react';
import {
  Alert,
  Box,
  Card,
  CardContent,
  Grid,
  Paper,
  Stack,
  Tab,
  Tabs,
  Typography,
} from '@mui/material';
import { getUndealtCardsBySuit, getUndealtCardsCount } from '../services/api';
import { useGame } from '../context/GameContext';
import { Suit, FaceValue } from '../types';

const UndealtCardsView: React.FC = () => {
  const { gameId, refreshTrigger } = useGame();
  const [tabValue, setTabValue] = useState(0);
  const [suitCounts, setSuitCounts] = useState<Record<Suit, number> | null>(null);
  const [cardCounts, setCardCounts] = useState<any>(null);
  const [loading, setLoading] = useState(false);

  const loadSuitCounts = async () => {
    if (!gameId) return;
    setLoading(true);
    try {
      const data = await getUndealtCardsBySuit(gameId);
      setSuitCounts(data.suitCounts);
    } catch (err) {
      setSuitCounts(null);
    } finally {
      setLoading(false);
    }
  };

  const loadCardCounts = async () => {
    if (!gameId) return;
    setLoading(true);
    try {
      const data = await getUndealtCardsCount(gameId);
      setCardCounts(data.cardCounts);
    } catch (err) {
      setCardCounts(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (tabValue === 0) {
      loadSuitCounts();
    } else {
      loadCardCounts();
    }
  }, [gameId, refreshTrigger, tabValue]);

  const getSuitColor = (suit: Suit) => {
    switch (suit) {
      case Suit.HEARTS:
      case Suit.DIAMONDS:
        return '#d32f2f';
      case Suit.SPADES:
      case Suit.CLUBS:
        return '#000000';
      default:
        return '#666666';
    }
  };

  return (
    <Card>
      <CardContent sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
        <Typography variant="h5">Undealt Cards</Typography>

        {!gameId ? (
          <Alert severity="info">Create a game to view undealt cards.</Alert>
        ) : (
          <Stack spacing={3}>
            <Tabs value={tabValue} onChange={(e, newValue) => setTabValue(newValue)}>
              <Tab label="By Suit" />
              <Tab label="Detailed Count" />
            </Tabs>

            {tabValue === 0 && (
              <Box>
                {suitCounts ? (
                  <Grid container spacing={2}>
                    {Object.entries(suitCounts).map(([suit, count]) => (
                      <Grid item xs={6} sm={3} key={suit}>
                        <Paper
                          elevation={2}
                          sx={{
                            p: 2,
                            textAlign: 'center',
                            border: `2px solid ${getSuitColor(suit as Suit)}`,
                          }}
                        >
                          <Typography
                            variant="h6"
                            sx={{ color: getSuitColor(suit as Suit), fontWeight: 'bold' }}
                          >
                            {suit}
                          </Typography>
                          <Typography variant="h4">{count}</Typography>
                          <Typography variant="caption" color="text.secondary">
                            cards
                          </Typography>
                        </Paper>
                      </Grid>
                    ))}
                  </Grid>
                ) : (
                  <Alert severity="info">No data available</Alert>
                )}
              </Box>
            )}

            {tabValue === 1 && (
              <Box>
                {cardCounts ? (
                  <Grid container spacing={2}>
                    {Object.entries(cardCounts).map(([suit, faceValues]: [string, any]) => (
                      <Grid item xs={12} key={suit}>
                        <Paper elevation={1} sx={{ p: 2 }}>
                          <Typography
                            variant="h6"
                            gutterBottom
                            sx={{ color: getSuitColor(suit as Suit), fontWeight: 'bold' }}
                          >
                            {suit}
                          </Typography>
                          <Grid container spacing={1}>
                            {Object.entries(faceValues).map(([faceValue, count]: [string, any]) => (
                              count > 0 && (
                                <Grid item key={faceValue}>
                                  <Paper
                                    variant="outlined"
                                    sx={{
                                      p: 1,
                                      minWidth: 80,
                                      textAlign: 'center',
                                    }}
                                  >
                                    <Typography variant="body2">{faceValue}</Typography>
                                    <Typography variant="h6">{count}</Typography>
                                  </Paper>
                                </Grid>
                              )
                            ))}
                          </Grid>
                        </Paper>
                      </Grid>
                    ))}
                  </Grid>
                ) : (
                  <Alert severity="info">No data available</Alert>
                )}
              </Box>
            )}
          </Stack>
        )}
      </CardContent>
    </Card>
  );
};

export default UndealtCardsView;


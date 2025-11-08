import React, { useState, useEffect } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Alert,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Chip,
} from '@mui/material';
import { getPlayersSorted } from '../services/api';
import { useGame } from '../context/GameContext';

const PlayersLeaderboard: React.FC = () => {
  const { gameId, refreshTrigger } = useGame();
  const [players, setPlayers] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);

  const loadPlayers = async () => {
    if (!gameId) {
      setPlayers([]);
      return;
    }
    setLoading(true);
    try {
      const data = await getPlayersSorted(gameId);
      setPlayers(data);
    } catch (err) {
      setPlayers([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPlayers();
  }, [gameId, refreshTrigger]);

  return (
    <Card>
      <CardContent>
        <Typography variant="h5" gutterBottom>
          Players Leaderboard
        </Typography>

        {!gameId ? (
          <Alert severity="info">Please create a game first</Alert>
        ) : players.length === 0 ? (
          <Alert severity="info">No players in the game</Alert>
        ) : (
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell><strong>Rank</strong></TableCell>
                  <TableCell><strong>Player Name</strong></TableCell>
                  <TableCell align="right"><strong>Hand Value</strong></TableCell>
                  <TableCell align="right"><strong>Cards</strong></TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {players.map((player, index) => (
                  <TableRow key={player.name}>
                    <TableCell>
                      <Chip
                        label={index + 1}
                        color={index === 0 ? 'primary' : 'default'}
                        size="small"
                      />
                    </TableCell>
                    <TableCell>{player.name}</TableCell>
                    <TableCell align="right">
                      <strong>{player.handValue}</strong>
                    </TableCell>
                    <TableCell align="right">{player.handSize}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </CardContent>
    </Card>
  );
};

export default PlayersLeaderboard;


import React, { useState, useEffect } from 'react';
import {
  Box,
  Button,
  Card,
  CardContent,
  Typography,
  Alert,
  TextField,
  List,
  ListItem,
  ListItemText,
  IconButton,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { addPlayer, removePlayer, getPlayersSorted } from '../services/api';
import { useGame } from '../context/GameContext';

const PlayerManager: React.FC = () => {
  const { gameId, refreshTrigger, triggerRefresh } = useGame();
  const [playerName, setPlayerName] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [players, setPlayers] = useState<any[]>([]);

  const loadPlayers = async () => {
    if (!gameId) return;
    try {
      const data = await getPlayersSorted(gameId);
      setPlayers(data);
    } catch (err) {
      // Ignore errors when loading
    }
  };

  useEffect(() => {
    loadPlayers();
  }, [gameId, refreshTrigger]);

  const handleAddPlayer = async () => {
    if (!gameId) {
      setError('Please create a game first');
      return;
    }
    if (!playerName.trim()) {
      setError('Player name cannot be empty');
      return;
    }
    setLoading(true);
    setError(null);
    try {
      await addPlayer(gameId, { playerName: playerName.trim() });
      triggerRefresh();
      setPlayerName('');
      await loadPlayers();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to add player');
    } finally {
      setLoading(false);
    }
  };

  const handleRemovePlayer = async (name: string) => {
    if (!gameId) return;
    setLoading(true);
    setError(null);
    try {
      await removePlayer(gameId, name);
      triggerRefresh();
      await loadPlayers();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to remove player');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card>
      <CardContent>
        <Typography variant="h5" gutterBottom>
          Player Management
        </Typography>

        {error && (
          <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        <Box sx={{ display: 'flex', gap: 1, mb: 2 }}>
          <TextField
            label="Player Name"
            value={playerName}
            onChange={(e) => setPlayerName(e.target.value)}
            onKeyPress={(e) => e.key === 'Enter' && handleAddPlayer()}
            disabled={loading || !gameId}
            fullWidth
          />
          <Button
            variant="contained"
            color="primary"
            onClick={handleAddPlayer}
            disabled={loading || !gameId}
          >
            Add Player
          </Button>
        </Box>

        {players.length > 0 && (
          <Box>
            <Typography variant="h6" gutterBottom>
              Players ({players.length})
            </Typography>
            <List>
              {players.map((player) => (
                <ListItem
                  key={player.name}
                  secondaryAction={
                    <IconButton
                      edge="end"
                      onClick={() => handleRemovePlayer(player.name)}
                      disabled={loading}
                      color="error"
                    >
                      <DeleteIcon />
                    </IconButton>
                  }
                >
                  <ListItemText
                    primary={player.name}
                    secondary={`Hand Value: ${player.handValue} (${player.handSize} cards)`}
                  />
                </ListItem>
              ))}
            </List>
          </Box>
        )}
      </CardContent>
    </Card>
  );
};

export default PlayerManager;


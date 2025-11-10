import React, { useState, useEffect } from 'react';
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Divider,
  IconButton,
  List,
  ListItem,
  ListItemText,
  Stack,
  TextField,
  Typography,
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
      <CardContent sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
        <Typography variant="h5">Player Management</Typography>

        {error && (
          <Alert severity="error" onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        <Stack
          direction={{ xs: 'column', sm: 'row' }}
          spacing={1.5}
          alignItems={{ xs: 'stretch', sm: 'center' }}
        >
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
        </Stack>

        <Divider />

        <Box>
          <Typography variant="h6" gutterBottom>
            Players
          </Typography>
          {players.length === 0 ? (
            <Typography variant="body2" color="text.secondary">
              No players have been added yet. Add a player to begin dealing cards.
            </Typography>
          ) : (
            <List disablePadding>
              {players.map((player) => (
                <ListItem
                  key={player.name}
                  divider
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
          )}
        </Box>
      </CardContent>
    </Card>
  );
};

export default PlayerManager;


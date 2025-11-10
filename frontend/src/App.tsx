import React from 'react';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Container from '@mui/material/Container';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import { GameProvider } from './context/GameContext';
import GameManager from './components/GameManager';
import DeckManager from './components/DeckManager';
import PlayerManager from './components/PlayerManager';
import CardDealer from './components/CardDealer';
import PlayerHand from './components/PlayerHand';
import PlayersLeaderboard from './components/PlayersLeaderboard';
import UndealtCardsView from './components/UndealtCardsView';
import ShuffleButton from './components/ShuffleButton';

const theme = createTheme({
  palette: {
    primary: {
      main: '#005a00',
    },
    secondary: {
      main: '#835809',
    },
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <GameProvider>
        <AppBar position="static">
          <Toolbar>
            <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
              Deck of Cards Game
            </Typography>
          </Toolbar>
        </AppBar>
        <Container maxWidth="xl" sx={{ mt: 4, mb: 4 }}>
          <Grid container spacing={3}>
            <Grid item xs={12}>
              <GameManager />
            </Grid>
            <Grid item xs={12} md={6}>
              <DeckManager />
            </Grid>
            <Grid item xs={12} md={6}>
              <ShuffleButton />
            </Grid>
            <Grid item xs={12} md={6}>
              <PlayerManager />
            </Grid>
            <Grid item xs={12} md={6}>
              <CardDealer />
            </Grid>
            <Grid item xs={12} md={6}>
              <PlayersLeaderboard />
            </Grid>
            <Grid item xs={12} md={6}>
              <PlayerHand />
            </Grid>
            <Grid item xs={12}> 
              <UndealtCardsView />
            </Grid>
          </Grid>
        </Container>
      </GameProvider>
    </ThemeProvider>
  );
}

export default App;


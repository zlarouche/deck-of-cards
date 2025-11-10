import axios from 'axios';
import {
  CreateGameResponse,
  CreateDeckResponse,
  AddDeckRequest,
  AddPlayerRequest,
  DealCardsRequest,
  Card,
  Player,
  Game,
  UndealtCardsBySuit,
  UndealtCardsCount
} from '../types';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Games
export const createGame = async (): Promise<CreateGameResponse> => {
  const response = await apiClient.post<CreateGameResponse>('/games');
  return response.data;
};

export const deleteGame = async (gameId: string): Promise<void> => {
  await apiClient.delete(`/games/${gameId}`);
};

export const getGames = async (): Promise<Game[]> => {
  const response = await apiClient.get<Game[]>(`/games`);
  return response.data;
};

export const addDeckToGame = async (gameId: string, request: AddDeckRequest): Promise<void> => {
  await apiClient.post(`/games/${gameId}/decks`, request);
};

export const getAddedDeckIds = async (gameId: string): Promise<string[]> => {
  const response = await apiClient.get<string[]>(`/games/${gameId}/decks`);
  return response.data;
}

export const getUnassignedDeckIds = async (): Promise<string[]> => {
  const response = await apiClient.get<string[]>(`/decks/unassigned`);
  return response.data;
};

export const addPlayer = async (gameId: string, request: AddPlayerRequest): Promise<void> => {
  await apiClient.post(`/games/${gameId}/players`, request);
};

export const removePlayer = async (gameId: string, playerName: string): Promise<void> => {
  await apiClient.delete(`/games/${gameId}/players/${encodeURIComponent(playerName)}`);
};

export const dealCards = async (gameId: string, request: DealCardsRequest): Promise<Card[]> => {
  const response = await apiClient.post<Card[]>(`/games/${gameId}/deal`, request);
  return response.data;
};

export const getPlayerCards = async (gameId: string, playerName: string): Promise<Card[]> => {
  const response = await apiClient.get<Card[]>(`/games/${gameId}/players/${encodeURIComponent(playerName)}/cards`);
  return response.data;
};

export const getPlayersSorted = async (gameId: string): Promise<Player[]> => {
  const response = await apiClient.get<Player[]>(`/games/${gameId}/players`);
  return response.data;
};

export const getUndealtCardsBySuit = async (gameId: string): Promise<UndealtCardsBySuit> => {
  const response = await apiClient.get<UndealtCardsBySuit>(`/games/${gameId}/undealt/suits`);
  return response.data;
};

export const getUndealtCardsCount = async (gameId: string): Promise<UndealtCardsCount> => {
  const response = await apiClient.get<UndealtCardsCount>(`/games/${gameId}/undealt/cards`);
  return response.data;
};

export const shuffleGameDeck = async (gameId: string): Promise<void> => {
  await apiClient.post(`/games/${gameId}/shuffle`);
};

// Decks
export const createDeck = async (): Promise<CreateDeckResponse> => {
  const response = await apiClient.post<CreateDeckResponse>('/decks');
  return response.data;
};


export enum Suit {
  HEARTS = 'HEARTS',
  SPADES = 'SPADES',
  CLUBS = 'CLUBS',
  DIAMONDS = 'DIAMONDS'
}

export enum FaceValue {
  ACE = 'ACE',
  TWO = 'TWO',
  THREE = 'THREE',
  FOUR = 'FOUR',
  FIVE = 'FIVE',
  SIX = 'SIX',
  SEVEN = 'SEVEN',
  EIGHT = 'EIGHT',
  NINE = 'NINE',
  TEN = 'TEN',
  JACK = 'JACK',
  QUEEN = 'QUEEN',
  KING = 'KING'
}

export interface Card {
  suit: Suit;
  faceValue: FaceValue;
  value: number;
  displayName: string;
}

export interface Player {
  name: string;
  hand: Card[];
  handValue: number;
  handSize: number;
}

export interface Game {
  id: string;
  name: string;
  shoe: Card[];
  players: Record<string, Player>;
  addedDeckIds: string[];
  shoeSize: number;
  playerCount: number;
}

export interface CreateGameResponse {
  gameId: string;
  name: string;
}

export interface CreateDeckResponse {
  deckId: string;
}

export interface AddDeckRequest {
  deckId: string;
}

export interface AddPlayerRequest {
  playerName: string;
}

export interface DealCardsRequest {
  playerName: string;
  count: number;
}

export interface UndealtCardsBySuit {
  suitCounts: Record<Suit, number>;
}

export interface UndealtCardsCount {
  cardCounts: Record<Suit, Record<FaceValue, number>>;
}


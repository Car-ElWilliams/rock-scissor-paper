import { GameResults, RockPaperScissor } from "src/constants/enums";

export interface ResultBody {
  mostCommonPick: RockPaperScissor[];
  winPercentage: number;
  totalWins: number;
  historicPicks: RockPaperScissor[];
  totalLosses: number;
  totalPlayTimeInS: number;
  userId: string;
}

export interface UpdateResultBody {
  didUserWin: boolean;
  picks: RockPaperScissor[];
  playTime: number;
}
export interface PostResultBody extends UpdateResultBody {
  userId: string;
}

export interface UpdateGameSessionBody {
  userChoice: RockPaperScissor;
}

export interface PostGameSessionBody {
  userChoice: RockPaperScissor;
  userId: string;
}

export interface GameSessionResponse {
  userId: string;
  roundResult: GameResults;
  userScore: number;
  computerScore: number;
  hasGameEnded: boolean;
  computerChoice: RockPaperScissor;
}

import { RockPaperScissor } from "src/constants/enums";

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

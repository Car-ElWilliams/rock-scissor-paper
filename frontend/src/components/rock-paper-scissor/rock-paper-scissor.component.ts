import { Component } from '@angular/core';
import { RockPaperScissor, GameResults } from 'src/constants/enums';
import { DataService } from 'src/services/data.service';
import * as uuid from 'uuid';

type RoundResult = {
  userChoice: RockPaperScissor;
  computerChoice: RockPaperScissor;
  result: GameResults;
};

type UserPicks = {
  current: typeof RockPaperScissor;
  previous: RockPaperScissor[];
};

@Component({
  selector: 'rock-paper-scissor',
  templateUrl: './rock-paper-scissor.component.html',
  styleUrls: ['./rock-paper-scissor.component.css'],
})
export class RockPaperScissorComponent {
  computerScore: number = 0;
  countdown: number = 3;
  hasGameStarted: boolean = false;
  roundResult: RoundResult | null = null;
  showPopUp: boolean = false;
  userScore: number = 0;
  userPicks: UserPicks = { current: RockPaperScissor, previous: [] };
  buttons: RockPaperScissor[] = Object.values(RockPaperScissor);
  userId: string = '';
  roundPlayTime: number = 0;

  constructor(private dataService: DataService) {}

  ngOnInit() {
    const userId = sessionStorage.getItem('userId');

    if (!userId) {
      const newUserId = uuid.v4();
      sessionStorage.setItem('userId', newUserId);

      this.dataService.postUserResult({
        userId: newUserId,
        historicPicks: [],
        mostCommonPick: [],
        totalLosses: 0,
        totalPlayTimeInS: 0,
        totalWins: 0,
        winLoseRatio: 0,
      });

      this.userId = newUserId;
    } else {
      this.userId = userId;
    }
  }

  play(userChoice: RockPaperScissor): void {
    this.roundResult = null;
    this.hasGameStarted = true;

    const choices = Object.values(RockPaperScissor);
    const computerChoice = choices[Math.floor(Math.random() * choices.length)];

    const elapsedGameTime = setInterval(() => this.roundPlayTime++, 1000);
    const countdown = setInterval(() => {
      this.countdown--;

      if (this.countdown === 0) {
        clearInterval(countdown);

        const result = this.determineWinner(userChoice, computerChoice);
        this.updateScore(result);

        this.userPicks.previous.push(userChoice);
        this.roundResult = { computerChoice, userChoice, result };
        this.countdown = 3;
        this.hasGameStarted = false;

        const hasGameEnded = this.userScore === 3 || this.computerScore === 3;

        if (hasGameEnded) {
          this.updateAnalytics(result);

          clearInterval(elapsedGameTime);
          setTimeout(() => {
            this.showPopUp = true;
          }, 500);
        }
      }
    }, 1000);
  }

  determineWinner(
    userChoice: RockPaperScissor,
    computerChoice: string
  ): RoundResult['result'] {
    const userWon =
      (userChoice === RockPaperScissor.ROCK &&
        computerChoice === RockPaperScissor.SCISSORS) ||
      (userChoice === RockPaperScissor.PAPER &&
        computerChoice === RockPaperScissor.ROCK) ||
      (userChoice === RockPaperScissor.SCISSORS &&
        computerChoice === RockPaperScissor.PAPER);

    const tie = userChoice === computerChoice;

    if (tie) {
      return GameResults.TIE;
    } else if (userWon) {
      return GameResults.USER_WIN;
    } else {
      return GameResults.COMPUTER_WIN;
    }
  }

  updateScore(result: GameResults): void {
    if (result === GameResults.USER_WIN) {
      this.userScore++;
    } else if (result === GameResults.COMPUTER_WIN) {
      this.computerScore++;
    }
  }

  updateAnalytics(result: GameResults): void {
    const didUserWin = result === GameResults.USER_WIN;

    this.dataService.updateUserResult(
      {
        didUserWin,
        picks: this.userPicks.previous,
        playTime: this.roundPlayTime,
      },
      this.userId
    );
  }

  newGame(): void {
    this.userScore = 0;
    this.computerScore = 0;
    this.countdown = 3;
    this.showPopUp = false;
    this.userPicks.previous = [];
    this.roundResult = null;
    this.roundPlayTime = 0;
  }
}

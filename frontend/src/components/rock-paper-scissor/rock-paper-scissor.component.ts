import { Component, OnChanges, SimpleChange } from "@angular/core";
import { RockPaperScissor, GameResults } from "src/constants/enums";
import { DataService } from "src/services/data.service";
import { ToastrService } from "ngx-toastr";
import * as uuid from "uuid";

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
  selector: "rock-paper-scissor",
  templateUrl: "./rock-paper-scissor.component.html",
  styleUrls: ["./rock-paper-scissor.component.css"],
})
export class RockPaperScissorComponent {
  computerScore: number = 0;
  countdown: number = 3;
  hasRoundStarted: boolean = false;
  roundResult: RoundResult | null = null;
  showPopUp: boolean = false;
  userScore: number = 0;
  userPicks: UserPicks = { current: RockPaperScissor, previous: [] };
  buttons: RockPaperScissor[] = Object.values(RockPaperScissor);
  userId: string = "";
  elapsedPlayTime: number = 0;
  isServerConnection: boolean = false;

  constructor(
    private dataService: DataService,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.checkConnectionToServer();

    const userId = localStorage.getItem("userId");

    if (!userId) {
      const newUserId = uuid.v4();
      localStorage.setItem("userId", newUserId);

      this.userId = newUserId;
    } else {
      this.userId = userId;
    }
  }

  async checkConnectionToServer() {
    const result = await this.dataService.isConnectedToServer();

    if (!result) {
      this.showError();
    }
  }

  showError() {
    this.toastr.error(
      "Failed to connect to server, stats will not be saved",
      "SERVER ERROR",
      {
        disableTimeOut: true,
      }
    );
  }

  play(userChoice: RockPaperScissor): void {
    this.roundResult = null;
    this.hasRoundStarted = true;

    const choices = Object.values(RockPaperScissor);
    const computerChoice = choices[Math.floor(Math.random() * choices.length)];

    if (this.elapsedPlayTime === 0) {
      const stopWatch = setInterval(() => {
        this.showPopUp && clearInterval(stopWatch);
        this.elapsedPlayTime++;
      }, 1000);
    }

    const countdown = setInterval(() => {
      this.countdown--;

      if (this.countdown === 0) {
        clearInterval(countdown);

        const result = this.determineWinner(userChoice, computerChoice);
        this.updateScore(result);

        this.userPicks.previous.push(userChoice);
        this.roundResult = { computerChoice, userChoice, result };
        this.countdown = 3;
        this.hasRoundStarted = false;

        const hasGameEnded = this.userScore === 3 || this.computerScore === 3;

        if (hasGameEnded) {
          this.handleGameResults(result);

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
  ): RoundResult["result"] {
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

  async handleGameResults(result: GameResults): Promise<void> {
    const userEntryExists = await this.dataService.getUserResult(this.userId);

    if (userEntryExists) {
      await this.updateUserStats(result);
    } else {
      await this.postUserStats(result);
    }
  }

  async updateUserStats(result: GameResults) {
    const didUserWin = result === GameResults.USER_WIN;

    await this.dataService.updateUserResult(
      {
        didUserWin,
        picks: this.userPicks.previous,
        playTime: this.elapsedPlayTime,
      },
      this.userId
    );
  }

  async postUserStats(result: GameResults) {
    const userWon = result === GameResults.USER_WIN;

    await this.dataService.postNewUser({
      userId: this.userId,
      didUserWin: userWon,
      playTime: this.elapsedPlayTime,
      picks: this.userPicks.previous,
    });
  }

  newGame(): void {
    this.userScore = 0;
    this.computerScore = 0;
    this.countdown = 3;
    this.showPopUp = false;
    this.userPicks.previous = [];
    this.roundResult = null;
    this.elapsedPlayTime = 0;
  }
}

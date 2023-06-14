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
  current: RockPaperScissor | null;
  previous: RockPaperScissor[] | null;
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
  userPicks: UserPicks = {
    current: null,
    previous: [],
  };
  buttons: RockPaperScissor[] = Object.values(RockPaperScissor);
  userId: string = "";
  elapsedPlayTime: number = 0;
  isServerConnection: boolean = false;
  hasGameEnded: boolean | null = null;

  constructor(
    private dataService: DataService,
    private toastr: ToastrService
  ) {}

  async ngOnInit() {
    this.checkConnectionToServer();

    const userId = localStorage.getItem("userId");

    if (!userId) {
      const newUserId = uuid.v4();
      localStorage.setItem("userId", newUserId);

      this.userId = newUserId;
    } else {
      this.userId = userId;
      const gameSession = await this.getGameSession(userId);

      if (gameSession) {
        this.userScore = gameSession.userScore;
        this.computerScore = gameSession.computerScore;
        this.userScore = gameSession.userScore;
        this.hasGameEnded = gameSession.hasGameEnded;
      }
    }
  }

  async checkConnectionToServer() {
    const connection = await this.dataService.isConnectedToServer();

    if (!connection) {
      this.showError(
        "Failed to connect to server. No new games can be started",
        "SERVER ERROR"
      );
    }
  }

  showError(message: string, title: string) {
    this.toastr.error(message, title, {
      disableTimeOut: true,
    });
  }

  async play(userChoice: RockPaperScissor) {
    this.roundResult = null;
    this.hasRoundStarted = true;

    const isGameSession = await this.dataService.getUserGameSession(
      this.userId
    );

    const result = isGameSession
      ? await this.updateGameSession(userChoice)
      : await this.startNewGameSession(userChoice);

    if (!result) {
      this.showError("ERROR", "Failed to get response from server");
      throw new Error("Failed to get api response");
    }

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

        const {
          userScore,
          computerChoice,
          computerScore,
          hasGameEnded,
          roundResult,
        } = result;

        this.userScore = userScore;
        this.computerScore = computerScore;
        this.roundResult = {
          computerChoice: computerChoice,
          userChoice,
          result: roundResult,
        };

        this.userPicks.previous && this.userPicks.previous.push(userChoice);
        this.countdown = 3;
        this.hasRoundStarted = false;
        this.hasGameEnded = hasGameEnded;

        if (hasGameEnded) {
          this.handleGameResults(roundResult);

          setTimeout(() => {
            this.showPopUp = true;
          }, 250);
        }
      }
    }, 1000);
  }

  async handleGameResults(result: GameResults): Promise<void> {
    const userEntryExists = await this.dataService.getUserResult(this.userId);

    console.log(userEntryExists);

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
        picks: this.userPicks.previous ?? [],
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
      picks: this.userPicks.previous ?? [],
    });
  }

  async startNewGameSession(userChoice: RockPaperScissor) {
    return await this.dataService.startNewGameSession({
      userChoice,
      userId: this.userId,
    });
  }

  async updateGameSession(userChoice: RockPaperScissor) {
    return await this.dataService.updateGameSession(
      { userChoice },
      this.userId
    );
  }

  async getGameSession(userId: string) {
    return await this.dataService.getUserGameSession(userId);
  }

  newGame(): void {
    this.countdown = 3;
    this.showPopUp = false;
    this.userPicks.previous = [];
    this.elapsedPlayTime = 0;
  }
}

import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { RockPaperScissor } from 'src/constants/enums';

const SVG_HAND = Object.freeze({
  ROCK: {
    computer: '/assets/svg/rock_left.svg',
    user: '/assets/svg/rock_right.svg',
  },
  SCISSOR: {
    computer: '/assets/svg/scissor_left.svg',
    user: '/assets/svg/scissor_right.svg',
  },
  PAPER: {
    computer: '/assets/svg/paper_left.svg',
    user: '/assets/svg/paper_right.svg',
  },
});

@Component({
  selector: 'hand-pick-svg',
  template: `<div
    [style]="{
      flexDirection: 'row',
      display: 'flex',
      flexGrow: 1,
      flexWrap: 'nowrap',
      justifyContent: 'space-between',
      width: '110%',
      marginLeft: '-5%',
      alignItems: 'center'
    }"
  >
    <img [style]="{ maxWidth: '40%' }" src="{{ hand.computer }}" alt="" />
    <h1 style="font-size: 4em" *ngIf="hasStarted">{{ countdown }}</h1>
    <img [style]="{ maxWidth: '40%' }" src="{{ hand.user }}" alt="" />
  </div> `,
})
export class HandGestureComponent implements OnChanges {
  @Input() hasStarted: boolean = false;
  @Input() countdown: number = 3;
  @Input() computerChoice?: RockPaperScissor = RockPaperScissor.ROCK;
  @Input() userChoice?: RockPaperScissor = RockPaperScissor.ROCK;

  hand = {
    computer: SVG_HAND[RockPaperScissor.ROCK].computer,
    user: SVG_HAND[RockPaperScissor.ROCK].user,
  };

  ngOnChanges(changes: SimpleChanges) {
    if (changes) {
      changes['computerChoice'] &&
        this.newHand('computer', changes['computerChoice'].currentValue);
      changes['userChoice'] &&
        this.newHand('user', changes['userChoice'].currentValue);
    }
  }

  newHand(type: 'user' | 'computer', result?: RockPaperScissor): void {
    if (this.countdown > 0) this.hand[type] = SVG_HAND.ROCK[type];

    switch (result) {
      case RockPaperScissor.PAPER:
        this.hand[type] = SVG_HAND.PAPER[type];
        break;
      case RockPaperScissor.ROCK:
        this.hand[type] = SVG_HAND.ROCK[type];
        break;
      case RockPaperScissor.SCISSORS:
        this.hand[type] = SVG_HAND.SCISSOR[type];
        break;

      default:
        this.hand[type] = SVG_HAND.ROCK[type];
    }
  }
}

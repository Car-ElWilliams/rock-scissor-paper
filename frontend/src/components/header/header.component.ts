import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { DataService } from 'src/services/data.service';
import { ResultBody } from 'src/schemas/schemas';

@Component({
  selector: 'game-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnChanges {
  @Input() userId?: string;
  @Input() showPopUp: boolean = false;

  isShowStats: boolean = false;
  data$: Omit<ResultBody, 'historicPicks' | 'userId'> | undefined = undefined;

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    const userId = sessionStorage.getItem('userId');

    if (userId) {
      this.fetchStats(userId);
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['showPopUp'] && this.userId) {
      this.fetchStats(this.userId);
    }
  }

  showStats() {
    this.isShowStats = !this.isShowStats;
  }

  async fetchStats(userId: string) {
    const result = await this.dataService.getUserResult(userId);

    if (result) {
      this.data$ = result;
    }
  }
}

import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Theme } from 'src/app/interfaces/theme.interface';
import { ThemeService } from 'src/app/services/theme.service';

@Component({
  selector: 'app-theme-list',
  templateUrl: './theme-list.component.html',
  styleUrls: ['./theme-list.component.scss']
})
export class ThemeListComponent implements OnInit {
  themes: Theme[] = [];
  userSubscriptions: Theme[] = [];

  constructor(private themeService: ThemeService) { }

  ngOnInit(): void {
    forkJoin({ // exécuter les deux appels API en parallèle
      themes: this.themeService.getThemes(),
      subscriptions: this.themeService.getUserSubscriptions(),
    }).subscribe({
      next: ({ themes, subscriptions }) => {
        this.themes = themes;
        this.userSubscriptions = subscriptions;
      },
      error: (error) => { },
    });
  }

  isSubscribed(themeId: number): boolean {
    return this.userSubscriptions.some((subscription) => subscription.id === themeId);
  }

  toggleSubscription(theme: Theme): void {
    this.themeService.updateSubscription(theme.id).subscribe({
      next: (message) => {
        this.themeService.getUserSubscriptions().subscribe({
          next: (subscriptions) => {
            this.userSubscriptions = subscriptions;
          },
          error: (error) => {},
        });
      },
      error: (error) => {},
    });
  }
}

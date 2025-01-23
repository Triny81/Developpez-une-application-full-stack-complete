import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Article } from 'src/app/interfaces/article.interface';
import { ArticleService } from 'src/app/services/article.service';

@Component({
  selector: 'app-article-list',
  templateUrl: './article-list.component.html',
  styleUrls: ['./article-list.component.scss']
})
export class ArticleListComponent implements OnInit {
  articles: Article[] = [];
  sortDecrease: boolean = false;

  constructor(
    private articleService: ArticleService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.articleService.getArticlesUserSubscribed().subscribe({
      next: (data) => {
        this.articles = data;
        this.sortBy('date');
      },
      error: (error) => { },
    });
  }

  sortBy(criteria: string): void {
    if (this.sortDecrease) {
      if (criteria === 'date') {
        this.articles.sort((a, b) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime());
      }

    } else {
      if (criteria === 'date') {
        this.articles.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
      } 
    }

    this.sortDecrease = !this.sortDecrease;
  }

  goToDetail(articleId: number): void {
    this.router.navigate(['/article/' + articleId]);
  }
}

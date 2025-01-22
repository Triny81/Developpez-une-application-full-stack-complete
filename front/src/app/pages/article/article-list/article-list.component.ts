import { Component, OnInit } from '@angular/core';
import { Article } from 'src/app/interfaces/article.interface';
import { ArticleService } from 'src/app/services/article.service';

@Component({
  selector: 'app-article-list',
  templateUrl: './article-list.component.html',
  styleUrls: ['./article-list.component.scss']
})
export class ArticleListComponent implements OnInit {
  articles: Article[] = [];

  constructor(private articleService: ArticleService) { }

  ngOnInit(): void {
    this.articleService.getArticlesUserSubscribed().subscribe({
      next: (data) => {
        this.articles = data;
      },
      error: (error) => { },
    });
  }

  sortBy(criteria: string): void {
    if (criteria === 'date') {
      this.articles.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
    } else if (criteria === 'title') {
      this.articles.sort((a, b) => a.title.localeCompare(b.title));
    }
  }
}

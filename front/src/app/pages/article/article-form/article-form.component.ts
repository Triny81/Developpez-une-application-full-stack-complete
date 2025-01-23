import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Article } from 'src/app/interfaces/article.interface';
import { Theme } from 'src/app/interfaces/theme.interface';
import { ArticleService } from 'src/app/services/article.service';
import { ArticleRequest } from 'src/app/services/interfaces/articleRequest.interface';
import { ThemeRequest } from 'src/app/services/interfaces/themeRequest.interface';
import { ThemeService } from 'src/app/services/theme.service';

@Component({
  selector: 'app-article-form',
  templateUrl: './article-form.component.html',
  styleUrls: ['./article-form.component.scss']
})
export class ArticleFormComponent implements OnInit {
  articleForm!: FormGroup;
  themes: Theme[] = [];
  article!: Article;
  onError!: string;

  constructor(
    private fb: FormBuilder,
    private themeService: ThemeService,
    private articleService: ArticleService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.themeService.getThemes().subscribe({
      next: (themes) => {
        this.themes = themes;
        this.setForm(this.article);
      },
      error: (error) => {
        this.onError = 'Impossible de charger les thèmes.';
      }
    });
  }

  saveArticle(): void { // pourra évoluer afin de post et put des articles
    if (this.articleForm.valid) {
      const articleRequest: ArticleRequest = {
        title: this.articleForm.value.title,
        message: this.articleForm.value.message,
        theme: {id : this.articleForm.value.themeId}
      };

      this.articleService.postArticle(articleRequest).subscribe({
        next: () => {
          this.router.navigate(['/article']);
        },
        error: (error) => {
          this.onError = "Can't post the article";
        }
      });
    } else {
      this.onError = "Form invalid";
    }
  }

  setForm(article: Article): void {
    this.article = article;

    this.articleForm = this.fb.group({
      themeId: [article ? article.theme.id : null, [Validators.required]],
      title: [article ? article.title : "", [Validators.required, Validators.minLength(3)]],
      message: [article ? article.message : "", [Validators.required, Validators.minLength(10)]],
    });
  }
}

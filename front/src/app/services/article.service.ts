import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Article } from '../interfaces/article.interface';
import { ArticleRequest } from './interfaces/articleRequest.interface';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  private pathService = '/api/articles';

  constructor(private httpClient: HttpClient) { }

  getArticleById(articleId: number): Observable<Article> {
    return this.httpClient.get<Article>(this.pathService + '/' + articleId);
  }

  getArticles(): Observable<Article[]> {
    return this.httpClient.get<{ articles: Article[] }>(this.pathService).pipe(
      map((response) => response.articles.map((article) => ({ ...article, subscribed: false })))
    );
  }

  postArticle(article: ArticleRequest): Observable<Article> {
    return this.httpClient.post<Article>(this.pathService, article);
  }

  putArticle(articleId: number, article: ArticleRequest): Observable<Article> {
    return this.httpClient.put<Article>(this.pathService + '/' + articleId, article);
  }

  deleteArticle(articleId: number): Observable<void> {
    return this.httpClient.delete<void>(this.pathService + '/' + articleId);
  }

  getArticlesUserSubscribed(): Observable<Article[]> { // get the articles of the themes that the current subscribed
    return this.httpClient.get<{ articles: Article[] }>(this.pathService + '/getUserSubscriptions').pipe(
      map((response) => response.articles.map((article) => ({ ...article, subscribed: false })))
    );
  }
}

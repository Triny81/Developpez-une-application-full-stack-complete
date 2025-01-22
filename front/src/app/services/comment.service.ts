import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Comment } from '../interfaces/comment.interface';
import { CommentRequest } from './interfaces/commentRequest.interface';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private pathService = '/api/comments';

  constructor(private httpClient: HttpClient) { }

  getCommentById(commentId: number): Observable<Comment> {
    return this.httpClient.get<Comment>(this.pathService + '/' + commentId);
  }

  getComments(): Observable<Comment[]> {
    return this.httpClient.get<{ comments: Comment[] }>(this.pathService).pipe(
      map((response) => response.comments.map((comment) => ({ ...comment, subscribed: false })))
    );
  }

  postComment(comment: CommentRequest): Observable<Comment> {
    return this.httpClient.post<Comment>(this.pathService, comment);
  }

  putComment(commentId: number, comment: CommentRequest): Observable<Comment> {
    return this.httpClient.put<Comment>(this.pathService + '/' + commentId, comment);
  }

  deleteComment(commentId: number): Observable<void> {
    return this.httpClient.delete<void>(this.pathService + '/' + commentId);
  }
}

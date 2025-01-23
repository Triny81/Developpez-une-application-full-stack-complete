import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Article } from 'src/app/interfaces/article.interface';
import { Comment } from 'src/app/interfaces/comment.interface';
import { ArticleService } from 'src/app/services/article.service';
import { CommentService } from 'src/app/services/comment.service';
import { CommentRequest } from 'src/app/services/interfaces/commentRequest.interface';

@Component({
  selector: 'app-article-detail',
  templateUrl: './article-detail.component.html',
  styleUrls: ['./article-detail.component.scss']
})
export class ArticleDetailComponent implements OnInit {
  article!: Article;
  comments: Comment[] = [];
  commentForm!: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private articleService: ArticleService,
    private commentService: CommentService,
  ) { }

  ngOnInit(): void {
    const articleId = this.route.snapshot.params['id'];

    this.articleService.getArticleById(articleId).subscribe({
      next: (article) => {
        this.article = article;
      },
      error: (error) => { },
    });

    this.loadComments();

    this.commentForm = this.fb.group({
      content: ['', [Validators.required, Validators.minLength(3)]],
    });
  }

  loadComments(): void {
    const articleId = this.route.snapshot.params['id'];
    this.commentService.getCommentsByArticle(articleId).subscribe({
      next: (comments) => {
        this.comments = comments;
      },
      error: (error) => { },
    });
  }

  addComment(): void {
    if (this.commentForm.valid) {
      const commentRequest: CommentRequest = {
        message: this.commentForm.value.content,
        article: { id: this.article.id },
      };

      this.commentService.postComment(commentRequest).subscribe({
        next: (comment: Comment) => {
          this.comments.push(comment);
          this.commentForm.reset();
        },
        error: (error) => {},
      });
    }
  }
}

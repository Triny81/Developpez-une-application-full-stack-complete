import { ArticleRequest } from "./articleRequest.interface";

export interface CommentRequest {
    id?: number,
    message?: string,
    article?: ArticleRequest
}
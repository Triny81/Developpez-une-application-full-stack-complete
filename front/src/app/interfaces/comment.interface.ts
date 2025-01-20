import { Article } from "./article.interface"
import { User } from "./user.interface"

export interface Comment {
    id: number,
    message: String,
    author: User,
    article: Article,
    createdAt: Date,
    updatedAt: Date
}
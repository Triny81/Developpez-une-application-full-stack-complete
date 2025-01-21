import { Theme } from "./theme.interface"
import { User } from "./user.interface"

export interface Article {
    id: number,
    title: string,
    message: string,
    author: User,
    theme: Theme
    createdAt: Date,
    updatedAt: Date
}
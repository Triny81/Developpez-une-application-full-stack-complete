
import { ThemeRequest } from "./themeRequest.interface";

export interface ArticleRequest {
    id?: number,
    title?: string,
    message?: string,
    theme?: ThemeRequest
}
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Theme } from '../interfaces/theme.interface';
import { Message } from '../interfaces/message.interface';
import { ThemeRequest } from './interfaces/themeRequest.interface';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private pathService = '/api/themes';

  constructor(private httpClient: HttpClient) { }

  getThemeById(themeId: number): Observable<Theme> {
    return this.httpClient.get<Theme>(this.pathService + '/' + themeId);
  }

  getThemes(): Observable<Theme[]> {
    return this.httpClient.get<{ themes: Theme[] }>(this.pathService).pipe(
      map((response) => response.themes.map((theme) => ({ ...theme, subscribed: false })))
    );
  }

  postTheme(theme: ThemeRequest): Observable<Theme> {
    return this.httpClient.post<Theme>(this.pathService, theme);
  }

  putTheme(themeId: number, theme: ThemeRequest): Observable<Theme> {
    return this.httpClient.put<Theme>(this.pathService + '/' + themeId, theme);
  }

  deleteTheme(themeId: number): Observable<void> {
    return this.httpClient.delete<void>(this.pathService + '/' + themeId);
  }

  getUserSubscriptions(): Observable<Theme[]> { // subscribtions of the current user
    return this.httpClient.get<{ themes: Theme[] }>(this.pathService + "/getUserSubscriptions").pipe(
      map((response) => response.themes.map((theme) => ({ ...theme, subscribed: false })))
    );
  }

  updateSubscription(themeId: number): Observable<Message> { // subscribe or unsubscribe the current user to the theme selected
    return this.httpClient.get<Message>(this.pathService + "/updateSubscription/" + themeId);
  }
}

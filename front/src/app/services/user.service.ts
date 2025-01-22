import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { User } from '../interfaces/user.interface';
import { UserRequest } from './interfaces/userRequest.interface';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private pathService = '/api/users';

  constructor(private httpClient: HttpClient) { }

  getUserById(userId: number): Observable<User> {
    return this.httpClient.get<User>(this.pathService + '/' + userId);
  }

  getUsers(): Observable<User[]> {
    return this.httpClient.get<{ users: User[] }>(this.pathService).pipe(
      map((response) => response.users.map((user) => ({ ...user, subscribed: false })))
    );
  }

  postUser(user: UserRequest): Observable<User> {
    return this.httpClient.post<User>(this.pathService, user);
  }

  putUser(userId: number, user: UserRequest): Observable<User> {
    return this.httpClient.put<User>(this.pathService + '/' + userId, user);
  }

  deleteUser(userId: number): Observable<void> {
    return this.httpClient.delete<void>(this.pathService + '/' + userId);
  }
}

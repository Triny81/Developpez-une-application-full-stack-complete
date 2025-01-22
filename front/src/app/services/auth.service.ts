import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest } from './interfaces/loginRequest.interface';
import { RegisterRequest } from './interfaces/registerRequest.interface';
import { SessionUser } from '../interfaces/sessionUser.interface';
import { User } from '../interfaces/user.interface';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private pathService = '/api/auth';

  constructor(private httpClient: HttpClient,
    private router: Router
  ) { }

  public register(registerRequest: RegisterRequest): Observable<SessionUser> {
    return this.httpClient.post<SessionUser>(this.pathService+"/register", registerRequest);
  }

  public login(loginRequest: LoginRequest): Observable<SessionUser> {
    return this.httpClient.post<SessionUser>(this.pathService+"/login", loginRequest);
  }

  public getUserDetails(): Observable<User> {
    return this.httpClient.get<User>(this.pathService+"/me");
  }

  public logout() {
    localStorage.removeItem('authToken');
    this.router.navigate(['/']);
  }

  public isLoggedIn(): boolean {
    return !!localStorage.getItem('authToken');
  }
}
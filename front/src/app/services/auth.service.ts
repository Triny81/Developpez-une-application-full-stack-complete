import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest } from '../pages/authentication/interfaces/loginRequest.interface';
import { RegisterRequest } from '../pages/authentication/interfaces/registerRequest.interface';
import { SessionUser } from '../interfaces/sessionUser.interface';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private pathService = '/api/auth';

  constructor(private httpClient: HttpClient,
    private router: Router
  ) { }

  public register(registerRequest: RegisterRequest): Observable<SessionUser> {
    return this.httpClient.post<SessionUser>(`${this.pathService}/register`, registerRequest);
  }

  public login(loginRequest: LoginRequest): Observable<SessionUser> {
    return this.httpClient.post<SessionUser>(`${this.pathService}/login`, loginRequest);
  }

  logout() {
    localStorage.removeItem('authToken');
    this.router.navigate(['/']);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('authToken');
  }
}
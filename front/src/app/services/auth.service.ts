import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, of, tap } from 'rxjs';
import { LoginRequest } from './interfaces/loginRequest.interface';
import { RegisterRequest } from './interfaces/registerRequest.interface';
import { User } from '../interfaces/user.interface';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private pathService = '/api/auth';
  private isAuthenticated = new BehaviorSubject<boolean>(false);

  constructor(private httpClient: HttpClient, private router: Router) {
    this.checkAuthentication(); // Vérifier au démarrage si l'utilisateur est connecté
  }

  public register(registerRequest: RegisterRequest): Observable<User> {
    return this.httpClient.post<User>(this.pathService + "/register", registerRequest)
      .pipe(
        tap(() => this.isAuthenticated.next(true))
      );
  }

  public login(loginRequest: LoginRequest): Observable<User> {
    return this.httpClient.post<User>(this.pathService + "/login", loginRequest, { withCredentials: true })
      .pipe(
        tap(() => this.isAuthenticated.next(true))
      );
  }

  public getUserDetails(): Observable<User> {
    return this.httpClient.get<User>(this.pathService + "/me");
  }

  public logout(): void {
    this.httpClient.post<string>(this.pathService + "/logout", null)
      .subscribe(() => {
        this.isAuthenticated.next(false);
      });

    this.router.navigate(['/']);
  }

  public isUserAuthenticated(): Observable<boolean> {
    return this.httpClient.get('/api/auth/me').pipe(
      map(response => !!response),
      catchError(() => of(false))
    );
  }

  private checkAuthentication(): void {
    this.getUserDetails().subscribe({
      next: (response) => { this.isAuthenticated.next(true), console.log("response", response) },
      error: (err) => { this.isAuthenticated.next(false), console.log("err", err) },
    });
  }
}
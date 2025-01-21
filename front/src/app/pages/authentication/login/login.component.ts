import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { LoginRequest } from '../interfaces/loginRequest.interface';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  public hide: boolean = true;
  public onError!: String;

  public loginRequest: LoginRequest = {
    login: '',
    password: ''
  };

  public loginForm: FormGroup = this.fb.group({
    login: [this.loginRequest.login, Validators.required],
    password: [this.loginRequest.password, Validators.required]
  });

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router) {
  }

  ngOnInit(): void {

  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.loginRequest = { ...this.loginForm.value };

      this.authService.login(this.loginRequest).subscribe({
        
        next: (response) => {
          localStorage.setItem('authToken', response.token);
          // this.router.navigate(['/theme']);
        },
        error: (error) => {
          this.onError = error.error;
        },
      });
    }
  }
}

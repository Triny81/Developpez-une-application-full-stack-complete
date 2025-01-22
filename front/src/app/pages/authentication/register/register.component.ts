import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RegisterRequest } from '../../../services/interfaces/registerRequest.interface';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  public hide: boolean = true;
  public onError!: String;

  public registerRequest: RegisterRequest = {
    email: '',
    username: '',
    password: ''
  }

  public registerForm: FormGroup = this.fb.group({
    email: [this.registerRequest.email , [Validators.required, Validators.email]],
    username: [this.registerRequest.username , Validators.required],
    password: [this.registerRequest.password , Validators.required],
  });

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  onSubmit() {
    if (this.registerForm.valid) {
      this.registerRequest = { ...this.registerForm.value };
      
      this.authService.register(this.registerRequest).subscribe({
        
        next: (response) => {
          localStorage.setItem('authToken', response.token);
          this.router.navigate(['/theme']);
        },
        error: (error) => {
          this.onError = error.error;
        },
      });
    }
  }
}

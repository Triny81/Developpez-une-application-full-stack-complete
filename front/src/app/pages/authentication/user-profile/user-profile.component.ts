import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Theme } from 'src/app/interfaces/theme.interface';
import { User } from 'src/app/interfaces/user.interface';
import { AuthService } from 'src/app/services/auth.service';
import { UserRequest } from 'src/app/services/interfaces/userRequest.interface';
import { ThemeService } from 'src/app/services/theme.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  hide: boolean = true;
  profileForm!: FormGroup;
  subscribedThemes: Theme[] = [];
  user!: User;
  onError!: string;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private themeService: ThemeService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.authService.getUserDetails().subscribe({
      next: (user) => {
        this.setForm(user);
      },
      error: (error) => {
      },
    });

    this.loadUserSubscriptions();
  }

  saveProfile(): void {
    if (this.profileForm.valid) {
      let userRequest: UserRequest = {
        username: this.profileForm.value.username,
        email: this.profileForm.value.email,
      };

      const password = this.profileForm.value.password;
      if (password != "") {
        userRequest.password = password;
      }

      this.userService.putUser(this.user.id, userRequest).subscribe({
        next: (user) => {
          this.setForm(user);
          this.onError = "";
        },
        error: (error) => {
          this.onError = error.error;
        },
      });
    } else {
      this.onError = "Form invalid";
    }
  }

  setForm(user: User): void {
    this.user = user;

    this.profileForm = this.fb.group({
      username: [this.user.username, [Validators.required, Validators.minLength(3)]],
      email: [this.user.email, [Validators.required, Validators.email]],
      password: [""],
    });
  }

  loadUserSubscriptions(): void {
    this.themeService.getUserSubscriptions().subscribe({
      next: (themes) => {
        this.subscribedThemes = themes;
      },
      error: (error) => {},
    });
  }

  unsubscribe(theme: Theme): void {
    this.themeService.updateSubscription(theme.id).subscribe({
      next: () => {
        this.loadUserSubscriptions();
      },
      error: (error) => {},
    });
  }

  logout(): void {
    this.authService.logout();
  }
}

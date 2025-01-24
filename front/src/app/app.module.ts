import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/authentication/login/login.component';
import { RegisterComponent } from './pages/authentication/register/register.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/jwt.interceptor';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { HeaderComponent } from './pages/shared/header/header.component';
import { ArticleListComponent } from './pages/article/article-list/article-list.component';
import { ThemeListComponent } from './pages/theme/theme-list/theme-list.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { UserProfileComponent } from './pages/authentication/user-profile/user-profile.component';
import { ArticleFormComponent } from './pages/article/article-form/article-form.component';
import { MatSelectModule } from '@angular/material/select';
import { ArticleDetailComponent } from './pages/article/article-detail/article-detail.component';

@NgModule({
  declarations: [AppComponent, HomeComponent, LoginComponent, RegisterComponent, HeaderComponent, ArticleListComponent, ThemeListComponent, NotFoundComponent, UserProfileComponent, ArticleFormComponent, ArticleDetailComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    HttpClientModule,
    MatIconModule,
    MatSelectModule,
    
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    }],
  bootstrap: [AppComponent],
})
export class AppModule { }

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/authentication/login/login.component';
import { RegisterComponent } from './pages/authentication/register/register.component';
import { AuthGuard } from './guards/auth.guard';
import { ThemeListComponent } from './pages/theme/theme-list/theme-list.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { ArticleListComponent } from './pages/article/article-list/article-list.component';
import { UserProfileComponent } from './pages/authentication/user-profile/user-profile.component';
import { ArticleFormComponent } from './pages/article/article-form/article-form.component';
import { ArticleDetailComponent } from './pages/article/article-detail/article-detail.component';

const routes: Routes = [
  { path: 'article', component: ArticleListComponent, canActivate: [AuthGuard] },
  { path: 'article/form', component: ArticleFormComponent, canActivate: [AuthGuard] },
  { path: 'article/form/:id', component: ArticleFormComponent, canActivate: [AuthGuard] },
  { path: 'article/:id', component: ArticleDetailComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'profile', component: UserProfileComponent, canActivate: [AuthGuard] },
  { path: 'register', component: RegisterComponent },
  { path: 'theme', component: ThemeListComponent, canActivate: [AuthGuard] },
  { path: '', component: HomeComponent },
  { path: '**', component: NotFoundComponent }];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}

import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { filter, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  hideHeader: boolean = false;
  isLoggedIn: boolean = false;
  showLinks: boolean = true;
  isProfilePage: boolean = false;
  
  constructor(private authService: AuthService, private router: Router) { }
  

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();

    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd),
        startWith(this.router))
      .subscribe((event: any) => {
        const currentRoute = event.url;

        this.hideHeader = event.url == "/";
        this.showLinks = !(currentRoute.includes('/login') || currentRoute.includes('/register'));
        this.isProfilePage = event.url === '/profile';
      });
  }

  logout(): void {
    this.authService.logout();
    this.isLoggedIn = false;
    this.router.navigate(['/login']);
  }
}

import { Component, HostListener, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { filter, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  showLinks: boolean = true;
  hideHeader: boolean = false;
  isLoggedIn: boolean = false;
  isProfilePage: boolean = false;
  isMenuOpen: boolean = false;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd),
        startWith(this.router))
      .subscribe((event) => {
        if (event instanceof NavigationEnd) {
          this.isLoggedIn = this.authService.isLoggedIn();

          const currentRoute = event.url;
          this.hideHeader = currentRoute == "/";
          this.showLinks = !(currentRoute.includes('/login') || currentRoute.includes('/register'));
          this.isProfilePage = currentRoute === '/profile';
        }
      });
  }

  logout(): void {
    this.authService.logout();
    this.isLoggedIn = false;
    this.router.navigate(['/login']);
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  closeMenu(): void {
    this.isMenuOpen = false;
  }

  @HostListener('document:click', ['$event.target'])
  onClick(targetElement: HTMLElement): void {
    const isInside = targetElement.closest('.nav-menu-mobile');
    if (!isInside) {
      this.closeMenu();
    }
  }
}

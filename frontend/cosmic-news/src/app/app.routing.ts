import { Routes, RouterModule } from '@angular/router';

const appRoutes: Routes = [
    { path: '', redirectTo: '/index', pathMatch: 'full'}
]

export const routing = RouterModule.forRoot(appRoutes);
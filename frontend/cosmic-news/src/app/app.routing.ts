import { Routes, RouterModule } from '@angular/router';
import { NewsComponent } from './components/cards/news.component';
import { NewsFormComponent } from './components/news/news-form.component';
import { NewsInfoComponent } from './components/news/news-info.component';
import { PicturesComponent } from './components/cards/pictures.component';
import { PictureFormComponent } from './components/picture/picture-form.component';
import { PictureInfoComponent } from './components/picture/picture-info.component';
import { UserLoginComponent } from './components/user/user-login.component';
import { UserFormComponent } from './components/user/user-form.component';
import { UserDetailComponent } from './components/user/user-detail.component';
import { MessageComponent } from './components/message/message.component';

const appRoutes: Routes = [
    { path: 'login', component: UserLoginComponent },
    { path: 'user/form', component: UserFormComponent },
    { path: 'users/:id', component: UserDetailComponent },
    { path: 'users/edit/:id', component: UserFormComponent },
    
    { path: 'news', component: NewsComponent},
    { path: 'news/new', component: NewsFormComponent },
    { path: 'news/:id/edit', component: NewsFormComponent},
    { path: 'news/:id', component: NewsInfoComponent},

    { path: 'pictures', component: PicturesComponent},
    { path: 'pictures/new', component: PictureFormComponent },
    { path: 'pictures/:id/edit', component: PictureFormComponent},
    { path: 'pictures/:id', component: PictureInfoComponent},

    { path: 'message', component: MessageComponent },

    { path: '', redirectTo: '/news', pathMatch: 'full'}
]

export const routing = RouterModule.forRoot(appRoutes);
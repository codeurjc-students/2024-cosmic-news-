import { Routes, RouterModule } from '@angular/router';
import { NewsComponent } from './components/cards/news.component';
import { NewsFormComponent } from './components/news/news-form.component';
import { NewsInfoComponent } from './components/news/news-info.component';
import { PicturesComponent } from './components/cards/pictures.component';
import { PictureFormComponent } from './components/picture/picture-form.component';
import { PictureInfoComponent } from './components/picture/picture-info.component';

const appRoutes: Routes = [
    { path: 'news', component: NewsComponent},
    { path: 'news/new', component: NewsFormComponent },
    { path: 'news/:id/edit', component: NewsFormComponent},
    { path: 'news/:id', component: NewsInfoComponent},

    { path: 'pictures', component: PicturesComponent},
    { path: 'pictures/new', component: PictureFormComponent },
    { path: 'pictures/:id/edit', component: PictureFormComponent},
    { path: 'pictures/:id', component: PictureInfoComponent},

    { path: '', redirectTo: '/news', pathMatch: 'full'}
]

export const routing = RouterModule.forRoot(appRoutes);
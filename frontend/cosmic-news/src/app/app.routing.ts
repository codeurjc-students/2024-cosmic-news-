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
import { VideosComponent } from './components/cards/videos.component';
import { QuizzesComponent } from './components/cards/quizzes.component';
import { VideoFormComponent } from './components/video/video-form.component';
import { VideoInfoComponent } from './components/video/video-info.component';
import { QuizzInfoComponent } from './components/quizz/quizz-info.component';
import { ResultComponent } from './components/quizz/result.component';
import { ReviewComponent } from './components/quizz/review.component';
import { QuizzFormComponent } from './components/quizz/quizz-form.component';
import { EventFormComponent } from './components/calendar/event-form.component';
import { CalendarComponent } from './components/calendar/calendar.component';
import { SpaceComponent } from './components/planet/space.component';

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

    { path: 'videos', component: VideosComponent},
    { path: 'videos/new', component: VideoFormComponent },
    { path: 'videos/:id/edit', component: VideoFormComponent},
    { path: 'videos/:id', component: VideoInfoComponent},

    { path: 'quizzes', component: QuizzesComponent},
    { path: 'quizzes/new', component: QuizzFormComponent },
    { path: 'quizzes/:id', component: QuizzInfoComponent},
    { path: 'quizzes/:id/edit', component: QuizzFormComponent},
    { path: 'quizzes/:id/result', component:ResultComponent},
    { path: 'quizzes/:id/result/review', component:ReviewComponent},

    { path: 'calendar', component: CalendarComponent},    
    { path: 'events/new', component: EventFormComponent },
    { path: 'events/:id/edit', component: EventFormComponent },

    { path: 'space', component: SpaceComponent },

    { path: 'message', component: MessageComponent },

    { path: '', redirectTo: '/news', pathMatch: 'full'}
]

export const routing = RouterModule.forRoot(appRoutes, {
    anchorScrolling: 'enabled',
    scrollPositionRestoration: 'enabled'
  });
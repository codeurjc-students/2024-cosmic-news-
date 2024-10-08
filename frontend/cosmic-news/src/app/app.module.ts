import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA  } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { AppComponent } from './app.component';
import { routing } from './app.routing';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NewsFormComponent } from './components/news/news-form.component';
import { NewsInfoComponent } from './components/news/news-info.component';
import { NewsComponent } from './components/cards/news.component';
import { PictureFormComponent } from './components/picture/picture-form.component';
import { PictureInfoComponent } from './components/picture/picture-info.component';
import { PicturesComponent } from './components/cards/pictures.component';
import { HeaderComponent } from './components/header/header.component';
import { UserLoginComponent } from './components/user/user-login.component';
import { UserFormComponent } from './components/user/user-form.component';
import { UserDetailComponent } from './components/user/user-detail.component';
import { MessageComponent } from './components/message/message.component';
import { VideosComponent } from './components/cards/videos.component';
import { QuizzesComponent } from './components/cards/quizzes.component';
import { VideoFormComponent } from './components/video/video-form.component';
import { VideoInfoComponent } from './components/video/video-info.component';

@NgModule({
  declarations: [
    AppComponent, NewsFormComponent, NewsInfoComponent, NewsComponent,
    PictureFormComponent, PictureInfoComponent, PicturesComponent, HeaderComponent, UserLoginComponent, UserFormComponent, UserDetailComponent, MessageComponent, VideosComponent, QuizzesComponent, VideoFormComponent, VideoInfoComponent
  ],
  imports: [
    CommonModule,
    NgbModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    routing
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule { }
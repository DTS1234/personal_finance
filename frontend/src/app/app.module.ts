import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {HomepageComponent} from './homepage/homepage.component';
import {NavbarComponent} from './navbar/navbar.component';
import {ManageComponent} from './manage/manage.component';
import {HistoryComponent} from './history/history.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';
import {AppRoutingModule} from './app-routing.module';
import {FooterComponent} from './footer/footer.component';
import {AssetBoxComponent} from './dashboard/asset-box/asset-box.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {SummaryBoxComponent} from './dashboard/summary-box/summary-box.component';
import {SummaryCreationComponent} from './manage/summary-creation/summary-creation.component';
import {AddAssetComponent} from './manage/summary-creation/add-asset/add-asset.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {EditAssetComponent} from './manage/summary-creation/edit-asset/edit-asset.component';
import {DatePipe} from '@angular/common';
import {AuthComponent} from './auth/auth.component';
import {AuthInterceptorService} from "./auth/auth.interceptor.service";
import {DashboardComponent} from './dashboard/dashboard.component';
import {PasswordResetComponent} from './password/password-reset/password-reset.component';
import {PasswordResetRequestComponent} from './password/password-reset-request/password-reset-request.component';

@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    NavbarComponent,
    ManageComponent,
    HistoryComponent,
    FooterComponent,
    AssetBoxComponent,
    SummaryBoxComponent,
    SummaryCreationComponent,
    AddAssetComponent,
    EditAssetComponent,
    AuthComponent,
    DashboardComponent,
    PasswordResetComponent,
    PasswordResetRequestComponent
  ],
  imports: [
    BrowserModule, HttpClientModule, RouterModule, AppRoutingModule,
    BrowserAnimationsModule, ReactiveFormsModule, FormsModule
  ],
  providers: [DatePipe, {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi:true }],
  bootstrap: [AppComponent]
})
export class AppModule {
}

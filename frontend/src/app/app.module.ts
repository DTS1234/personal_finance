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
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatNativeDateModule} from "@angular/material/core";
import {MatInputModule} from "@angular/material/input";
import {SignUpComponent} from "./auth/sign-up/sign-up.component";
import {LoginComponent} from "./auth/login/login.component";
import {SuccessComponent} from "./subscription/success/success.component";
import {SubscriptionComponent} from "./subscription/subscription.component";
import {BuyComponent} from "./subscription/buy/buy.component";
import {SpinnerComponent} from "./common/spinner/spinner.component";
import {StockItemFormComponent} from "./manage/summary-creation/stock-item-form/stock-item-form.component";
import {NormalItemFormComponent} from "./manage/summary-creation/normal-item-form/normal-item-form.component";

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
    PasswordResetRequestComponent,
    SubscriptionComponent,
    SuccessComponent,
    BuyComponent
  ],
  imports: [
    BrowserModule, HttpClientModule, RouterModule, AppRoutingModule,
    BrowserAnimationsModule, ReactiveFormsModule, FormsModule, MatDatepickerModule, MatFormFieldModule,
    MatNativeDateModule, MatInputModule, SignUpComponent, LoginComponent, SpinnerComponent, StockItemFormComponent, NormalItemFormComponent
  ],
  providers: [DatePipe, {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi:true }],
  bootstrap: [AppComponent]
})
export class AppModule {
}

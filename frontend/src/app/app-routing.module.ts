import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomepageComponent} from './homepage/homepage.component';
import {HistoryComponent} from './history/history.component';
import {ManageComponent} from './manage/manage.component';
import {CommonModule} from '@angular/common';
import {SummaryCreationComponent} from './manage/summary-creation/summary-creation.component';
import {AssetFormComponent} from './manage/summary-creation/asset-form/asset-form.component';
import {AuthComponent} from "./auth/auth.component";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {PasswordResetComponent} from "./password/password-reset/password-reset.component";
import {PasswordResetRequestComponent} from "./password/password-reset-request/password-reset-request.component";
import {AuthGuardService} from "./services/auth-guard.service";
import {PaymentComponent} from "./subscription/payment/payment.component";
import {SubscriptionComponent} from "./subscription/subscription.component";
import {AccountComponent} from "./account/account.component";
import {SuccessComponent} from "./subscription/success/success.component";
import {BuyComponent} from "./subscription/buy/buy.component";
import {ExplorerComponent} from "./explorer/explorer.component";
import {SubscriptionGuardService} from "./auth/subscription-guard.service";
import {NoSubscriptionComponent} from "./auth/no-subscription/no-subscription.component";

const appRoutes: Routes = [
  {path: '', redirectTo: '/homepage', pathMatch: 'full'},
  {
    path: 'homepage', component: HomepageComponent, children: []
  },
  {
    path: 'dashboard', component: DashboardComponent, children: [], canActivate: [AuthGuardService]
  },
  {
    path: 'history', component: HistoryComponent, children: [], canActivate: [AuthGuardService]
  },
  {
    path: 'manage', component: ManageComponent, canActivate: [AuthGuardService]
  },
  {
    path: 'summary/:id', component: SummaryCreationComponent, canActivate: [AuthGuardService]
  },
  {
    path: 'summary/:id/add-asset', component: AssetFormComponent, canActivate: [AuthGuardService]
  },
  {
    path: 'auth', component: AuthComponent
  },
  {
    path: 'password_reset', component: PasswordResetComponent
  },
  {
    path: 'password_reset/request', component: PasswordResetRequestComponent
  },
  {
    path: 'subscription', component: SubscriptionComponent, children: [
      {path: "success", component: SuccessComponent},
      {path: "buy", component: BuyComponent},
      {path: "payment", component: PaymentComponent}
    ], canActivate: [AuthGuardService]
  },
  {
    path: 'account', component: AccountComponent
  },
  {
    path: 'explorer', component: ExplorerComponent, canActivate: [AuthGuardService, SubscriptionGuardService]
  },
  {
    path: 'get-subscription', component: NoSubscriptionComponent
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes, {enableTracing: true}), CommonModule
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {

}

import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomepageComponent} from './homepage/homepage.component';
import {HistoryComponent} from './history/history.component';
import {ManageComponent} from './manage/manage.component';
import {CommonModule} from '@angular/common';
import {SummaryCreationComponent} from './manage/summary-creation/summary-creation.component';
import {AddAssetComponent} from './manage/summary-creation/add-asset/add-asset.component';
import {EditAssetComponent} from "./manage/summary-creation/edit-asset/edit-asset.component";
import {AuthComponent} from "./auth/auth.component";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {PasswordResetComponent} from "./password/password-reset/password-reset.component";
import {PasswordResetRequestComponent} from "./password/password-reset-request/password-reset-request.component";
import {AuthGuardService} from "./services/auth-guard.service";

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
    path: 'summary/:id/add-asset', component: AddAssetComponent, canActivate: [AuthGuardService]
  },
  {
    path: 'summary/:id/edit-asset', component: EditAssetComponent, canActivate: [AuthGuardService]
  },
  {
    path: 'auth', component: AuthComponent
  },
  {
    path: 'password_reset', component: PasswordResetComponent
  },
  {
    path: 'password_reset/request', component: PasswordResetRequestComponent
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes, {}), CommonModule
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {

}

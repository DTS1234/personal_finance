import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomepageComponent} from './homepage/homepage.component';
import {HistoryComponent} from './history/history.component';
import {ManageComponent} from './manage/manage.component';
import {CommonModule} from '@angular/common';
import {SummaryCreationComponent} from './manage/summary-creation/summary-creation.component';


const appRoutes: Routes = [
  {path: '', redirectTo: '/homepage', pathMatch: 'full'},
  {
    path: 'homepage', component: HomepageComponent, children: []
  },
  {
    path: 'history', component: HistoryComponent, children: [
      // {path: 'edit/:id', component: ShoppingListEditComponent}
    ]
  },
  {
    path: 'manage', component: ManageComponent,
  },
  {
    path: 'summary/new', component: SummaryCreationComponent
  }

];

@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes), CommonModule
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {

}

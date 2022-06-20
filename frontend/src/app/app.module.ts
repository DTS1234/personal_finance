import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {HomepageComponent} from './homepage/homepage.component';
import {NavbarComponent} from './navbar/navbar.component';
import {ManageComponent} from './manage/manage.component';
import {HistoryComponent} from './history/history.component';
import {HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';
import {AssetService} from './services/asset.service';
import {AppRoutingModule} from './app-routing.module';
import {FooterComponent} from './footer/footer.component';
import {AssetBoxComponent} from './homepage/asset-box/asset-box.component';
import {HomepageChartComponent} from './homepage/homepage-chart/homepage-chart.component';
import {ChartsModule, MDBBootstrapModule} from 'angular-bootstrap-md';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {SummaryBoxComponent} from './homepage/summary-box/summary-box.component';
import { PiechartComponent } from './manage/piechart/piechart.component';

@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    NavbarComponent,
    ManageComponent,
    HistoryComponent,
    FooterComponent,
    AssetBoxComponent,
    HomepageChartComponent,
    SummaryBoxComponent,
    PiechartComponent,
    PiechartComponent
  ],
  imports: [
    BrowserModule, HttpClientModule, RouterModule, AppRoutingModule, MDBBootstrapModule.forRoot(),
    BrowserAnimationsModule, ChartsModule
  ],
  providers: [AssetService],
  bootstrap: [AppComponent]
})
export class AppModule {
}

import {Component, OnInit} from '@angular/core';
import {Asset} from '../models/asset.model';
import {AssetService} from '../services/asset.service';
import {Summary} from '../models/summary.model';
import {SummaryService} from '../services/summary.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {
  ngOnInit(): void {
  }
}

import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {

  date: string;

  constructor() {
  }

  ngOnInit(): void {
    const currDate: Date = new Date(Date.now());
    this.date = currDate.toLocaleDateString();
  }

}

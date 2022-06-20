import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomepageChartComponent } from './homepage-chart.component';

describe('HomepageChartComponent', () => {
  let component: HomepageChartComponent;
  let fixture: ComponentFixture<HomepageChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HomepageChartComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HomepageChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

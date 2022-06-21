import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SummaryCreationComponent } from './summary-creation.component';

describe('SummaryCreationComponent', () => {
  let component: SummaryCreationComponent;
  let fixture: ComponentFixture<SummaryCreationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SummaryCreationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SummaryCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

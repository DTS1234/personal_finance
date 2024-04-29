import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NoSubscriptionComponent } from './no-subscription.component';

describe('NoSubscriptionComponent', () => {
  let component: NoSubscriptionComponent;
  let fixture: ComponentFixture<NoSubscriptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NoSubscriptionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NoSubscriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

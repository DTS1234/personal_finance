import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NormalItemFormComponent } from './normal-item-form.component';

describe('NormalItemFormComponent', () => {
  let component: NormalItemFormComponent;
  let fixture: ComponentFixture<NormalItemFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NormalItemFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NormalItemFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

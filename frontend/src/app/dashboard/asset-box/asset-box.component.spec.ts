import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssetBoxComponent } from './asset-box.component';

describe('AssetBoxComponent', () => {
  let component: AssetBoxComponent;
  let fixture: ComponentFixture<AssetBoxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AssetBoxComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AssetBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

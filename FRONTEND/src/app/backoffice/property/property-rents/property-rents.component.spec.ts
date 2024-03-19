import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PropertyRentsComponent } from './property-rents.component';

describe('PropertyRentsComponent', () => {
  let component: PropertyRentsComponent;
  let fixture: ComponentFixture<PropertyRentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PropertyRentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PropertyRentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

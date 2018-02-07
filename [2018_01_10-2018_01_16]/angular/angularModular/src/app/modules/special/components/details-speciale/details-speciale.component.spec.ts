import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailsSpecialeComponent } from './details-speciale.component';

describe('DetailsSpecialeComponent', () => {
  let component: DetailsSpecialeComponent;
  let fixture: ComponentFixture<DetailsSpecialeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetailsSpecialeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailsSpecialeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListeSpecialeComponent } from './liste-speciale.component';

describe('ListeSpecialeComponent', () => {
  let component: ListeSpecialeComponent;
  let fixture: ComponentFixture<ListeSpecialeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListeSpecialeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListeSpecialeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

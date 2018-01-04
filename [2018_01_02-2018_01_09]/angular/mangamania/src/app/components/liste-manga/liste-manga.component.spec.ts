import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListeMangaComponent } from './liste-manga.component';

describe('ListeMangaComponent', () => {
  let component: ListeMangaComponent;
  let fixture: ComponentFixture<ListeMangaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListeMangaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListeMangaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

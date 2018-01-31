import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TexturedCubeComponent } from './textured-cube.component';

describe('TexturedCubeComponent', () => {
  let component: TexturedCubeComponent;
  let fixture: ComponentFixture<TexturedCubeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TexturedCubeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TexturedCubeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { TestBed, inject } from '@angular/core/testing';

import { ImageRepositoryService } from './image-repository.service';

describe('ImageRepositoryService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ImageRepositoryService]
    });
  });

  it('should be created', inject([ImageRepositoryService], (service: ImageRepositoryService) => {
    expect(service).toBeTruthy();
  }));
});

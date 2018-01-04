import { TestBed, inject } from '@angular/core/testing';

import { MangaRepositoryService } from './manga-repository.service';

describe('MangaRepositoryService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MangaRepositoryService]
    });
  });

  it('should be created', inject([MangaRepositoryService], (service: MangaRepositoryService) => {
    expect(service).toBeTruthy();
  }));
});

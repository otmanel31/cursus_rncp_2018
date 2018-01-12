import { TestBed, inject } from '@angular/core/testing';

import { TagRepositoryService } from './tag-repository.service';

describe('TagRepositoryService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TagRepositoryService]
    });
  });

  it('should be created', inject([TagRepositoryService], (service: TagRepositoryService) => {
    expect(service).toBeTruthy();
  }));
});

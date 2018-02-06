import { TestBed, inject } from '@angular/core/testing';

import { TagRepositoryService } from './tag-repository.service';

describe('TagRepositoryService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TagRepositoryService] // test de service, declaration en provider
    });
  });

  // utilisation de la fonction inject, pour verifier si le service est bien injecté
  it('should be created', inject([TagRepositoryService],
     (service: TagRepositoryService) => {
    expect(service).toBeTruthy();
  }));
  
});

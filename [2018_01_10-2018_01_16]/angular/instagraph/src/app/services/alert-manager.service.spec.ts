import { TestBed, inject } from '@angular/core/testing';

import { AlertManagerService } from './alert-manager.service';

describe('AlertManagerService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AlertManagerService]
    });
  });

  it('should be created', inject([AlertManagerService], (service: AlertManagerService) => {
    expect(service).toBeTruthy();
  }));
});

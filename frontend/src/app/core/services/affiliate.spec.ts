import { TestBed } from '@angular/core/testing';

import { Affiliate } from './affiliate';

describe('Affiliate', () => {
  let service: Affiliate;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Affiliate);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

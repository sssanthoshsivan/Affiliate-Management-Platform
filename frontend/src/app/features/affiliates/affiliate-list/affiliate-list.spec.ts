import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AffiliateList } from './affiliate-list';

describe('AffiliateList', () => {
  let component: AffiliateList;
  let fixture: ComponentFixture<AffiliateList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AffiliateList],
    }).compileComponents();

    fixture = TestBed.createComponent(AffiliateList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

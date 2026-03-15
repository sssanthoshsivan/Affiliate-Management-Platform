import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LinkGenerator } from './link-generator';

describe('LinkGenerator', () => {
  let component: LinkGenerator;
  let fixture: ComponentFixture<LinkGenerator>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LinkGenerator],
    }).compileComponents();

    fixture = TestBed.createComponent(LinkGenerator);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

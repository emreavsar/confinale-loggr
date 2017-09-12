/* tslint:disable max-line-length */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {LoggrTestModule} from '../../../test.module';
import {StatisticDetailComponent} from '../../../../../../main/webapp/app/statistic/statistic-Detail/statistic-detail.component';

describe('Component Tests', () => {

    describe('Statistic Detail component', () => {
        let comp: StatisticDetailComponent;
        let fixture: ComponentFixture<StatisticDetailComponent>;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LoggrTestModule],
                declarations: [StatisticDetailComponent]
            }).overrideTemplate(StatisticDetailComponent, '')
                .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StatisticDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should round numbers to two digits on init', () => {
                // GIVEN
                comp.statistic = {
                    name: 'Hello World Project',
                    bookedHours: 500.20592
                };

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.statistic.bookedHours).toEqual(500.21);
            });
        });
    });

});

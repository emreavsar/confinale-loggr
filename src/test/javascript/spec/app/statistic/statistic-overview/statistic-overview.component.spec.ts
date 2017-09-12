/* tslint:disable max-line-length */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {DatePipe} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs/Rx';
import {JhiDataUtils, JhiDateUtils, JhiEventManager} from 'ng-jhipster';
import {LoggrTestModule} from '../../../test.module';
import {MockActivatedRoute} from '../../../helpers/mock-route.service';
import {
    Statistic,
    StatisticOverviewComponent
} from '../../../../../../main/webapp/app/statistic/statistic-overview/statistic-overview.component';
import {WorkLogService} from '../../../../../../main/webapp/app/entities/work-log/work-log.service';

describe('Component Tests', () => {

    describe('Statistic Overview component', () => {
        let comp: StatisticOverviewComponent;
        let fixture: ComponentFixture<StatisticOverviewComponent>;
        let service: WorkLogService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LoggrTestModule],
                declarations: [StatisticOverviewComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute()
                    },
                    WorkLogService,
                    JhiEventManager
                ]
            }).overrideTemplate(StatisticOverviewComponent, '')
                .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StatisticOverviewComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WorkLogService);
        });

        describe('OnInit', () => {
            it('Should call statisticPerEmployee on init', () => {
                // GIVEN

                const mockStatisticsPerEmployee: Statistic[] = [
                    {
                        name: "Workaholic Employee",
                        bookedHours: 500
                    },
                    {
                        name: "Lazy employee",
                        bookedHours: 5
                    }
                ];
                spyOn(service, 'statisticPerEmployee').and.returnValue(Observable.of(mockStatisticsPerEmployee));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.statisticPerEmployee).toHaveBeenCalled();
                expect(comp.statisticPerEmployee).toEqual(jasmine.objectContaining(mockStatisticsPerEmployee));
            });
            it('Should call statisticPerProject on init', () => {
                // GIVEN

                const mockStatisticsPerProject: Statistic[] = [
                    {
                        name: "Confinale App",
                        bookedHours: 500
                    },
                    {
                        name: "Android App",
                        bookedHours: 20
                    },
                    {
                        name: "iOS App",
                        bookedHours: 50
                    }
                ];
                spyOn(service, 'statisticPerProject').and.returnValue(Observable.of(mockStatisticsPerProject));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.statisticPerProject).toHaveBeenCalled();
                expect(comp.statisticPerProject).toEqual(jasmine.objectContaining(mockStatisticsPerProject));
            });
        });
    });

});

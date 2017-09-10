/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { LoggrTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { WorkLogDetailComponent } from '../../../../../../main/webapp/app/entities/work-log/work-log-detail.component';
import { WorkLogService } from '../../../../../../main/webapp/app/entities/work-log/work-log.service';
import { WorkLog } from '../../../../../../main/webapp/app/entities/work-log/work-log.model';

describe('Component Tests', () => {

    describe('WorkLog Management Detail Component', () => {
        let comp: WorkLogDetailComponent;
        let fixture: ComponentFixture<WorkLogDetailComponent>;
        let service: WorkLogService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LoggrTestModule],
                declarations: [WorkLogDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    WorkLogService,
                    JhiEventManager
                ]
            }).overrideTemplate(WorkLogDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WorkLogDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WorkLogService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new WorkLog(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.workLog).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});

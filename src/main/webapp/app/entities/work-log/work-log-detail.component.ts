import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { WorkLog } from './work-log.model';
import { WorkLogService } from './work-log.service';

@Component({
    selector: 'jhi-work-log-detail',
    templateUrl: './work-log-detail.component.html'
})
export class WorkLogDetailComponent implements OnInit, OnDestroy {

    workLog: WorkLog;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private workLogService: WorkLogService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWorkLogs();
    }

    load(id) {
        this.workLogService.find(id).subscribe((workLog) => {
            this.workLog = workLog;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWorkLogs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'workLogListModification',
            (response) => this.load(this.workLog.id)
        );
    }
}

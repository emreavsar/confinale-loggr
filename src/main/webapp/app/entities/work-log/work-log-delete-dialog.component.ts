import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { WorkLog } from './work-log.model';
import { WorkLogPopupService } from './work-log-popup.service';
import { WorkLogService } from './work-log.service';

@Component({
    selector: 'jhi-work-log-delete-dialog',
    templateUrl: './work-log-delete-dialog.component.html'
})
export class WorkLogDeleteDialogComponent {

    workLog: WorkLog;

    constructor(
        private workLogService: WorkLogService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.workLogService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'workLogListModification',
                content: 'Deleted an workLog'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-work-log-delete-popup',
    template: ''
})
export class WorkLogDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private workLogPopupService: WorkLogPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.workLogPopupService
                .open(WorkLogDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

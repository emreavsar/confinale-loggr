import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { WorkLog } from './work-log.model';
import { WorkLogPopupService } from './work-log-popup.service';
import { WorkLogService } from './work-log.service';
import { User, UserService } from '../../shared';
import { Project, ProjectService } from '../project';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-work-log-dialog',
    templateUrl: './work-log-dialog.component.html'
})
export class WorkLogDialogComponent implements OnInit {

    workLog: WorkLog;
    isSaving: boolean;

    users: User[];

    projects: Project[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private workLogService: WorkLogService,
        private userService: UserService,
        private projectService: ProjectService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.projectService.query()
            .subscribe((res: ResponseWrapper) => { this.projects = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.workLog.id !== undefined) {
            this.subscribeToSaveResponse(
                this.workLogService.update(this.workLog));
        } else {
            this.subscribeToSaveResponse(
                this.workLogService.create(this.workLog));
        }
    }

    private subscribeToSaveResponse(result: Observable<WorkLog>) {
        result.subscribe((res: WorkLog) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: WorkLog) {
        this.eventManager.broadcast({ name: 'workLogListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackProjectById(index: number, item: Project) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-work-log-popup',
    template: ''
})
export class WorkLogPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private workLogPopupService: WorkLogPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.workLogPopupService
                    .open(WorkLogDialogComponent as Component, params['id']);
            } else {
                this.workLogPopupService
                    .open(WorkLogDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

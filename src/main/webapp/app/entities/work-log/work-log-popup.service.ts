import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { WorkLog } from './work-log.model';
import { WorkLogService } from './work-log.service';

@Injectable()
export class WorkLogPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private workLogService: WorkLogService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.workLogService.find(id).subscribe((workLog) => {
                    workLog.startDate = this.datePipe
                        .transform(workLog.startDate, 'yyyy-MM-ddTHH:mm:ss');
                    workLog.endDate = this.datePipe
                        .transform(workLog.endDate, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.workLogModalRef(component, workLog);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.workLogModalRef(component, new WorkLog());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    workLogModalRef(component: Component, workLog: WorkLog): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.workLog = workLog;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}

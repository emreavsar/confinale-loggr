import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { WorkLogComponent } from './work-log.component';
import { WorkLogDetailComponent } from './work-log-detail.component';
import { WorkLogPopupComponent } from './work-log-dialog.component';
import { WorkLogDeletePopupComponent } from './work-log-delete-dialog.component';

export const workLogRoute: Routes = [
    {
        path: 'work-log',
        component: WorkLogComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'loggrApp.workLog.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'work-log/:id',
        component: WorkLogDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'loggrApp.workLog.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const workLogPopupRoute: Routes = [
    {
        path: 'work-log-new',
        component: WorkLogPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'loggrApp.workLog.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'work-log/:id/edit',
        component: WorkLogPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'loggrApp.workLog.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'work-log/:id/delete',
        component: WorkLogDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'loggrApp.workLog.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

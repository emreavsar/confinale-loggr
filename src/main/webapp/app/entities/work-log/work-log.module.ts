import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LoggrSharedModule } from '../../shared';
import { LoggrAdminModule } from '../../admin/admin.module';
import {
    WorkLogService,
    WorkLogPopupService,
    WorkLogComponent,
    WorkLogDetailComponent,
    WorkLogDialogComponent,
    WorkLogPopupComponent,
    WorkLogDeletePopupComponent,
    WorkLogDeleteDialogComponent,
    workLogRoute,
    workLogPopupRoute,
} from './';

const ENTITY_STATES = [
    ...workLogRoute,
    ...workLogPopupRoute,
];

@NgModule({
    imports: [
        LoggrSharedModule,
        LoggrAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        WorkLogComponent,
        WorkLogDetailComponent,
        WorkLogDialogComponent,
        WorkLogDeleteDialogComponent,
        WorkLogPopupComponent,
        WorkLogDeletePopupComponent,
    ],
    entryComponents: [
        WorkLogComponent,
        WorkLogDialogComponent,
        WorkLogPopupComponent,
        WorkLogDeleteDialogComponent,
        WorkLogDeletePopupComponent,
    ],
    providers: [
        WorkLogService,
        WorkLogPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LoggrWorkLogModule {}

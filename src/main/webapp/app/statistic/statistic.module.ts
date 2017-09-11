import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from "@angular/router";
import {statisticsRoute} from "./statistics.route";
import {LoggrAdminModule} from "../admin/admin.module";
import {LoggrSharedModule} from "../shared/shared.module";
import {StatisticOverviewComponent} from "./statistic-overview/statistic-overview.component";
import {StatisticDetailComponent} from "./statistic-detail/statistic-detail.component";

const ENTITY_STATES = [
    ...statisticsRoute
];

@NgModule({
    imports: [
        LoggrSharedModule,
        LoggrAdminModule,
        CommonModule,
        RouterModule.forRoot(ENTITY_STATES, {useHash: true})
    ],
    declarations: [StatisticOverviewComponent, StatisticDetailComponent]
})
export class StatisticModule {
}

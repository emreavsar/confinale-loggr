import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate} from '@angular/router';

import {UserRouteAccessService} from "../shared/auth/user-route-access-service";
import {StatisticOverviewComponent} from "./statistic-overview/statistic-overview.component";

export const statisticsRoute: Routes = [
    {
        path: 'statistic',
        component: StatisticOverviewComponent,
        data: {
            authorities: ['ROLE_MANAGER'],
            pageTitle: 'loggrApp.statistic.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

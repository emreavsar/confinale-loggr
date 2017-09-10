import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { LoggrSharedModule, UserRouteAccessService } from './shared';
import { LoggrHomeModule } from './home/home.module';
import { LoggrAdminModule } from './admin/admin.module';
import { LoggrAccountModule } from './account/account.module';
import { LoggrEntityModule } from './entities/entity.module';

import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
    LayoutRoutingModule,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent
} from './layouts';
import {StatisticModule} from "./statistic/statistic.module";

@NgModule({
    imports: [
        BrowserModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        LoggrSharedModule,
        LoggrHomeModule,
        LoggrAdminModule,
        LoggrAccountModule,
        LoggrEntityModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        StatisticModule
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class LoggrAppModule {}

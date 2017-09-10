import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { LoggrProjectModule } from './project/project.module';
import { LoggrWorkLogModule } from './work-log/work-log.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        LoggrProjectModule,
        LoggrWorkLogModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LoggrEntityModule {}

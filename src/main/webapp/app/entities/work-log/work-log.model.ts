import { BaseEntity } from './../../shared';

const enum WorkLogType {
    'WORK',
    'VACATION'
}

export class WorkLog implements BaseEntity {
    constructor(
        public id?: number,
        public startDate?: any,
        public endDate?: any,
        public type?: WorkLogType,
        public approved?: boolean,
        public creatorId?: number,
        public projects?: BaseEntity[],
    ) {
        this.approved = false;
    }
}

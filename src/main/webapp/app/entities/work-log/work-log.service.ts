import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {JhiDateUtils} from 'ng-jhipster';

import {WorkLog} from './work-log.model';
import {ResponseWrapper, createRequestOption} from '../../shared';
import {StatisticModule} from "../../statistic/statistic.module";
import {Statistic} from "../../statistic/statistic-overview/statistic-overview.component";

@Injectable()
export class WorkLogService {

    private resourceUrl = 'api/work-logs';

    constructor(private http: Http, private dateUtils: JhiDateUtils) {
    }

    create(workLog: WorkLog): Observable<WorkLog> {
        const copy = this.convert(workLog);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(workLog: WorkLog): Observable<WorkLog> {
        const copy = this.convert(workLog);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<WorkLog> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    statisticPerProject(): Observable<Statistic[]> {
        return this.http.get(`${this.resourceUrl}/statistics/project`)
            .map((res: Response) => res.json());
    }

    statisticPerEmployee(): Observable<Statistic[]> {
        return this.http.get(`${this.resourceUrl}/statistics/employee`)
            .map((res: Response) => res.json());
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.startDate = this.dateUtils
            .convertDateTimeFromServer(entity.startDate);
        entity.endDate = this.dateUtils
            .convertDateTimeFromServer(entity.endDate);
    }

    private convert(workLog: WorkLog): WorkLog {
        const copy: WorkLog = Object.assign({}, workLog);

        copy.startDate = this.dateUtils.toDate(workLog.startDate);

        copy.endDate = this.dateUtils.toDate(workLog.endDate);
        return copy;
    }
}

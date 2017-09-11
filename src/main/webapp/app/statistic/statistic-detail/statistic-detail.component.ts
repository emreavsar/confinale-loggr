import {Component, OnInit, Input} from '@angular/core';
import {Statistic} from "../statistic-overview/statistic-overview.component";

@Component({
    selector: 'jhi-statistic-detail',
    templateUrl: './statistic-detail.component.html',
    styles: []
})
export class StatisticDetailComponent implements OnInit {

    @Input()
    statistic: Statistic;

    constructor() {
    }

    ngOnInit() {
    }

}

import {Component, OnInit} from '@angular/core';
import {WorkLogService} from "../../entities/work-log/work-log.service";
import {WorkLog} from "../../entities/work-log/work-log.model";

@Component({
    selector: 'jhi-statistic-overview',
    templateUrl: './statistic-overview.component.html',
    styles: [`
        pre {
            font-size: 10px !important;
            background-color: lightgray;
            margin-top: 20px;
        }        
    `]
})
export class StatisticOverviewComponent implements OnInit {

    statisticPerProject: Statistic[];

    statisticPerEmployee: Statistic[];

    showRawDataPerProject: boolean;

    showRawDataPerEmployee: boolean;

    perProjectBarChartLabels: string[];

    perProjectBarChartData: string[];

    constructor(private worklogService: WorkLogService) {
    }

    ngOnInit() {

        this.worklogService.statisticPerProject()
            .subscribe((res) => {
                this.statisticPerProject = res;
                this.updateStatisticChart();
            });

        this.worklogService.statisticPerEmployee()
            .subscribe((res) => {
                this.statisticPerEmployee = res;
            });
    }

    toggleRawDataPerProject() {
        this.showRawDataPerProject = !this.showRawDataPerProject;
    }

    toggleRawDataPerEmployee() {
        this.showRawDataPerEmployee = !this.showRawDataPerEmployee;
    }

    private updateStatisticChart() {
        this.perProjectBarChartLabels = this.statisticPerProject.map(statistic => statistic.name);
        this.perProjectBarChartData = ['hello', 'world', 'whats', 'up'];
        // this.perProjectBarChartData = this.statisticPerProject.map(statistic => {
        // statistic.worklogs.map(value => value)
        // });
    }
}

export interface Statistic {
    name: string;
    worklogs: WorkLog[];
}

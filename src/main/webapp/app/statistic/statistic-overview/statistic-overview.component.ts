import {Component, OnInit} from '@angular/core';
import {WorkLogService} from "../../entities/work-log/work-log.service";

@Component({
    selector: 'jhi-statistic-overview',
    templateUrl: './statistic-overview.component.html',
    styles: [`
        pre {
            font-size: 10px !important;
            background-color: lightgray;
            margin-top: 20px;
        }
        
        .btn-toggle, .statistic-container {
            margin-top: 50px;
        }
    `]
})
export class StatisticOverviewComponent implements OnInit {

    statisticPerProject: Statistic[];

    statisticPerEmployee: Statistic[];

    statisticPerProjectLoading = false;
    statisticPerEmployeeLoading = false;

    showRawDataPerProject: boolean;

    showRawDataPerEmployee: boolean;

    constructor(private worklogService: WorkLogService) {
    }

    ngOnInit() {
        this.statisticPerProjectLoading = true;
        this.worklogService.statisticPerProject()
            .subscribe((res) => {
                this.statisticPerProjectLoading = false;
                this.statisticPerProject = res;
            });

        this.statisticPerEmployeeLoading = true;
        this.worklogService.statisticPerEmployee()
            .subscribe((res) => {
                this.statisticPerEmployeeLoading = false;
                this.statisticPerEmployee = res;
            });
    }

    toggleRawDataPerProject() {
        this.showRawDataPerProject = !this.showRawDataPerProject;
    }

    toggleRawDataPerEmployee() {
        this.showRawDataPerEmployee = !this.showRawDataPerEmployee;
    }
}

export interface Statistic {
    name: string;
    bookedHours: number;
}

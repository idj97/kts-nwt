
export class Report {
    ticketCount: number;
    income: number;
    labels: Array<string>;
    ticketData: Array<number>;
    incomeData: Array<number>;

    constructor() {
        this.labels = [];
        this.ticketData = [];
        this.incomeData = [];
    }

}
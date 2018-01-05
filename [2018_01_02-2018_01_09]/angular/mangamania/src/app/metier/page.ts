export class Page<T> {
    constructor(public content: T[],
                public number: number,
                public numberOfElements: number,
                public size: number,
                public totalElements: number,
                public totalPages: number,
                public first: boolean,
                public last: boolean,
                public sort: any) {}

}
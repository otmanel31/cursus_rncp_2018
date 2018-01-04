export class Manga {
    constructor(public id : number,
                public titre: string,
                public auteur: string,
                public dateParution: Date,
                public genre: string,
                public rating: number) {}
}
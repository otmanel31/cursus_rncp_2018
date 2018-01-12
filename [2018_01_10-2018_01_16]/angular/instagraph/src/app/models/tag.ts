import { Image } from "./image";

export class Tag {
    constructor(public id : number,
                public libelle: string,
                public descritpion: string,
                public contents? : Image[]) {}
}
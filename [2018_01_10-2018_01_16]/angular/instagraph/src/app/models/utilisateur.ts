import { Role } from "./role";

export class Utilisateur {
    constructor(public username : string,
                public password: string,
                public enabled: boolean,
                public roles?: Role[]) {}
}
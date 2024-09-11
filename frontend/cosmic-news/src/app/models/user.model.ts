export interface User {
    id?: number;
    name?: string;
    surname?: string;
    image?: string;
    hasImage?: boolean;
    description?: string;
    nick: string;
    mail: string;
    pass: string;
    roles: string[];
}

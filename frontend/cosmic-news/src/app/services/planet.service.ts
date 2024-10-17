import { Injectable } from "@angular/core";
import { Observable, throwError, catchError } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Planet } from "../models/planet.model";

const urlPlanets = '/api/planets'

@Injectable({ providedIn: 'root' })
export class PlanetService {
    constructor(private httpClient: HttpClient) { }

    getPlanets():Observable<Planet[]>{
        return this.httpClient.get<String[]>(urlPlanets).pipe(
            catchError(error=>this.handleError(error))
        ) as Observable<Planet[]>;
    }

    getPlanetImage(planet:Planet) {
        return this.httpClient.get(urlPlanets + "/" + planet.id + '/photo', { responseType: 'arraybuffer' })
    }

    private handleError(error: any) {
        console.error(error);
        return throwError("Server error (" + error.status + "):" + error.text())
    }
}
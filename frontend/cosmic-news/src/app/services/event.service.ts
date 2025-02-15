import { Injectable } from "@angular/core";
import { Observable, throwError, catchError } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Event } from "../models/event.model";

const urlEvents = '/api/events'

@Injectable({ providedIn: 'root' })
export class EventService {
    constructor(private httpClient: HttpClient) { }

    addOrUpdateEvent(event: Event){
        if (!event.id){
            return this.addEvent(event);
        } else{
            return this.updateEvent(event);
        }
    }

    getEvents():Observable<Event[]>{
        return this.httpClient.get<Event[]>(urlEvents).pipe(
            catchError(error=>this.handleError(error))
        ) as Observable<Event[]>;
    }

    getEvent(id: number): Observable<Object> {
        return this.httpClient.get(urlEvents + "/" + id);
    }

    deleteEvent(id: number | undefined) {
        return this.httpClient.delete(urlEvents + "/" + id);
    }

    editEvent(id: number | undefined, event: Event) {
        this.httpClient.put(urlEvents + "/" + id, event).subscribe();
        return true
    }

    newEvent(event: Event): Observable<Object>{
        return this.httpClient.post(urlEvents, event);
    }

    notifyEvent(id:number | undefined){
        return this.httpClient.post(urlEvents+"/"+id+"/notify",null);
    }

    private addEvent(event: Event){
        return this.httpClient.post(urlEvents,event).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private updateEvent(event:Event){
        console.log("UPDATE"+JSON.stringify(event));
        return this.httpClient.put(urlEvents+"/"+event.id,event).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private handleError(error: any) {
        console.error(error);
        return throwError("Server error (" + error.status + "):" + error.text())
    }
}
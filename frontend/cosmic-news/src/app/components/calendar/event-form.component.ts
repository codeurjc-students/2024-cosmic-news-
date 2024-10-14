import { Component, ViewChild } from '@angular/core';
import { Event } from '../../models/event.model';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { EventService } from '../../services/event.service';
import { Observable } from 'rxjs';

@Component({
    selector: "event-form",
    templateUrl: './event-form.component.html',
    styleUrl: '../../styles/form.css'
})
export class EventFormComponent{
    event:Event;
    edit:boolean;

    @ViewChild("file")
    file:any;

    constructor(activatedRoute: ActivatedRoute, private router:Router, private service: EventService, private httpClient: HttpClient) {
        let id = activatedRoute.snapshot.params['id'];
        let type : string | undefined;
        const routeSegments = activatedRoute.snapshot.url;
        this.edit=false;

        this.event = { }
        if (id){
            this.edit = true;

        }

        if (routeSegments.length > 0) {
            const firstSegment = routeSegments[0];
            type = firstSegment.path;
        }
        if (id) {
            service.getEvent(id).subscribe(
                event => {
                    this.event = event
                    this.edit = true;
                },
                error => console.error(error)
            );
        }else{
            this.event.icon="bi bi-sun";
        }
    }

    save() {
        this.service.addOrUpdateEvent(this.event).subscribe(
            response => {
                this.event = response as Event;
                this.router.navigate(['/calendar'])
            },
            error => {
                console.error('Error creating/updating the event: ' + error);
                this.router.navigate(['/calendar'])
            }
        );
    }

}
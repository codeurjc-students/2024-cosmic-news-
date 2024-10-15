import { Component, OnInit } from '@angular/core';
import { EventService } from '../../services/event.service';
import { Event } from '../../models/event.model';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { Me } from '../../models/me.model';
import { MessageService } from '../../services/message.service';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['../../styles/calendar.css']
})
export class CalendarComponent implements OnInit{
  logged: boolean = false;
  canAdd: boolean = false;
  me: Me;
  events: Event[] = [];
  today = new Date();
  days: { day: number, date: string, event?: Event | null }[] = [];
  month: number;
  year: number;
  selectedEvent: Event | null = null;
  modalVisible = false;

  constructor(private router: Router, private eventService: EventService, private userService: UserService, private messageService: MessageService) {
    this.month = this.today.getMonth();
    this.year = this.today.getFullYear();
  }

  ngOnInit(): void {
    this.fetchEvents();
    this.checkUser();
  }

  private checkUser() {
    this.userService.me().subscribe(
      response => {
        this.me = response as Me;
        this.logged=true;
        this.canAdd = (this.me.mail == "xd"); //Check the admin
      },
      _error => console.log("Error al obtener el usuario")
    );
  }

  fetchEvents(): void {
    this.eventService.getEvents().subscribe(
      (data: Event[]) => {
        this.events = data;
        this.createCalendar(this.year, this.month);
      },
      error => console.error('Error fetching events:', error)
    );
  }

  createCalendar(year: number, month: number): void {
    const daysInMonth = new Date(year, month + 1, 0).getDate();
    const startDay = new Date(year, month, 1).getDay();
    const days = [];

    for (let i = 1; i <= daysInMonth; i++) {
      const dateStr = `${year}-${(month + 1).toString().padStart(2, '0')}-${i.toString().padStart(2, '0')}`;
      const event = this.events.find(e => e.date?.startsWith(dateStr));
      days.push({ day: i, date: dateStr, event });
    }

    this.days = Array(startDay).fill(null).concat(days);
  }

  showDayInfo(day: any): void {
    if (!day) return;

    this.selectedEvent = day.event || { date: day.date, description: 'No hay información adicional para este día.' };
    this.modalVisible = true;
  }

  closeModal(): void {
    this.modalVisible = false;
  }

  deleteEvent(eventId: number | undefined): void {
    this.eventService.deleteEvent(eventId).subscribe(
      () => {
        this.fetchEvents();
        this.closeModal();
      },
      error => console.error('Error deleting event:', error)
    );
  }

  notifyEvent(eventId: number | undefined): void {
    if (this.logged){
      this.eventService.notifyEvent(eventId).subscribe(
        () => {
          this.messageService.showMessage("¡Recibirás una notificación cuando se produzca el evento!","/calendar");
        },
        error => console.error('Error notifying event:', error)
      );
    }else{
      this.router.navigate(['/login']);
    }
  }

  editEvent(eventId: number | undefined): void {
    this.router.navigate(['/events/'+eventId+'/edit']);
  }

  prevMonth() {
    if (this.month === 0) {
      this.month = 11;
      this.year--;
    } else {
      this.month--;
    }
    this.today = new Date(this.year, this.month);
    this.createCalendar(this.year, this.month);
  }

  nextMonth() {
    if (this.month === 11) {
      this.month = 0;
      this.year++;
    } else {
      this.month++;
    }
    this.today = new Date(this.year, this.month); 
    this.createCalendar(this.year, this.month);
  }

  getFormattedDate(): string {
    if (!this.selectedEvent?.date) return '';
    
    const date = new Date(this.selectedEvent.date);
    const day = date.getDate();
    const year = date.getFullYear();
    
    const months = ['enero', 'febrero', 'marzo', 'abril', 'mayo', 'junio', 'julio', 'agosto', 'septiembre', 'octubre', 'noviembre', 'diciembre'];
    const month = months[date.getMonth()];

    return `Información para el día ${day} de ${month} de ${year}`;
  }
}
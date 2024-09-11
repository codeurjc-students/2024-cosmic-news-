import { Injectable } from "@angular/core";
import { Observable, throwError, catchError, tap, take } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { User } from "../models/user.model";
import { LoginResponse } from "../models/login-response.model";

const urlUsers = '/api/users'

@Injectable({ providedIn: 'root'})
export class UserService{
    user:User;
    logged:boolean;

    constructor(private httpClient: HttpClient){
        this.user = {mail:"",pass:"",nick:"", roles:[]}
    }

    getMails():Observable<String[]>{
        return this.httpClient.get<String[]>("/api/mails").pipe(
            catchError(error=>this.handleError(error))
        ) as Observable<String[]>;
    }

    getUser(id: number): Observable<User>{
        return this.httpClient.get<User>(urlUsers+"/"+id).pipe(
            catchError(error => this.handleError(error))
        ) as Observable<User>
    }

    addOrUpdateUser(user: User){
        if (!user.id){
            return this.addUser(user);
        } else{
            return this.updateUser(user);
        }
    }

    deleteUser(user:User) {
		return this.httpClient.delete(urlUsers + "/" + user.id)
			.pipe(
				catchError(error => this.handleError(error))
			);
	}

    setUserImage(user: User, formData: FormData) {
		return this.httpClient.post(urlUsers + "/"+ user.id + '/photo', formData)
			.pipe(
				catchError(error => this.handleError(error))
			);
	}

	deleteUserImage(user: User) {
		return this.httpClient.delete(urlUsers + "/" + user.id + '/photo')
			.pipe(
				catchError(error => this.handleError(error))
			);
	}

    getUserImage(user:User) {
        return this.httpClient.get(urlUsers + "/" + user.id + '/photo', { responseType: 'arraybuffer' })
    }

    private addUser(user: User){
        return this.httpClient.post(urlUsers,user).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private updateUser(user:User){
        return this.httpClient.put(urlUsers+"/"+user.id,user).pipe(
            catchError(error => this.handleError(error))
        );
    }


    login(mail: string, pass: string): Observable<LoginResponse> {
        return this.httpClient.post("/api/auth/login", { username: mail, password: pass }, { withCredentials: true })
            .pipe(
                take(1),
                tap(_ => this.reqIsLogged()),
                catchError(error => {
                    alert("Wrong credentials");
                    return this.handleError(error)
                })
            ) as Observable<LoginResponse>
    }

    private reqIsLogged() {
        this.httpClient.get('/api/auth/me', { withCredentials: true }).subscribe(
            (response:any) => {
                this.user = response as User;
                this.logged = true;
            },
            error => {
                if (error.status != 404) {
                    console.error('Error when asking if logged: ' + JSON.stringify(error));
                }
            }
        );

    }

    logout() {
        this.logged = false;
        this.user = {mail:"",pass:"",nick:"", roles:[]}

        return this.httpClient.post('/api/auth/logout', { withCredentials: true })
            .pipe(
                catchError(error => this.handleError(error))
            )
    }

    isLogged() {
        return this.logged;
    }

    isAdmin() {
        return this.user && this.user.roles.indexOf('ADMIN') !== -1;
    }


    me(): Observable<Object>{
        return this.httpClient.get("/api/auth/me");
    }

    getCurrentUser(): User{
        return this.user
    }

    private handleError(error:any){
        console.error(error);
        return throwError("Server error ("+error.status+"):"+error.text())
    }
}
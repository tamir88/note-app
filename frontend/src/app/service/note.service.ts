import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { Level } from '../enum/level.enum';
import { CustomHttpResponse } from '../interface/custom-http-response';
import { Note } from '../interface/note';

@Injectable({
  providedIn: 'root'
})
export class NoteService {

  private readonly server = 'http://localhost:8080';

  constructor(private http: HttpClient) { }
  /* ------------ get notes -------------------------------------------------------------------*/

  // procedurale approuch 
  getNotes(): Observable<CustomHttpResponse> {
    return this.http.get<CustomHttpResponse>(`${this.server}/note/all`);
  }

  //reactive approuch
  notes$ = <Observable<CustomHttpResponse>>this.http.get<CustomHttpResponse>
    (`${this.server}/note/all`)
    .pipe(
      tap(console.log),
      catchError(this.handleError)
    );
  /* ------------ save note -------------------------------------------------------------------*/

  // procedurale approuch 
  saveNote(note: Note): Observable<CustomHttpResponse> {
    return this.http.post<CustomHttpResponse>(`${this.server}/note/add`, note);
  }

  //reactive approuch
  save$ = (note: Note) => <Observable<CustomHttpResponse>>this.http.post<CustomHttpResponse>
    (`${this.server}/note/add`, note)
    .pipe(
      tap(console.log),
      catchError(this.handleError)
    );
  /* ------------ update note -------------------------------------------------------------------*/

  // procedurale approuch 
  updateNote(note: Note): Observable<CustomHttpResponse> {
    return this.http.put<CustomHttpResponse>(`${this.server}/note/update`, note);
  }

  //reactive approuch
  update$ = (note: Note) => <Observable<CustomHttpResponse>>this.http.put<CustomHttpResponse>
    (`${this.server}/note/update`, note)
    .pipe(
      tap(console.log),
      catchError(this.handleError)
    );
  /* ------------ filter notes -------------------------------------------------------------------*/

  filterNotes$ = (level: Level, data: CustomHttpResponse) => <Observable<CustomHttpResponse>>
    new Observable<CustomHttpResponse>(subscriber => {
      subscriber.next(level === Level.ALL ? { ...data, message: data.notes.length > 0 ? `${data.notes.length} notes retrieved` : `No notes to display` } :
        <CustomHttpResponse>{
          ...data,
          message: data.notes.filter(note => note.level === level).length > 0 ? `Notes filtered by ${level.toLowerCase()} priority` : `No notes of ${level.toLowerCase()} priority found`,
          notes: data.notes.filter(note => note.level === level)
        });
      subscriber.complete();
    }).pipe(
      tap(console.log),
      catchError(this.handleError)
    );

/* ------------ delete notes -------------------------------------------------------------------*/

  // procedurale approuch 
  deleteNote(noteId: number): Observable<CustomHttpResponse> {
    return this.http.delete<CustomHttpResponse>(`${this.server}/note/delete/${noteId}`);
  }

  //reactive approuch
  delete$ = (noteId: number) => <Observable<CustomHttpResponse>>this.http.delete<CustomHttpResponse>
    (`${this.server}/note/delete/${noteId}`)
    .pipe(
      tap(console.log),
      catchError(this.handleError)
    );

  /* ------------ private methods -------------------------------------------------------------------*/
  private handleError(error: HttpErrorResponse): Observable<never> {
    console.log(error);
    let errorMessage: string;
    if (error.error instanceof ErrorEvent) {
      errorMessage = `A client error occurred - ${error.error.message}`;
    } else {
      if (error.error.reason) {
        errorMessage = `${error.error.reason} - Error code ${error.status}`;
      } else {
        `An error occurred - Error code ${error.status}`;
      }
    }
    return throwError(() => errorMessage);
  }
}

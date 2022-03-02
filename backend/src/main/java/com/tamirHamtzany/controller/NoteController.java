package com.tamirHamtzany.controller;

import com.tamirHamtzany.domain.HttpResponse;
import com.tamirHamtzany.domain.Note;
import com.tamirHamtzany.enumeration.Level;
import com.tamirHamtzany.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static com.tamirHamtzany.util.DateUtil.dateTimeFormatter;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/note")
public class NoteController {
    private final NoteService noteService;

    @GetMapping("/all")
    public ResponseEntity<HttpResponse<Note>> getNotes() throws InterruptedException {
        //TimeUnit.SECONDS.sleep(3);
       return ResponseEntity.ok().body(noteService.getNotes());
    }

    @PostMapping ("/add")
    public ResponseEntity<HttpResponse<Note>> saveNote(@RequestBody @Valid Note note){
        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/note/all").toUriString()))
                .body(noteService.saveNote(note));
    }

    @GetMapping("/filter")
    public ResponseEntity<HttpResponse<Note>> filterNotes(@RequestParam(value = "level") Level level){
        return ResponseEntity.ok().body(noteService.filterNotes(level));
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse<Note>> updateNote(@RequestBody @Valid Note note){
        return ResponseEntity.ok().body(noteService.updateNote(note));
    }

    @DeleteMapping ("/delete/{noteId}")
    public ResponseEntity<HttpResponse<Note>> deleteNote(@PathVariable(value = "noteId") Long id){
        return ResponseEntity.ok().body(noteService.deleteNote(id));
    }

    @RequestMapping ("/error")
    public ResponseEntity<HttpResponse<?>> handleError(HttpServletRequest request){
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason("There is no mapping for a " + request.getMethod() + " request for this path on the server")
                        .developerMessage("There is no mapping for a " + request.getMethod() + " request for this path on the server")
                        .status(HttpStatus.NOT_FOUND)
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                        .build(), HttpStatus.NOT_FOUND);
    }
}

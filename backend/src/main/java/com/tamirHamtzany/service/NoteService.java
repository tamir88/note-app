package com.tamirHamtzany.service;

import com.tamirHamtzany.domain.HttpResponse;
import com.tamirHamtzany.domain.Note;
import com.tamirHamtzany.enumeration.Level;
import com.tamirHamtzany.exception.NoteNotFoundException;
import com.tamirHamtzany.repo.NoteRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tamirHamtzany.util.DateUtil.dateTimeFormatter;
import static java.util.Collections.*;
import static java.util.Optional.*;
import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class NoteService {
    private final NoteRepo noteRepo;

    // method to get all notes
    public HttpResponse<Note> getNotes(){
        log.info("fetching all the notes from the database");
        return HttpResponse.<Note>builder()
                .notes(noteRepo.findAll().stream().sorted(Comparator.comparing(Note::getCreatedAt).reversed()).collect(Collectors.toList()))
                .message(noteRepo.count() > 0 ? noteRepo.count() + " notes retrieved" : "No notes to display")
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build();
    }

    // method to filter notes
    public HttpResponse<Note> filterNotes(Level level){
        log.info("fetching all the notes by level {}", level);
        return HttpResponse.<Note>builder()
                .notes(noteRepo.findByLevel(level).stream().sorted(Comparator.comparing(Note::getCreatedAt).reversed()).collect(Collectors.toList()))
                .message(noteRepo.count() + " notes are of " + level + " importance")
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build();
    }

    // method to save a new note
    public HttpResponse<Note> saveNote(Note note){
        log.info("Saving new note to the database");
        note.setCreatedAt(LocalDateTime.now());
        return HttpResponse.<Note>builder()
                .notes(singleton(noteRepo.save(note)))
                .message("Note created successfully")
                .status(CREATED)
                .statusCode(CREATED.value())
                .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build();
    }

    // method to update a note
    public HttpResponse<Note> updateNote(Note note) throws NoteNotFoundException  {
        log.info("Updating note to the database");
        Optional<Note> optionalNote = ofNullable(noteRepo.findById(note.getId()))
                .orElseThrow(()-> new NoteNotFoundException("The note was not found on the server"));
        Note updateNote = optionalNote.get();
        updateNote.setId(note.getId());
        updateNote.setTitle(note.getTitle());
        updateNote.setDescription(note.getDescription());
        updateNote.setLevel(note.getLevel());
        noteRepo.save(updateNote);
        return HttpResponse.<Note>builder()
                .notes(singleton(updateNote))
                .message("Note update successfully")
                .status(CREATED)
                .statusCode(CREATED.value())
                .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build();
    }

    // method to delete a note
    public HttpResponse<Note> deleteNote(Long id) throws NoteNotFoundException  {
        log.info("Deleting note from the database by id {}", id);
        Optional<Note> optionalNote = ofNullable(noteRepo.findById(id)
                .orElseThrow(()-> new NoteNotFoundException("The note was not found on the server")));
        noteRepo.deleteById(id);
        return HttpResponse.<Note>builder()
                .notes(singleton(optionalNote.get()))
                .message("Note deleted successfully")
                .status(CREATED)
                .statusCode(CREATED.value())
                .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build();
    }
}

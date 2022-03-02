package com.tamirHamtzany.repo;

import com.tamirHamtzany.domain.Note;
import com.tamirHamtzany.enumeration.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepo extends JpaRepository<Note, Long> {
     List<Note> findByLevel(Level level);
     void deleteById(Long id);
     Optional<Note> findById(Long id);
}

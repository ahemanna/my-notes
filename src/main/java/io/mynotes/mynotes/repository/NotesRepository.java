package io.mynotes.mynotes.repository;

import io.mynotes.mynotes.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotesRepository extends JpaRepository<Note, UUID> {
}

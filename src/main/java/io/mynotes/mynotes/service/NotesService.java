package io.mynotes.mynotes.service;

import io.mynotes.api.management.model.Note;
import io.mynotes.mynotes.repository.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class NotesService {

    @Autowired
    NotesRepository notesRepository;

    public Note createNote(Note note) {
        io.mynotes.mynotes.entity.Note n = toEntity(note);

        n.setUserId("test_user");
        n.setCreatedAt(OffsetDateTime.now());

        io.mynotes.mynotes.entity.Note n2 = notesRepository.save(n);
        return toModel(n2);
    }

    public io.mynotes.mynotes.entity.Note toEntity(Note note) {
        io.mynotes.mynotes.entity.Note n = new io.mynotes.mynotes.entity.Note();

        n.setTitle(note.getTitle());
        n.setContent(note.getContent());
        n.setModifiedAt(OffsetDateTime.now());

        return n;
    }

    public Note toModel(io.mynotes.mynotes.entity.Note note) {
        Note n = new Note();

        n.setId(note.getId());
        n.setTitle(note.getTitle());
        n.setContent(note.getContent());
        n.setCreatedAt(note.getCreatedAt());
        n.setModifiedAt(note.getModifiedAt());

        return n;
    }
}

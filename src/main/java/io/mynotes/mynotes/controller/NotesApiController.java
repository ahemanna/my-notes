package io.mynotes.mynotes.controller;

import io.mynotes.api.management.api.NotesApi;
import io.mynotes.api.management.model.Note;
import io.mynotes.api.management.model.Notes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;
import java.util.UUID;

@RestController
public class NotesApiController implements NotesApi {
    @Override
    public Optional<NativeWebRequest> getRequest() {
        return NotesApi.super.getRequest();
    }

    @Override
    public ResponseEntity<Note> createNote(Note note) {
        return NotesApi.super.createNote(note);
    }

    @Override
    public ResponseEntity<Void> deleteNote(UUID id) {
        return NotesApi.super.deleteNote(id);
    }

    @Override
    public ResponseEntity<Notes> listNotes(Integer offset, Integer limit) {
        return NotesApi.super.listNotes(offset, limit);
    }

    @Override
    public ResponseEntity<Note> retrieveNote(UUID id) {
        return NotesApi.super.retrieveNote(id);
    }

    @Override
    public ResponseEntity<Note> updateNote(UUID id, Note note) {
        return NotesApi.super.updateNote(id, note);
    }
}

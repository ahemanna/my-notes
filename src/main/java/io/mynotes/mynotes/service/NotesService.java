package io.mynotes.mynotes.service;

import io.mynotes.api.management.model.Note;
import io.mynotes.api.management.model.Notes;
import io.mynotes.mynotes.exception.NotFoundError;
import io.mynotes.mynotes.helper.Helper;
import io.mynotes.mynotes.repository.NotesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotesService {

    Logger logger = LoggerFactory.getLogger(NotesService.class);

    @Autowired
    NotesRepository notesRepository;

    public Notes listNotes(String username, Integer offset, Integer limit) {
        logger.info("Service :: listNotes");

        Pageable pageable = PageRequest.of(offset, limit);
        Page<io.mynotes.mynotes.entity.Note> notes = notesRepository.findAllByUserId(username, pageable);

        Notes notesList = new Notes();
        notesList.setItems(notes.getContent().stream().map(Helper::toModel).toList());
        notesList.setOffset(offset);
        notesList.setLimit(limit);
        notesList.setTotal(notes.getTotalElements());

        return notesList;
    }

    public Note createNote(Note note, String username) {
        logger.info("Service :: createNote");

        io.mynotes.mynotes.entity.Note n = Helper.toEntity(note);

        n.setUserId(username);

        OffsetDateTime now = OffsetDateTime.now();
        n.setCreatedAt(now);
        n.setModifiedAt(now);

        io.mynotes.mynotes.entity.Note n2 = notesRepository.save(n);
        return Helper.toModel(n2);
    }

    public Note retrieveNote(UUID id, String username) {
        logger.info("Service :: retrieveNote");

        io.mynotes.mynotes.entity.Note note = notesRepository.findByIdAndUserId(id, username);
        if(null == note) {
            logger.info(String.format("Note of ID %s not found", id));
            throw new NotFoundError(String.format("Note of ID %s not found", id));
        }

        return Helper.toModel(note);
    }

    public Note updateNote(UUID id, Note note, String username) {
        logger.info("Service :: updateNote");

        io.mynotes.mynotes.entity.Note n = notesRepository.findByIdAndUserId(id, username);
        if(null == n) {
            logger.info(String.format("Note of ID %s not found", id));
            throw new NotFoundError(String.format("Note of ID %s not found", id));
        }
        Optional.ofNullable(note.getTitle()).ifPresent(n::setTitle);
        Optional.ofNullable(note.getContent()).ifPresent(n::setContent);
        n.setModifiedAt(OffsetDateTime.now());

        io.mynotes.mynotes.entity.Note updatedNote = notesRepository.save(n);

        return Helper.toModel(updatedNote);
    }

    public void deleteNote(UUID id, String username) {
        logger.info("Service :: deleteNote");

        long count = notesRepository.deleteByIdAndUserId(id, username);

        if( count == 0) {
            logger.info(String.format("%d records found for the user ID %s", count, id));
            throw new NotFoundError(String.format("Note of ID %s not found", id));
        }
    }
}

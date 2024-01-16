package com.example.demo.visitor;

import com.example.demo.exception.PhoneTakenException;
import com.example.demo.exception.VisitorNotFoundException;
import com.example.demo.user.permission.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisitorService {
    private final static Logger logger = LoggerFactory.getLogger(VisitorService.class);
    private final VisitorRepository repository;

    @Autowired
    public VisitorService(VisitorRepository visitorRepository) {
        this.repository = visitorRepository;
    }

    public List<Visitor> getAll() {
        return repository.findAll();
    }

    public Visitor getById(Long id) {
        if (id == null) {
            logger.error("Given id is null. Termination of operation.");
            throw new NullPointerException("Given id is null.");
        }

        Optional<Visitor> optionalVisitor = repository.findById(id);

        if (optionalVisitor.isEmpty()) {
            logger.error("Visitor with given id not found. Termination of operation.");
            throw new VisitorNotFoundException("Visitor with given id not found.");
        }

        return optionalVisitor.get();
    }

    public void create(Visitor visitor) {
        if (visitor == null) {
            logger.error("Given visitor is null. Termination of operation.");
            throw new NullPointerException("Given visitor is null.");
        }

        if (repository.existsByPhone(visitor.getPhone())) {
            logger.error("Phone is taken. Termination of operation.");
            throw new PhoneTakenException("Phone is taken.");
        }

        visitor.setId(null);

        repository.save(visitor);
    }

    public void delete(Long id) {
        if (id == null) {
            logger.error("Given id is null. Termination of operation.");
            throw new NullPointerException("Given id is null.");
        }
        repository.deleteById(id);
    }

    public void edit(Long id, Visitor editedVisitor) {
        if (id == null || editedVisitor == null) {
            logger.error("Given id or visitor is null. Termination of operation.");
            throw new NullPointerException("Given id or visitor is null.");
        }

        if (repository.existsByPhone(editedVisitor.getPhone())) {
            logger.error("Phone is taken. Termination of operation.");
            throw new PhoneTakenException("Phone is taken.");
        }

        Optional<Visitor> optionalVisitor = repository.findById(id);
        if (optionalVisitor.isEmpty()) {
            logger.error("Visitor with given id not found. Termination of operation.");
            throw new VisitorNotFoundException("Visitor with given id not found.");
        }
        Visitor existingVisitor = optionalVisitor.get();

        existingVisitor.setFirstname(editedVisitor.getFirstname());
        existingVisitor.setLastname(editedVisitor.getLastname());
        existingVisitor.setPhone(editedVisitor.getPhone());
        existingVisitor.setEntry(editedVisitor.getEntry());
        existingVisitor.setExit(editedVisitor.getExit());
        existingVisitor.setRole(editedVisitor.getRole());

        repository.save(existingVisitor);
    }

}

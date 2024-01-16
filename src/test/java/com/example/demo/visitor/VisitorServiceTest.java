package com.example.demo.visitor;

import com.example.demo.user.permission.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class VisitorServiceTest {
    @Mock
    private VisitorRepository visitorRepository;
    private VisitorService service;

    @BeforeEach
    void setUp() {
        service = new VisitorService(visitorRepository);
    }

    private Visitor getNewVisitor(Long id) {
        return Visitor.builder()
                .id(id)
                .firstname("name")
                .lastname("lastname")
                .phone("+4418318238")
                .entry(new Date(System.currentTimeMillis()))
                .exit(new Date(System.currentTimeMillis()))
                .role(Role.VISITOR)
                .build();
    }

    @Test
    void itShouldGetById() {
        //given
        Long id = 1L;
        Visitor visitor = getNewVisitor(id);

        when(visitorRepository.findById(id)).thenReturn(Optional.of(visitor));

        //when
        Visitor recievedVisitor = service.getById(id);

        //then
        assertThat(recievedVisitor).isEqualTo(visitor);
    }

    @Test
    void itShouldEditVisitor() {
        //given
        Long id = 1L;
        Visitor visitor = getNewVisitor(id);
        Visitor edited = visitor;
        edited.setRole(Role.ADMIN);

        when(visitorRepository.existsByPhone(edited.getPhone())).thenReturn(false);
        when(visitorRepository.findById(id)).thenReturn(Optional.of(edited));

        ArgumentCaptor<Visitor> captor = ArgumentCaptor.forClass(Visitor.class);

        //when
        service.edit(id, edited);

        //then
        verify(visitorRepository, times(1)).save(captor.capture());

        assertThat(visitor).isEqualTo(captor.getValue());
    }

    @Test
    void itShouldCreateVisitor() {
        //given
        Long id = 1L;
        Visitor visitor = getNewVisitor(id);

        when(visitorRepository.existsByPhone(visitor.getPhone())).thenReturn(false);

        //when
        service.create(visitor);

        //then
        verify(visitorRepository, times(1)).save(any(Visitor.class));
    }









}










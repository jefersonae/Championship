package ifs.championship.service;

import ifs.championship.dto.EventDTO;
import ifs.championship.model.Event;
import ifs.championship.model.enums.EventStatus;
import ifs.championship.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public Event createEvent(EventDTO eventDTO) {
        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setCourseLevel(eventDTO.getLevel());
        event.setStatus(EventStatus.INSCRICOES_ABERTAS);
        return eventRepository.save(event);
    }
}
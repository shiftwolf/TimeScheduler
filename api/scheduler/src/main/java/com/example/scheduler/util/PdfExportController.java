package com.example.scheduler.util;

import com.example.scheduler.controller.EventController;
import com.example.scheduler.entities.EventsEntity;
import com.example.scheduler.entities.ParticipantsEntity;
import com.example.scheduler.entities.TokensEntity;
import com.example.scheduler.exceptions.EventNotFoundException;
import com.example.scheduler.exceptions.LoginFailedException;
import com.example.scheduler.exceptions.NoAuthorizationException;
import com.example.scheduler.repositories.EventRepository;
import com.example.scheduler.repositories.ParticipantRepository;
import com.example.scheduler.repositories.TokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletResponse;
import java.io.Console;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class PdfExportController {
    private final PdfGenerator pdfGenerator;
    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;
    private final TokenRepository tokenRepository;


    public PdfExportController(PdfGenerator pdfGenerator,
                               ParticipantRepository participantRepository,
                               EventRepository eventRepository,
                               TokenRepository tokenRepository) {
        this.pdfGenerator = pdfGenerator;
        this.participantRepository = participantRepository;
        this.eventRepository = eventRepository;
        this.tokenRepository = tokenRepository;
    }


    @GetMapping("/pdf/generate")
    public void generatePdf(HttpServletResponse response, @RequestHeader("userId") Long userId,
                            @RequestHeader("token") String token) throws IOException {
        //check if user is logged in (token)
        TokensEntity tokensEntity = tokenRepository.findById(token).orElseThrow(LoginFailedException::new);
        if(!(tokensEntity.getUserId().longValue() == userId.longValue())) {
            throw new NoAuthorizationException(userId);
        }

        //attach pdf to response
        response.setContentType("application/pdf");
        DateFormat dateFormater = new SimpleDateFormat("dd-MM-yyyy:hh:mm:ss");
        String currentDateTime = dateFormater.format(new Date());

        String headerkey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headerValue);

        List<ParticipantsEntity> participantsEntities =  participantRepository.findAllByUserId(userId);
        List<EventsEntity> eventsEntities = new ArrayList<>();
        for (ParticipantsEntity entity: participantsEntities) {
            eventsEntities.add(eventRepository.findById(entity.getEventId())
                    .orElseThrow(() -> new EventNotFoundException(entity.getEventId())));
        }

        if (eventsEntities.size() == 0)
            System.out.println("No events listed"); //todo: make new exception
        else
            this.pdfGenerator.PdfExport(response, eventsEntities);
    }
}

package com.example.scheduler.util;

import com.example.scheduler.controller.EventController;
import com.example.scheduler.entities.EventsEntity;
import com.example.scheduler.entities.ParticipantsEntity;
import com.example.scheduler.exceptions.EventNotFoundException;
import com.example.scheduler.repositories.EventRepository;
import com.example.scheduler.repositories.ParticipantRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PdfGenerator {
    public void PdfExport(HttpServletResponse response, List<EventsEntity> eventsEntities) throws IOException {
        //the document we want as a pdf
        Document document = new Document(PageSize.A4);

        //get the document we want to export and write it to the output stream of the response
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        //title of pdf
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);
        String textTitle = String.format("Schedule from %s to %s", eventsEntities.get(0).getDate(), eventsEntities.get(eventsEntities.size() - 1).getDate());;
        Paragraph paragraphTitle = new Paragraph(textTitle, fontTitle);
        paragraphTitle.setAlignment(Paragraph.ALIGN_CENTER);

        //context of pdf
        Font fontContext = FontFactory.getFont(FontFactory.HELVETICA);
        fontContext.setSize(12);
        Paragraph paragraphContext = new Paragraph("Context.", fontContext);
        paragraphContext.setAlignment(Paragraph.ALIGN_LEFT);

        //add title and context to the document
        document.add(paragraphTitle);
        document.add(paragraphContext);

        document.close();
    }
}

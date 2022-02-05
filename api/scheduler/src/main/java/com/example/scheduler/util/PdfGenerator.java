package com.example.scheduler.util;

import com.example.scheduler.entities.EventsEntity;
import com.example.scheduler.entities.ParticipantsEntity;
import com.example.scheduler.entities.UsersEntity;
import com.example.scheduler.repositories.UserRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Service
public class PdfGenerator {
    public void PdfExport(HttpServletResponse response, List<EventsEntity> eventsEntities, Long userId, UserRepository userRepository) throws IOException {
        //the document we want as a pdf
        Document document = new Document(PageSize.A4);

        //get the document we want to export and write it to the output stream of the response
        PdfWriter pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        //title of pdf
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);
        long endTime = eventsEntities.get(eventsEntities.size() - 1).getDate().getTime() + eventsEntities.get(eventsEntities.size() - 1).getDuration().getTime();
        String textTitle = String.format("Schedule from %s\n\n", getNeatDate(eventsEntities.get(0).getDate(), new Timestamp(endTime)));
        Paragraph paragraphTitle = new Paragraph(textTitle, fontTitle);
        paragraphTitle.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(paragraphTitle);

        //font settings
        Font fontContext = FontFactory.getFont(FontFactory.HELVETICA);
        fontContext.setSize(12);

        StringBuilder context = new StringBuilder();

        //context of pdf
        for (EventsEntity e : eventsEntities){

            //endTime describes the end of the event: eventStart + Duration
            endTime = e.getDate().getTime() + e.getDuration().getTime();

            //description of one event

            context.append("Name: " + e.getName());
            context.append("\nDate: ").append(getNeatDate(e.getDate(), new Timestamp(endTime)));
            context.append("\nDuration: ").append(getNeatDuration(e.getDate(), new Timestamp(endTime)));
            context.append("\nPriority: ").append(e.getPriority());

            if(!e.getLocation().equals(""))
                context.append("\nLocation: ").append(e.getLocation());

            List<ParticipantsEntity> participantsEntities = e.getParticipantsEntities();
            for(int i = 0; i < participantsEntities.size(); i++){
                if(Objects.equals(participantsEntities.get(i).getUserId(), userId))
                    participantsEntities.remove(i);
            }

            if (e.getParticipantsEntities().size() > 0)
                context.append("\nParticipants: ");

            for(int i = 0; i < e.getParticipantsEntities().size(); i++){
                Long userIdP = e.getParticipantsEntities().get(i).getUserId();
                //if(!Objects.equals(userIdP, userId)){
                    UsersEntity usersEntity = userRepository.findUserById(userIdP);
                    context.append(usersEntity.getUsername());
                    if (i < e.getParticipantsEntities().size() - 1)
                        context.append(", ");
                    if (i%4 == 0)
                        context.append("\n");
                //}
            }

            context.append("\n\n");
        }
        Paragraph paragraphContext = new Paragraph(context.toString(), fontContext);
        paragraphContext.setAlignment(Paragraph.ALIGN_LEFT);

        MultiColumnText multiColumnText = new MultiColumnText();
        multiColumnText.addSimpleColumn(5f, document.getPageSize().getWidth()/2);
        multiColumnText.addElement(paragraphContext);
        document.add(multiColumnText);
        //document.add(paragraphContext);

        document.close();
    }

    private String getNeatDuration(Timestamp begin, Timestamp end) {
        String neat = "";
        if(begin.getYear() <= end.getYear()){

        }




        return neat;
    }

    private String getNeatDate(Timestamp begin, Timestamp end){
        String neat = "";

        neat += begin.getDate();
        neat += "." + (begin.getMonth()+1);
        neat += "." + (begin.getYear()+1900);
        neat += " " + begin.getHours();
        neat += ":" + begin.getMinutes();

        neat += " - ";

        String endDay = end.toString().split(" ")[0];
        String beginDay = begin.toString().split(" ")[0];
        if (!endDay.equals(beginDay)){
            neat += end.getDate();
            neat += "." + (end.getMonth()+1);
            neat += "." + (end.getYear()+1900);
            neat += " ";
        }
        neat += end.getHours();
        neat += ":" + end.getMinutes();

        return neat;
    }
    
}

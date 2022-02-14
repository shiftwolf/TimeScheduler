package com.example.scheduler.util;

import com.example.scheduler.entities.EventsEntity;
import com.example.scheduler.entities.ParticipantsEntity;
import com.example.scheduler.entities.UsersEntity;
import com.example.scheduler.repositories.UserRepository;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class PdfGenerator {
    public void PdfExport(HttpServletResponse response, List<EventsEntity> eventsEntities,
                          Long userId, UserRepository userRepository)
            throws IOException {

        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(response.getOutputStream()));
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);
        newestDesign(document, eventsEntities, userId, userRepository);
        document.close();
    }

    private void newestDesign(Document document, List<EventsEntity> eventsEntities,
                              Long userId, UserRepository userRepository) throws IOException {

        Table table;

        //set the title of the Pdf
        long endTime = eventsEntities.get(eventsEntities.size() - 1).getDate().getTime()
                + eventsEntities.get(eventsEntities.size() - 1).getDuration().getTime();
        table = new Table(1).useAllAvailableWidth();
        table.addCell(new Cell().add("Schedule: " + getNeatDate(eventsEntities.get(0).getDate(), new Timestamp(endTime)))
                .setBorder(Border.NO_BORDER)
                .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD))
                .setFontSize(18f)
                .setTextAlignment(TextAlignment.CENTER)
        );
        document.add(table);

        //set content of Pdf
        boolean thisday = false;
        for(int i = 0; i < eventsEntities.size(); i++){
            if(!thisday){
                table = new Table(1).useAllAvailableWidth();
                String dayOfWeek = new SimpleDateFormat("EEEE", Locale.UK).format(new Date(eventsEntities.get(i).getDate().getTime()));
                dayOfWeek += " " + eventsEntities.get(i).getDate().getDate()
                        + "." + (eventsEntities.get(i).getDate().getMonth() + 1)
                        + "." + (eventsEntities.get(i).getDate().getYear() + 1900);
                table.addCell(new Cell().add(dayOfWeek)
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD))
                        .setFontSize(18f)
                );
                document.add(table);
            }

            table = new Table(new float[]{70f, 200f}).useAllAvailableWidth().setBorder(new SolidBorder(Color.LIGHT_GRAY, 1f));

            //name
            table.addCell(new Cell().add("Name:")
                    .setBorder(Border.NO_BORDER)
                    .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                    .setFontSize(12f)
            );
            table.addCell(new Cell().add(eventsEntities.get(i).getName())
                    .setBorder(Border.NO_BORDER)
                    .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                    .setFontSize(12f)
            );

            //date
            endTime = eventsEntities.get(i).getDate().getTime()
                    + eventsEntities.get(i).getDuration().getTime();
            table.addCell(new Cell().add("Date:")
                    .setBorder(Border.NO_BORDER)
                    .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                    .setFontSize(12f)
            );
            table.addCell(new Cell().add(getNeatDate(eventsEntities.get(i).getDate(), new Timestamp(endTime)))
                    .setBorder(Border.NO_BORDER)
                    .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                    .setFontSize(12f)
            );

            //duration
            table.addCell(new Cell().add("Duration:")
                    .setBorder(Border.NO_BORDER)
                    .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                    .setFontSize(12f)
            );
            table.addCell(new Cell().add(getNeatDuration(eventsEntities.get(i).getDuration().getTime()))
                    .setBorder(Border.NO_BORDER)
                    .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                    .setFontSize(12f)
            );

            //location
            table.addCell(new Cell().add("Location:")
                    .setBorder(Border.NO_BORDER)
                    .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                    .setFontSize(12f)
            );
            table.addCell(new Cell().add(eventsEntities.get(i).getLocation())
                    .setBorder(Border.NO_BORDER)
                    .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                    .setFontSize(12f)
            );

            //participants
            String participants = "";
            List<ParticipantsEntity> participantsEntityList = eventsEntities.get(i).getParticipantsEntities();
            for(int j = 0; j < participantsEntityList.size(); j++){
                Long userIdP = participantsEntityList.get(j).getUserId();
                if(!Objects.equals(userIdP, userId)) {
                    UsersEntity usersEntity = userRepository.findUserById(userIdP);
                    participants += usersEntity.getUsername();
                    if (j+1 < participantsEntityList.size())
                        participants += ", ";
                }
            }
            if(!participants.equals("")) {
                table.addCell(new Cell().add("Participants:")
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                        .setFontSize(12f)
                );
                table.addCell(new Cell().add(participants)
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                        .setFontSize(12f)
                );
            }
            document.add(table);

            table = new Table(1);
            table.addCell(new Cell().setBorder(Border.NO_BORDER));
            table.addCell(new Cell().setBorder(Border.NO_BORDER));
            table.addCell(new Cell().setBorder(Border.NO_BORDER));
            document.add(table);


            if(i+1 < eventsEntities.size()){
                if(eventsEntities.get(i).getDate().getDay() == eventsEntities.get(i+1).getDate().getDay()){
                    thisday = true;
                }
                else {
                    thisday = false;
                    table = new Table(1);
                    table.addCell(new Cell().setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().setBorder(Border.NO_BORDER));
                    document.add(table);
                }
            }
        }
    }

    private String getNeatDuration(Long duration) {
        String neat = "";

        Long days = duration / 1000L / 60L / 60L / 24L;
        if(days > 1)
            neat += days + " days ";
        else if(days > 0)
            neat += days + " day ";

        Long hours = (duration / 1000L / 60L / 60L) % 24;
        if(hours > 1)
            neat += hours + " hours ";
        else if (hours > 0)
            neat += hours + " hour ";

        Long minutes = (duration / 1000L / 60L) % 60;
        if(minutes > 1)
            neat += minutes + " minutes ";
        else if (minutes > 0)
            neat += minutes + " minute ";

        if(neat.equals(""))
            neat = "0 minutes";

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

package com.example.scheduler.util;

import com.example.scheduler.entities.EventsEntity;
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
import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;

@Service
public class PdfGenerator {
    public void PdfExport(HttpServletResponse response, List<EventsEntity> eventsEntities,
                          Long userId, UserRepository userRepository)
            throws IOException {

        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(response.getOutputStream()));
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);
        //newDesign(document, eventsEntities, userId, userRepository);
        //newerDesign(document, eventsEntities, userId, userRepository);
        newestDesign(document, eventsEntities, userId, userRepository);
        document.close();

        /*if (e.getParticipantsEntities().size() > 0)
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
        }*/
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
    }

    private void newDesign(Document document, List<EventsEntity> eventsEntities,
                           Long userId, UserRepository userRepository) throws IOException {
        //●⏺⬤
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

        //set the context of the Pdf
        boolean thisMonth = false;
        float[] columnWidth = new float[] {document.getPdfDocument().getDefaultPageSize().getWidth() / 3,
                document.getPdfDocument().getDefaultPageSize().getWidth() / 3,
                document.getPdfDocument().getDefaultPageSize().getWidth() / 3};
        for(int i = 0; i < eventsEntities.size(); i++){
            if(!thisMonth){
                //add month row
                table = new Table(1);
                table.addCell(new Cell().add(java.time.Month.of(eventsEntities.get(i).getDate().getMonth() + 1).getDisplayName(TextStyle.FULL, Locale.UK)
                        + " " + (eventsEntities.get(i).getDate().getYear() + 1900))
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD))
                        .setFontSize(14f));
                document.add(table);

                //add header row
                table = new Table(columnWidth);
                table.addCell(new Cell().add("Name")
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont((FontConstants.HELVETICA_BOLD)))
                        .setFontSize(12f)
                );
                table.addCell(new Cell().add("Date and Duration")
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont((FontConstants.HELVETICA_BOLD)))
                        .setFontSize(12f)
                );
                table.addCell(new Cell().add("Location and Participants")
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont((FontConstants.HELVETICA_BOLD)))
                        .setFontSize(12f)
                );
                document.add(table);
            }

            //add event rows
            table = new Table(columnWidth);

            table.addCell(new Cell(2,0).add(eventsEntities.get(i).getName())
                    //.setBorder(Border.NO_BORDER)
                    .setFont(PdfFontFactory.createFont((FontConstants.HELVETICA)))
                    .setFontSize(10f)
            );
            endTime = eventsEntities.get(i).getDate().getTime() + eventsEntities.get(i).getDuration().getTime();
            table.addCell(new Cell().add(getNeatDate(eventsEntities.get(i).getDate(), new Timestamp(endTime)))
                    //.setBorder(Border.NO_BORDER)
                    .setFont(PdfFontFactory.createFont((FontConstants.HELVETICA)))
                    .setFontSize(10f)
            );
            table.addCell(new Cell().add(eventsEntities.get(i).getLocation())
                    //.setBorder(Border.NO_BORDER)
                    .setFont(PdfFontFactory.createFont((FontConstants.HELVETICA)))
                    .setFontSize(10f)
            );
            table.addCell(new Cell().add(getNeatDuration(eventsEntities.get(i).getDuration().getTime()))
                    //.setBorder(Border.NO_BORDER)
                    .setFont(PdfFontFactory.createFont((FontConstants.HELVETICA)))
                    .setFontSize(10f)
            );
            document.add(table);
            if(i+1 < eventsEntities.size()){
                if(eventsEntities.get(i).getDate().getMonth() == eventsEntities.get(i+1).getDate().getMonth()) {
                    if (eventsEntities.get(i).getDate().getYear() == eventsEntities.get(i + 1).getDate().getYear())
                        thisMonth = true;
                }
                else {
                    thisMonth = false;
                    //document.add(new Paragraph("\n\n"));
                }
            }
        }
    }

    private void newerDesign(Document document, List<EventsEntity> eventsEntities,
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

        //set the context of the Pdf
        boolean thisMonth = false;
        float[] columnWidth = new float[] {document.getPdfDocument().getDefaultPageSize().getWidth() / 2 -2.5f,
                5f,
                document.getPdfDocument().getDefaultPageSize().getWidth() / 2 - 2.5f};
        table = new Table(columnWidth);
        boolean left = true;
        for(int i = 0; i < eventsEntities.size(); i++){
            if(!thisMonth){
                //add month row
                table.addCell(new Cell(0,3).add(java.time.Month.of(eventsEntities.get(i).getDate().getMonth() + 1).getDisplayName(TextStyle.FULL, Locale.UK)
                                + " " + (eventsEntities.get(i).getDate().getYear() + 1900))
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD))
                        .setFontSize(14f));

                table.addCell(new Cell().add(" ")
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                        .setFontSize(12f)
                );
                table.addCell(new Cell().add(" ")
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                        .setFontSize(12f)
                );
                table.addCell(new Cell().add(" ")
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                        .setFontSize(12f)
                );
            }


            float opacity = 0.15f;
            float fontSize = 10f;
            Color color;
            if(eventsEntities.get(i).getPriority() == 0)
                color = Color.GREEN;
            else if(eventsEntities.get(i).getPriority() == 1)
                color = Color.ORANGE;
            else
                color = Color.RED;

            if(left){
                table.addCell(new Cell().add(eventsEntities.get(i).getName())
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                        .setFontSize(fontSize)
                        .setBackgroundColor(color, opacity)
                        .setBorderTop(new SolidBorder(color, 1f))
                        .setBorderLeft(new SolidBorder(color, 1f))
                        .setBorderRight(new SolidBorder(color, 1f))
                );
                table.addCell(new Cell().setBorder(Border.NO_BORDER));
                table.addCell(new Cell()
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                        .setFontSize(fontSize)
                );
                endTime = eventsEntities.get(i).getDate().getTime() + eventsEntities.get(i).getDuration().getTime();
                table.addCell(new Cell().add(getNeatDate(eventsEntities.get(i).getDate(), new Timestamp(endTime)))
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                        .setFontSize(fontSize)
                        .setBackgroundColor(color, opacity)
                        .setBorderLeft(new SolidBorder(color, 1f))
                        .setBorderRight(new SolidBorder(color, 1f))
                );
                table.addCell(new Cell().setBorder(Border.NO_BORDER));
                table.addCell(new Cell()
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                        .setFontSize(fontSize)
                );
                table.addCell(new Cell().add(getNeatDuration(eventsEntities.get(i).getDuration().getTime()))
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                        .setFontSize(fontSize)
                        .setBackgroundColor(color, opacity)
                        .setBorderLeft(new SolidBorder(color, 1f))
                        .setBorderRight(new SolidBorder(color, 1f))
                );
                table.addCell(new Cell().setBorder(Border.NO_BORDER));
                table.addCell(new Cell()
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                        .setFontSize(fontSize)
                );
                table.addCell(new Cell().add(eventsEntities.get(i).getLocation())
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                        .setFontSize(fontSize)
                        .setBackgroundColor(color, opacity)
                        .setBorderLeft(new SolidBorder(color, 1f))
                        .setBorderRight(new SolidBorder(color, 1f))
                        .setBorderBottom(new SolidBorder(color, 1f))
                );
                table.addCell(new Cell().setBorder(Border.NO_BORDER));
                table.addCell(new Cell()
                        .setBorder(Border.NO_BORDER)
                        .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                        .setFontSize(fontSize)
                );
            }
            else{
                table.getCell(table.getNumberOfRows() - 5, 2)
                        .add(eventsEntities.get(i).getName())
                        .setBackgroundColor(color, opacity)
                        .setBorderTop(new SolidBorder(color, 1f))
                        .setBorderLeft(new SolidBorder(color, 1f))
                        .setBorderRight(new SolidBorder(color, 1f))
                ;
                endTime = eventsEntities.get(i).getDate().getTime() + eventsEntities.get(i).getDuration().getTime();
                table.getCell(table.getNumberOfRows() - 4, 2)
                        .add(getNeatDate(eventsEntities.get(i).getDate(), new Timestamp(endTime)))
                        .setBackgroundColor(color, opacity)
                        .setBorderLeft(new SolidBorder(color, 1f))
                        .setBorderRight(new SolidBorder(color, 1f))
                ;
                table.getCell(table.getNumberOfRows() - 3, 2)
                        .add(getNeatDuration(eventsEntities.get(i).getDuration().getTime()))
                        .setBackgroundColor(color, opacity)
                        .setBorderLeft(new SolidBorder(color, 1f))
                        .setBorderRight(new SolidBorder(color, 1f))
                ;
                table.getCell(table.getNumberOfRows() - 2, 2)
                        .add(eventsEntities.get(i).getLocation())
                        .setBackgroundColor(color, opacity)
                        .setBorderLeft(new SolidBorder(color, 1f))
                        .setBorderRight(new SolidBorder(color, 1f))
                        .setBorderBottom(new SolidBorder(color, 1f))
                ;
            }

            left = !left;

            table.addCell(new Cell().add(" ")
                    .setBorder(Border.NO_BORDER)
                    .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                    .setFontSize(12f)
            );
            table.addCell(new Cell().add(" ")
                    .setBorder(Border.NO_BORDER)
                    .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                    .setFontSize(12f)
            );
            table.addCell(new Cell().add(" ")
                    .setBorder(Border.NO_BORDER)
                    .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                    .setFontSize(12f)
            );

            table.setBorder(new SolidBorder(Color.LIGHT_GRAY, 1f));

            if(i+1 < eventsEntities.size()){
                if(eventsEntities.get(i).getDate().getMonth() == eventsEntities.get(i+1).getDate().getMonth()) {
                    if (eventsEntities.get(i).getDate().getYear() == eventsEntities.get(i + 1).getDate().getYear())
                        thisMonth = true;
                }
                else {
                    thisMonth = false;
                    document.add(table);
                    table = new Table(1);
                    table.addCell(new Cell().add(" ").setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().add(" ").setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().add(" ").setBorder(Border.NO_BORDER));
                    document.add(table);
                    LineSeparator lineSeparator = new LineSeparator(new SolidLine());
                    document.add(lineSeparator);
                    document.add(table);
                    table = new Table(columnWidth);
                    left = true;
                }
            }
        }
        document.add(table);

        table = new Table(new float[] {100f});
        table.addCell(new Cell().add("x")
                .setBorder(Border.NO_BORDER)
                .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                .setFontSize(18f)
                .setFontColor(Color.RED, 0f)
        );
        document.add(table);

        table = new Table(new float[] {100f});
        table.addCell(new Cell().add("name")
                .setBorder(Border.NO_BORDER)
                .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                .setFontSize(8f)
        );
        table.addCell(new Cell().add("date")
                .setBorder(Border.NO_BORDER)
                .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                .setFontSize(8f)
        );
        table.addCell(new Cell().add("duration")
                .setBorder(Border.NO_BORDER)
                .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                .setFontSize(8f)
        );
        table.addCell(new Cell().add("location")
                .setBorder(Border.NO_BORDER)
                .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                .setFontSize(8f)
        );
        table.setBorder(new SolidBorder(Color.LIGHT_GRAY, 1f));
        document.add(table);


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

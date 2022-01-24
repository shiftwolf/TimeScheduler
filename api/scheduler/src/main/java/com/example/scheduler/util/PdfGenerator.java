package com.example.scheduler.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class PdfGenerator {
    public void PdfExport(HttpServletResponse response) throws IOException {
        //the document we want as a pdf
        Document document = new Document(PageSize.A4);

        //get the document we want to export and write it to the outputstream of the response
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        //title of pdf
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);
        Paragraph paragraphTitle = new Paragraph("Title text.", fontTitle);
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

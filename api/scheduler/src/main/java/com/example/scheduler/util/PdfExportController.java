package com.example.scheduler.util;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class PdfExportController {
    private final PdfGenerator pdfGenerator;


    public PdfExportController(PdfGenerator pdfGenerator) {
        this.pdfGenerator = pdfGenerator;
    }


    @GetMapping("/pdf/generate")
    public void generatePdf(HttpServletResponse response) throws IOException {

        //attach pdf to response
        response.setContentType("application/pdf");
        DateFormat dateFormater = new SimpleDateFormat("dd-MM-yyyy:hh:mm:ss");
        String currentDateTime = dateFormater.format(new Date());

        String headerkey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headerValue);

        this.pdfGenerator.PdfExport(response);
    }
}

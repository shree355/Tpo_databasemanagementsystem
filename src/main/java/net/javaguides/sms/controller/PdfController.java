package net.javaguides.sms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;


@Controller

@RequestMapping("/pdf")
public class PdfController {



    /*   Logic to generate PDF using OpenPDF library*/
    @PostMapping("/create-pdf")
    public void createPdf() {
       
    }
    
}

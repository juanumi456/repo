package com.miaplicacion.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.miaplicacion.demo.model.Proyecto;
import com.miaplicacion.demo.service.EmpleadoService;
import com.miaplicacion.demo.service.ProyectoService;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public String listProyectos(Model model) {
        model.addAttribute("proyectos", proyectoService.findAll());
        return "proyectos";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        model.addAttribute("empleados", empleadoService.findAll());
        return "proyecto-form";
    }

    @PostMapping
    public String createProyecto(@ModelAttribute Proyecto proyecto) {
        proyectoService.save(proyecto);
        return "redirect:/proyectos";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Proyecto proyecto = proyectoService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid proyecto Id:" + id));
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("empleados", empleadoService.findAll());
        return "proyecto-form";
    }

    @PostMapping("/{id}")
    public String updateProyecto(@PathVariable String id, @ModelAttribute Proyecto proyecto) {
        proyecto.setId(id);
        proyectoService.save(proyecto);
        return "redirect:/proyectos";
    }

    @GetMapping("/{id}/delete")
    public String deleteProyecto(@PathVariable String id) {
        proyectoService.deleteById(id);
        return "redirect:/proyectos";
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportProyectosExcel() throws IOException {
        List<Proyecto> proyectos = proyectoService.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Proyectos");

        // Header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nombre");
        headerRow.createCell(2).setCellValue("Descripción");
        headerRow.createCell(3).setCellValue("Empleado ID");

        // Data rows
        int rowNum = 1;
        for (Proyecto proyecto : proyectos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(proyecto.getId());
            row.createCell(1).setCellValue(proyecto.getNombre());
            row.createCell(2).setCellValue(proyecto.getDescripcion());
            row.createCell(3).setCellValue(proyecto.getEmpleadoId());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "proyectos.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportProyectosPdf() throws IOException {
        List<Proyecto> proyectos = proyectoService.findAll();

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 750);
        contentStream.showText("Lista de Proyectos");
        contentStream.endText();

        contentStream.setFont(PDType1Font.HELVETICA, 10);
        float yPosition = 720;
        for (Proyecto proyecto : proyectos) {
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText("ID: " + proyecto.getId() + ", Nombre: " + proyecto.getNombre() +
                    ", Descripción: " + proyecto.getDescripcion() + ", Empleado ID: " + proyecto.getEmpleadoId());
            contentStream.endText();
            yPosition -= 15;
            if (yPosition < 50) {
                contentStream.close();
                PDPage newPage = new PDPage(PDRectangle.A4);
                document.addPage(newPage);
                contentStream = new PDPageContentStream(document, newPage);
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                yPosition = 750;
            }
        }
        contentStream.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "proyectos.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }
}
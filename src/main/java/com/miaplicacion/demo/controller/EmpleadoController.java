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

import com.miaplicacion.demo.model.Empleado;
import com.miaplicacion.demo.service.EmpleadoService;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public String listEmpleados(Model model) {
        model.addAttribute("empleados", empleadoService.findAll());
        return "empleados";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("empleado", new Empleado());
        return "empleado-form";
    }

    @PostMapping
    public String createEmpleado(@ModelAttribute Empleado empleado) {
        empleadoService.save(empleado);
        return "redirect:/empleados";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Empleado empleado = empleadoService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid empleado Id:" + id));
        model.addAttribute("empleado", empleado);
        return "empleado-form";
    }

    @PostMapping("/{id}")
    public String updateEmpleado(@PathVariable Long id, @ModelAttribute Empleado empleado) {
        empleado.setId(id);
        empleadoService.save(empleado);
        return "redirect:/empleados";
    }

    @GetMapping("/{id}/delete")
    public String deleteEmpleado(@PathVariable Long id) {
        empleadoService.deleteById(id);
        return "redirect:/empleados";
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportEmpleadosExcel() throws IOException {
        List<Empleado> empleados = empleadoService.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Empleados");

        // Header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nombre");
        headerRow.createCell(2).setCellValue("Cargo");
        headerRow.createCell(3).setCellValue("Email");
        headerRow.createCell(4).setCellValue("Salario");

        // Data rows
        int rowNum = 1;
        for (Empleado empleado : empleados) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(empleado.getId());
            row.createCell(1).setCellValue(empleado.getNombre());
            row.createCell(2).setCellValue(empleado.getCargo());
            row.createCell(3).setCellValue(empleado.getEmail());
            row.createCell(4).setCellValue(empleado.getSalario());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "empleados.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportEmpleadosPdf() throws IOException {
        List<Empleado> empleados = empleadoService.findAll();

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 750);
        contentStream.showText("Lista de Empleados");
        contentStream.endText();

        contentStream.setFont(PDType1Font.HELVETICA, 10);
        float yPosition = 720;
        for (Empleado empleado : empleados) {
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText("ID: " + empleado.getId() + ", Nombre: " + empleado.getNombre() +
                    ", Cargo: " + empleado.getCargo() + ", Email: " + empleado.getEmail() + ", Salario: " + empleado.getSalario());
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
        headers.setContentDispositionFormData("attachment", "empleados.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }
}
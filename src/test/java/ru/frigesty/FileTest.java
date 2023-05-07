package ru.frigesty;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static com.codeborne.pdftest.PDF.containsExactText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Чтение и проверкa содержимого каждого файла из архива")
public class FileTest {

    public ClassLoader cl = FileTest.class.getClassLoader();

    @DisplayName("Чтение и проверка содержимого pdf файла из архива")
    @Test
    void readPDFTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File("src/test/resources/TestZip.zip"));
        try (InputStream is = cl.getResourceAsStream("TestZip.zip")) {
            assert is != null;
            try (ZipInputStream zipInputStream = new ZipInputStream(is)) {
                ZipEntry entry;
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    if (entry.getName().endsWith(".pdf"))
                        try (InputStream inputStreamPdf = zipFile.getInputStream(entry)) {
                        PDF pdf = new PDF(inputStreamPdf);
                        assertEquals(7, pdf.numberOfPages);
                        assertEquals("iit", pdf.author);
                        assertEquals("Bullzip PDF Printer / www.bullzip.com / Freeware Edition", pdf.producer);
                        assertThat(pdf, containsExactText("Научная статья - руководство по написанию."));
                        assertThat(pdf, containsExactText("Keywords: investment, dynamics, management, project financing"));
                        }
                }
            }
        }
    }

    @DisplayName("Чтение и проверка содержимого xlsx файла из архива")
    @Test
    void readXLSXTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File("src/test/resources/TestZip.zip"));
        try (InputStream is = cl.getResourceAsStream("TestZip.zip")) {
            assert is != null;
            try (ZipInputStream zipInputStream = new ZipInputStream(is)) {
                ZipEntry entry;
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    if (entry.getName().endsWith(".xlsx"))
                        try (InputStream inputStreamXlsx = zipFile.getInputStream(entry)) {
                            XLS xls = new XLS(inputStreamXlsx);
                            Assertions.assertEquals("Dr Andrew JOHNSON", xls.excel.getSheet("Executive Council Members _...").getRow(27).getCell(3).toString());
                            Assertions.assertEquals("pr_australia@bom.gov.au", xls.excel.getSheet("Executive Council Members _...").getRow(27).getCell(8).toString());
                            Assertions.assertEquals("Ms Diane CAMPBELL", xls.excel.getSheet("Executive Council Members _...").getRow(22).getCell(3).toString());
                            Assertions.assertEquals("Canada", xls.excel.getSheet("Executive Council Members _...").getRow(22).getCell(5).toString());
                        }
                }
            }
        }
    }

    @DisplayName("Чтение и проверка содержимого csv файла из архива")
    @Test
    public void readCSVTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File("src/test/resources/TestZip.zip"));
        try (InputStream is = cl.getResourceAsStream("TestZip.zip")) {
            assert is != null;
            try (ZipInputStream zipInputStream = new ZipInputStream(is)) {
                ZipEntry entry;
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    if (entry.getName().endsWith(".csv"))
                        try (InputStream inputStreamCsv = zipFile.getInputStream(entry)) {
                            CSVReader csvReader = new CSVReader(new InputStreamReader(inputStreamCsv, StandardCharsets.UTF_8));
                            List<String[]> csvContent = csvReader.readAll().subList(0, 6);
                            org.assertj.core.api.Assertions.assertThat(csvContent).contains(
                                    new String[]{"Name", "Last_name", "Position", "E-mail_address"},
                                    new String[]{"Ivanova", "Irina", "Vitalievna", "Head_of_Department", "iivanova@company.ru"},
                                    new String[]{"Sergeev", "Konstantin", "Igorevich", "Marketer", "ksergeev@company.ru"},
                                    new String[]{"Buchelnikov", "Nikolay", "Ivanovich", "Recruiter", "nbuchelnikov@company.ru"},
                                    new String[]{"Gulina", "Alena", "Evgenievna", "Specialist", "agulina@company.ru"},
                                    new String[]{"Kalashnikov", "Evgeny", "Valentinovich", "Head_of_Department", "ekalashnikov@company.ru"}
                            );
                        }
                }
            }
        }
    }
}
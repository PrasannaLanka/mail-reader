//package mail.reader.example.service;
//
//import com.google.api.services.gmail.Gmail;
//import com.google.api.services.gmail.model.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import mail.reader.example.client.ExcelClient;
//
//import java.time.LocalDate;
//import java.util.Base64;
//
//@Service
//@RequiredArgsConstructor
//public class GmailService {
//
//    private final Gmail gmail;
//    private final ExcelClient excelClient;
//
//    public void processEmails() {
//
//        try {
//
//            String query = "from:itz.meprasu16@gmail.com "
//                    + "after:" + LocalDate.now().minusDays(1)
//                    + " test:xlsx";
//
//            ListMessagesResponse response =
//                    gmail.users().messages()
//                            .list("me")
//                            .setQ(query)
//                            .execute();
//
//            if (response.getMessages() == null) return;
//
//            for (Message message : response.getMessages()) {
//
//                Message fullMessage =
//                        gmail.users().messages()
//                                .get("me", message.getId())
//                                .execute();
//
//                extractAndSendAttachment(fullMessage);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void extractAndSendAttachment(Message message) throws Exception {
//
//        if (message.getPayload().getParts() == null) return;
//
//        for (MessagePart part : message.getPayload().getParts()) {
//
//            if (part.getFilename() != null &&
//                    part.getFilename().endsWith(".xlsx")) {
//
//                String attachmentId = part.getBody().getAttachmentId();
//
//                MessagePartBody attachPart =
//                        gmail.users().messages().attachments()
//                                .get("me", message.getId(), attachmentId)
//                                .execute();
//
//                byte[] fileBytes =
//                        Base64.getUrlDecoder().decode(attachPart.getData());
//
//                excelClient.sendExcel(fileBytes);
//            }
//        }
//    }
//}

package mail.reader.example.service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Base64;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
@RequiredArgsConstructor
public class GmailService {

    @Autowired
    private final Gmail gmail;

    public void processEmails() {

        try {

            String query = "from:itz.meprasu16@gmail.com "
                    + "after:" + LocalDate.now().minusDays(1)
                    + " test:xlsx";

            System.out.println("Searching with query: " + query);

            ListMessagesResponse response =
                    gmail.users().messages()
                            .list("me")
                            .setQ(query)
                            .execute();

            if (response.getMessages() == null) {
                System.out.println("No messages found.");
                return;
            }

            System.out.println("Messages found: " + response.getMessages().size());

            for (Message message : response.getMessages()) {

                Message fullMessage =
                        gmail.users().messages()
                                .get("me", message.getId())
                                .execute();

                extractAttachment(fullMessage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void extractAttachment(Message message) throws Exception {

        if (message.getPayload().getParts() == null) {
            System.out.println("No parts in message.");
            return;
        }

        for (MessagePart part : message.getPayload().getParts()) {

            if (part.getFilename() != null &&
                    part.getFilename().endsWith(".xlsx")) {

                System.out.println("Found attachment: " + part.getFilename());

                String attachmentId = part.getBody().getAttachmentId();

                MessagePartBody attachPart =
                        gmail.users().messages().attachments()
                                .get("me", message.getId(), attachmentId)
                                .execute();

                byte[] fileBytes =
                        Base64.getUrlDecoder().decode(attachPart.getData());

                System.out.println("Reading Excel content...");

                readExcel(fileBytes);
            }
        }
    }

    private void readExcel(byte[] fileBytes) throws Exception {

        try (ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes);
             Workbook workbook = new XSSFWorkbook(bis)) {

            Sheet sheet = workbook.getSheetAt(0);

            System.out.println("----- Excel Content -----");

            for (Row row : sheet) {

                for (Cell cell : row) {

                    switch (cell.getCellType()) {
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;
                        case NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t");
                            break;
//                        case BOOLEAN:
//                            System.out.print(cell.getBooleanValue() + "\t");
//                            break;
                        case FORMULA:
                            System.out.print(cell.getCellFormula() + "\t");
                            break;
                        default:
                            System.out.print("\t");
                    }
                }
                System.out.println();
            }

            System.out.println("--------------------------");
        }
    }
}

package socialnetwork.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import socialnetwork.domain.Utilizator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.stream.Stream;

public class ServiceReports {
    ServicePrietenii servicePrietenii;
    ServiceMesaje serviceMesaje;

    public ServiceReports(ServicePrietenii servicePrietenii, ServiceMesaje serviceMesaje) {
        this.servicePrietenii = servicePrietenii;
        this.serviceMesaje = serviceMesaje;
    }

    /**
     * makes a report for a user and saves it to the specified path containing new friends and new messages between 2 dates
     * @param user user reference
     * @param fromDate starting date
     * @param toDate ending date
     * @param path folder path
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public void activityReportPDF(Utilizator user, LocalDate fromDate, LocalDate toDate, String path) throws FileNotFoundException, DocumentException {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream( path + "/ActivityReport" + user.getId() + ".pdf"));
        doc.open();
        Paragraph paragraphMain = new Paragraph("Report of " + user + " 's activity");
        Paragraph paragraph1 = new Paragraph("New Friends");
        Paragraph paragraph2 = new Paragraph("New Messages");
        paragraph1.add(createFriendsTable(user,fromDate,toDate));
        paragraph2.add(createMessagesTable(user,fromDate,toDate));
        paragraphMain.add(paragraph1);
        paragraphMain.add(paragraph2);
        doc.add(paragraphMain);
        doc.close();
    }

    /**
     *
     * @param user user reference
     * @param fromDate starting date
     * @param toDate ending date
     * @return creates a pdf table with a user's friends
     */
    private PdfPTable createFriendsTable(Utilizator user, LocalDate fromDate, LocalDate toDate){
        PdfPTable table1 = new PdfPTable(3);
        Stream.of("First Name", "Last name", "Date").forEach(table1::addCell);
        servicePrietenii.getFriendsDate(user.getId(),fromDate,toDate).forEach(fr->{
            table1.addCell(fr.getFirstName());
            table1.addCell(fr.getLastName());
            table1.addCell(fr.getFriendshipDate().toString());
        });
        return table1;
    }

    /**
     *
     * @param user user reference
     * @param fromDate starting date
     * @param toDate ending date
     * @return a pdf table containing messages received by the user between 2 dates
     */
    private PdfPTable createMessagesTable(Utilizator user, LocalDate fromDate, LocalDate toDate){
        PdfPTable table2 = new PdfPTable(3);
        Stream.of("From", "Message", "Date").forEach(table2::addCell);
        serviceMesaje.getMesajeDate(user.getId(),fromDate,toDate).forEach(msg->{
            table2.addCell(msg.getFrom().toString());
            table2.addCell(msg.getText());
            table2.addCell(msg.getDate().toString());
        });
        return table2;
    }

    /**
     * creates a report of user1's conversation with user2 and saves it to the specified path
     * @param user user1 reference
     * @param user2 user2 reference
     * @param fromDate starting date
     * @param toDate ending date
     * @param path folder path
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public void conversationReportPDF(Utilizator user, Utilizator user2,LocalDate fromDate, LocalDate toDate, String path) throws FileNotFoundException, DocumentException {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream( path + "/ConversationReport" + user.getId() +user2.getId()+ ".pdf"));
        doc.open();
        Paragraph paragraphMain = new Paragraph("Report of " + user + " 's conversation with " + user2);
        Paragraph paragraph1 = new Paragraph("New Messages");
        paragraph1.add(createConversationTable(user,user2,fromDate,toDate));
        paragraphMain.add(paragraph1);
        doc.add(paragraphMain);
        doc.close();
    }

    /**
     *
     * @param user user1 reference
     * @param user2 user2 reference
     * @param fromDate starting date
     * @param toDate ending date
     * @return pdf table containing a conversation between 2 users between 2 dates
     */
    private PdfPTable createConversationTable(Utilizator user, Utilizator user2,LocalDate fromDate, LocalDate toDate) {
        PdfPTable table2 = new PdfPTable(3);
        Stream.of("From", "Message", "Date").forEach(table2::addCell);
        serviceMesaje.getConversatieDate(user.getId(),user2.getId(),fromDate,toDate).forEach(msg->{
            table2.addCell(msg.getFrom().toString());
            table2.addCell(msg.getText());
            table2.addCell(msg.getDate().toString());
        });
        return table2;
    }
}

package com.mbooking.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.mbooking.model.Reservation;
import com.mbooking.model.ReservationDetails;
import com.mbooking.service.PDFCreatorService;
import com.mbooking.utility.Constants;

@Service
public class PDFCreatorServiceImpl implements PDFCreatorService{
	
	private static Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.BLACK);
	private static Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
	@SuppressWarnings("unused")
	private static Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
	private static Font tableContentFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
	
	private static final int CELL_PADDING = 14;
	
	
	
	@Override
	public ByteArrayOutputStream createReservationPDF(Reservation reservation) {
		@SuppressWarnings("unused")
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
		Document document = new Document(PageSize.A4, 36, 36, 36, 56);
		PdfWriter writer = null;
		try {
			writer = PdfWriter.getInstance(document, byteArrayOutputStream);
			//writer = PdfWriter.getInstance(document, new FileOutputStream("ReservationTestPDF.pdf"));
			writer.setPageEvent(new HeaderFooterPageEvent());
		} catch (Exception e) {
			return null;
		}
		document.open();
		
		
		try {
			document.newPage();
			document.addTitle("Reservation");
			Paragraph header = new Paragraph("Reservation", titleFont);
			header.setAlignment(Element.ALIGN_CENTER);
	        document.add(header);
	        
	        document.add(new Paragraph(" ")); //New empty space
	        document.add(new Paragraph(" ")); //New empty space
	        
	        Paragraph p1 = new Paragraph(
	        		"Thank you for making reservation in m-booking.", paragraphFont);
	        p1.setAlignment(Element.ALIGN_LEFT);
	        p1.setIndentationLeft(50);
	        document.add(p1);
	        
	        document.add(new Paragraph(" ")); //New empty space
	        Paragraph p2 = new Paragraph(
	        		"Description: " + reservation.getManifestation().getDescription(), paragraphFont);
	        p2.setAlignment(Element.ALIGN_LEFT);
	        p2.setIndentationLeft(50);
	        document.add(p2);
	        
	        document.add(new Paragraph(" ")); //New empty space
	        document.add(new Paragraph(" ")); //New empty space
	        
	        PdfPTable table = new PdfPTable(2);	//2 columns;
	        PdfPCell c1 = new PdfPCell(new Phrase("Manifestation name", tableContentFont));
	        c1.setPadding(CELL_PADDING);
	        
	        PdfPCell c2 = new PdfPCell(new Phrase(reservation.getManifestation().getName(), tableContentFont));
	        c2.setPadding(CELL_PADDING);
	        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        
	        PdfPCell c3 = new PdfPCell(new Phrase("Reservable until", tableContentFont));
	        c3.setPadding(CELL_PADDING);
	        
	        PdfPCell c4 = new PdfPCell(new Phrase(
	        		format.format(reservation.getManifestation().getReservableUntil()),
	        		tableContentFont));
	        c4.setPadding(CELL_PADDING);
	        c4.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        
	        PdfPCell c5 = new PdfPCell(new Phrase("Customer name", tableContentFont));
	        c5.setPadding(CELL_PADDING);
	        
	        PdfPCell c6 = new PdfPCell(new Phrase(reservation.getCustomer().getFirstname() + " " +
	        		reservation.getCustomer().getLastname(), tableContentFont));
	        c6.setPadding(CELL_PADDING);
	        c6.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        
	        PdfPCell c7 = new PdfPCell(new Phrase("Total price", tableContentFont));
	        c7.setPadding(CELL_PADDING);
	        
	        PdfPCell c8 = new PdfPCell(new Phrase(reservation.getPrice() + " " +  Constants.DEFAULT_CURRENCY,
	        		tableContentFont));
	        c8.setPadding(CELL_PADDING);
	        c8.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        
	        PdfPCell c9 = new PdfPCell(new Phrase("Reservation date",
	        		tableContentFont));
	        c9.setPadding(CELL_PADDING);
	        
	        PdfPCell c10 = new PdfPCell(new Phrase(format.format(reservation.getDateCreated()),
	        		tableContentFont));
	        c10.setPadding(CELL_PADDING);
	        c10.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        
	        PdfPCell c11 = new PdfPCell(new Phrase("Expiration date",
	        		tableContentFont));
	        c11.setPadding(CELL_PADDING);
	        
	        PdfPCell c12 = new PdfPCell(new Phrase(format.format(reservation.getExpirationDate()),
	        		tableContentFont));
	        c12.setPadding(CELL_PADDING);
	        c12.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        
	        table.addCell(c1);
	        table.addCell(c2);
	        table.addCell(c3);
	        table.addCell(c4);
	        table.addCell(c5);
	        table.addCell(c6);
	        table.addCell(c7);
	        table.addCell(c8);
	        table.addCell(c9);
	        table.addCell(c10);
	        table.addCell(c11);
	        table.addCell(c12);
	        document.add(table);
	        
	        document.add(new Paragraph(" ")); //New empty space
	        document.add(new Paragraph(" ")); //New empty space
	        
	        Paragraph moreInfo = new Paragraph(
	        		"More information:", paragraphFont);
	        moreInfo.setAlignment(Element.ALIGN_LEFT);
	        moreInfo.setIndentationLeft(50);
	        document.add(moreInfo);
	        
	        for (ReservationDetails details : reservation.getReservationDetails()) {
	        	PdfPTable detailsTable = new PdfPTable(2);	//2 columns;
	        	document.add(new Paragraph(" ")); //New empty space
	        	
	        	PdfPCell cc1 = new PdfPCell(new Phrase("Manifestation day",
		        		tableContentFont));
		        cc1.setPadding(CELL_PADDING);
		        detailsTable.addCell(cc1);
		        
		        PdfPCell cc2 = new PdfPCell(new Phrase(format.format(details.getManifestationDay().getDate()),
		        		tableContentFont));
		        cc2.setPadding(CELL_PADDING);
		        cc2.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        detailsTable.addCell(cc2);
		        
		        PdfPCell cc3 = new PdfPCell(new Phrase("Manifestation section",
		        		tableContentFont));
		        cc3.setPadding(CELL_PADDING);
		        detailsTable.addCell(cc3);
		        
		        PdfPCell cc4 = new PdfPCell(new Phrase(details
		        		.getManifestationSection()
		        		.getSelectedSection()
		        		.getName(),
		        		tableContentFont));
		        cc4.setPadding(CELL_PADDING);
		        cc4.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        detailsTable.addCell(cc4);
		        
		        PdfPCell cc5 = new PdfPCell(new Phrase("Is seating",
		        		tableContentFont));
	        	cc5.setPadding(CELL_PADDING);
		        detailsTable.addCell(cc5);
		        
		        PdfPCell cc6 = new PdfPCell(new Phrase(details.isSeating() ? "Yes" : "No",
		        		tableContentFont));
		        cc6.setPadding(CELL_PADDING);
		        cc6.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        detailsTable.addCell(cc6);
		        
		        if (details.isSeating()) {
		        	PdfPCell cc7 = new PdfPCell(new Phrase("Row",
			        		tableContentFont));
		        	cc7.setPadding(CELL_PADDING);
			        detailsTable.addCell(cc7);
			        
			        PdfPCell cc8 = new PdfPCell(new Phrase(details.getRow() + "",
			        		tableContentFont));
			        cc8.setPadding(CELL_PADDING);
			        cc8.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        detailsTable.addCell(cc8);
			        
			        PdfPCell cc9 = new PdfPCell(new Phrase("Column",
			        		tableContentFont));
			        cc9.setPadding(CELL_PADDING);
			        detailsTable.addCell(cc9);
			        
			        PdfPCell cc10 = new PdfPCell(new Phrase(details.getColumn() + "",
			        		tableContentFont));
			        cc10.setPadding(CELL_PADDING);
			        cc10.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        detailsTable.addCell(cc10);
		        }
		        
		        
		        document.add(detailsTable);
	        }
	        
	        
		} catch (DocumentException e) {
			document.close();
		}
		
        
        
		
		document.close();
		
		return byteArrayOutputStream;
		
	}
	
	
	
	
	// ---------- TEST ----------
	
	@Override
	public void createTestPDF() {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("TestPDF.pdf"));
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
		 
		document.open();
		 
		PdfPTable table = new PdfPTable(3);
		addTableHeader(table);
		addRows(table);
		try {
			addCustomRows(table);
		} catch (BadElementException | URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		 
		try {
			document.add(table);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		document.close();
		
	}
	
	
	//Utility
	
	private void addTableHeader(PdfPTable table) {
	    Stream.of("column header 1", "column header 2", "column header 3")
	      .forEach(columnTitle -> {
	        PdfPCell header = new PdfPCell();
	        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        header.setBorderWidth(2);
	        header.setPhrase(new Phrase(columnTitle));
	        table.addCell(header);
	    });
	}
	
	private void addRows(PdfPTable table) {
	    table.addCell("row 1, col 1");
	    table.addCell("row 1, col 2");
	    table.addCell("row 1, col 3");
	}
	
	private void addCustomRows(PdfPTable table) 
	  throws URISyntaxException, BadElementException, IOException {
	 
	    PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
	    horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.addCell(horizontalAlignCell);
	 
	    PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
	    verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
	    table.addCell(verticalAlignCell);
	}
	
	
	// Auxilary class
	
	private static class HeaderFooterPageEvent extends PdfPageEventHelper {
		private PdfTemplate t;
		private Image total;

	    public void onOpenDocument(PdfWriter writer, Document document) {
	        t = writer.getDirectContent().createTemplate(30, 16);
	        try {
	            total = Image.getInstance(t);
	            total.setRole(PdfName.ARTIFACT);
	        } catch (DocumentException de) {
	            throw new ExceptionConverter(de);
	        }
	    }
		
		@Override
	    public void onEndPage(PdfWriter writer, Document document) {
	        addFooter(writer);
	    }

	    private void addFooter(PdfWriter writer){
	        PdfPTable footer = new PdfPTable(3);
	        try {
	            // set defaults
	            footer.setWidths(new int[]{24, 2, 1});
	            footer.setTotalWidth(527);
	            footer.setLockedWidth(true);
	            footer.getDefaultCell().setFixedHeight(40);
	            footer.getDefaultCell().setBorder(Rectangle.TOP);
	            footer.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

	            // add copyright
	            footer.addCell(new Phrase("\u00A9 m-booking", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));

	            // add current page count
	            footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            footer.addCell(new Phrase(String.format("Page %d of", writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)));

	            // add placeholder for total page count
	            PdfPCell totalPageCount = new PdfPCell(total);
	            totalPageCount.setBorder(Rectangle.TOP);
	            totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
	            footer.addCell(totalPageCount);

	            // write page
	            PdfContentByte canvas = writer.getDirectContent();
	            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
	            footer.writeSelectedRows(0, -1, 34, 50, canvas);
	            canvas.endMarkedContentSequence();
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public void onCloseDocument(PdfWriter writer, Document document) {
	        int totalLength = String.valueOf(writer.getPageNumber()).length();
	        int totalWidth = totalLength * 5;
	        ColumnText.showTextAligned(t, Element.ALIGN_RIGHT,
	                new Phrase(String.valueOf(writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)),
	                totalWidth, 6, 0);
	    }
	}

}

package com.issues.controller;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.issues.dao.RequestDao;
import com.issues.model.Request;
import com.issues.model.StatusType;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

@ManagedBean
@ViewScoped
public class ReportController extends Message1 {

	private Date from = null;
	private Date to = null;

	public void report() {

		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			String fileName = " requestReport.pdf";
			String contentType = "application/pdf";
			ec.responseReset();
			ec.setResponseContentType(contentType);
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

			OutputStream out = ec.getResponseOutputStream();
			Document doc = new Document();
			PdfWriter.getInstance(doc, out);
			LineSeparator ls = new LineSeparator();
			doc.open();

			Paragraph header = new Paragraph();

			Paragraph header1 = new Paragraph(
					"MTN Rwanda,\nP.O.Box 2029 - Kigali,Rwanda\nPhone : (+250) 788 502945"
							+ "\nEmail : mtnrwanda@gmail.com");
			// header.setAlignment(Element.ALIGN_RIGHT);
			header.setAlignment(Image.ALIGN_LEFT + Element.ALIGN_RIGHT);
			header.add(header1);
			doc.add(header);
			doc.add(new Chunk(ls));
			doc.add(new Paragraph("                                          "));

			doc.add(new Paragraph("Date:" + new SimpleDateFormat("dd/MMM/yyyy").format(new Date())));

			Paragraph p = new Paragraph("ALL Customer Request",
					FontFactory.getFont(FontFactory.TIMES_BOLD, 15, Font.BOLD, BaseColor.DARK_GRAY));
			p.setAlignment(Element.ALIGN_CENTER);
			doc.add(p);
			doc.add(new Paragraph("                                          "));
			doc.add(new Paragraph("                                          "));

			PdfPTable table = new PdfPTable(5);
			table.setWidthPercentage(100);
			doc.add(table);
			// BaseColor color = new BaseColor(10, 113, 156);

			Font font0 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, BaseColor.BLACK);

			PdfPCell namesCell = new PdfPCell(new Phrase("Request ID\n\n", font0));

			namesCell.setBorder(Rectangle.BOTTOM);
			table.addCell(namesCell);
			PdfPCell assignedBy = new PdfPCell(new Phrase("Customer Name\n\n", font0));

			assignedBy.setBorder(Rectangle.BOTTOM);
			table.addCell(assignedBy);
			PdfPCell givenDate = new PdfPCell(new Phrase("Request Date\n\n", font0));

			givenDate.setBorder(Rectangle.BOTTOM);
			table.addCell(givenDate);
			PdfPCell quantity = new PdfPCell(new Phrase("Issues\n\n", font0));

			quantity.setBorder(Rectangle.BOTTOM);
			table.addCell(quantity);
			PdfPCell totalCost = new PdfPCell(new Phrase("Request Status\n\n", font0));

			totalCost.setBorder(Rectangle.BOTTOM);
			table.addCell(totalCost);

			for (Request re : new RequestDao().findRequestByDate(from, to)) {
				PdfPCell id = new PdfPCell(new Phrase(re.getId() + ""));
				id.setBorder(Rectangle.NO_BORDER);
				table.addCell(id);

				PdfPCell names = new PdfPCell(
						new Phrase(re.getCustomer().getFirstName() + " " + re.getCustomer().getLastName()));
				names.setBorder(Rectangle.NO_BORDER);
				table.addCell(names);
				PdfPCell requestDate = new PdfPCell(
						new Phrase(new SimpleDateFormat("yyyy/MM/dd").format(re.getRequestDate())));
				requestDate.setBorder(Rectangle.NO_BORDER);
				table.addCell(requestDate);

				PdfPCell issues = new PdfPCell(new Phrase(re.getDescription()));
				issues.setBorder(Rectangle.NO_BORDER);
				table.addCell(issues);
				if (re.getStatus() == StatusType.closed) {
					PdfPCell status = new PdfPCell(new Phrase("Closed"));
					status.setBorder(Rectangle.NO_BORDER);
					table.addCell(status);
				} else {
					PdfPCell status = new PdfPCell(new Phrase("Pending"));
					status.setBorder(Rectangle.NO_BORDER);
					table.addCell(status);
				}

			}

			doc.add(table);
			doc.add(new Paragraph("                                          "));
			doc.add(new Paragraph("                                          "));
			String d = new SimpleDateFormat("dd/MMM/yyyy HH:mm").format(new Date());
			Paragraph printedOn = new Paragraph("Printed On:" + d);
			printedOn.setAlignment(Element.ALIGN_RIGHT);
			doc.add(printedOn);
			doc.close();

			fc.responseComplete();

		} catch (Exception ex) {
			System.err.println("Error:" + ex.getMessage());
			errorMessage("Error:", ex.getMessage());
		}
	}

	public Date getFrom() {
		return from;
	}

	public Date getTo() {
		return to;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public void setTo(Date to) {
		this.to = to;
	}
	
}

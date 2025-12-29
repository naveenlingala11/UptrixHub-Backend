package com.ja.invoice.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ja.invoice.entity.Invoice;
import com.ja.payment.entity.OrderItem;
import com.ja.payment.repository.OrderItemRepository;
import com.ja.user.entity.User;
import com.ja.utils.MoneyUtil;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class InvoicePdfService {

    private final OrderItemRepository orderItemRepository;

    public InvoicePdfService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    private static final Color PRIMARY = new Color(37, 99, 235);
    private static final Color SOFT_BG = new Color(241, 245, 249);
    private static final Color BORDER = new Color(226, 232, 240);
    private static final Color MUTED = new Color(100, 116, 139);

    public byte[] generatePdf(Invoice invoice, User user) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            // 🔥 TIGHT MARGINS FOR SINGLE PAGE
            Document doc = new Document(PageSize.A4, 25, 25, 25, 25);
            PdfWriter writer = PdfWriter.getInstance(doc, out);

            /* ===== WATERMARK ===== */
            writer.setPageEvent(new PdfPageEventHelper() {
                @Override
                public void onEndPage(PdfWriter w, Document d) {
                    Font wm = new Font(Font.HELVETICA, 60, Font.BOLD,
                            new Color(230, 230, 230));
                    ColumnText.showTextAligned(
                            w.getDirectContentUnder(),
                            Element.ALIGN_CENTER,
                            new Phrase("PAID", wm),
                            297, 420, 30
                    );
                }
            });

            doc.open();

            Font h1 = new Font(Font.HELVETICA, 20, Font.BOLD, PRIMARY);
            Font h2 = new Font(Font.HELVETICA, 13, Font.BOLD);
            Font bold = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font normal = new Font(Font.HELVETICA, 10);
            Font muted = new Font(Font.HELVETICA, 9, Font.NORMAL, MUTED);

            /* ================= HEADER ================= */
            doc.add(new Paragraph("Uptrix Hub", h1));
            doc.add(new Paragraph("Learn • Practice • Crack Interviews", muted));
            doc.add(spacer(10));

            /* ================= META ================= */
            PdfPTable meta = new PdfPTable(2);
            meta.setWidthPercentage(100);
            meta.setKeepTogether(true);

            meta.addCell(box("Invoice No", invoice.getInvoiceNumber(), bold, normal));
            meta.addCell(box(
                    "Date",
                    invoice.getGeneratedAt()
                            .atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                    bold, normal
            ));
            meta.addCell(box("Order ID", invoice.getOrderId().toString(), bold, normal));
            meta.addCell(box("Status", "PAID", bold,
                    new Font(Font.HELVETICA, 10, Font.BOLD, new Color(22,163,74))));

            doc.add(meta);
            doc.add(spacer(10));

            /* ================= CUSTOMER ================= */
            PdfPTable customer = new PdfPTable(2);
            customer.setWidthPercentage(100);
            customer.setKeepTogether(true);

            customer.addCell(box("Name", user.getName(), bold, normal));
            customer.addCell(box("Email", user.getEmail(), bold, normal));
            customer.addCell(box("Mobile", user.getMobile() != null ? user.getMobile() : "—", bold, normal));
            customer.addCell(box("Subscription", user.getSubscription().name(), bold, normal));

            doc.add(customer);
            doc.add(spacer(10));

            /* ================= ITEMS ================= */
            PdfPTable items = new PdfPTable(4);
            items.setWidthPercentage(100);
            items.setWidths(new float[]{45, 10, 20, 25});
            items.setKeepTogether(true);

            header(items, "Course");
            header(items, "Qty");
            header(items, "Price");
            header(items, "Amount");

            List<OrderItem> orderItems =
                    orderItemRepository.findByOrderId(invoice.getOrderId());

            for (OrderItem item : orderItems) {
                items.addCell(cell(item.getCourse().getTitle()));
                items.addCell(cell("1", Element.ALIGN_CENTER));
                items.addCell(cell(MoneyUtil.format(item.getCourse().getPrice())));
                items.addCell(cell(MoneyUtil.format(item.getCourse().getPrice())));
            }

            doc.add(items);
            doc.add(spacer(8));

            /* ================= TOTAL ================= */
            PdfPTable totals = new PdfPTable(2);
            totals.setWidthPercentage(45);
            totals.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totals.setKeepTogether(true);

            totals.addCell(total("Subtotal"));
            totals.addCell(value(MoneyUtil.format(invoice.getSubtotal())));
            totals.addCell(total("GST"));
            totals.addCell(value(MoneyUtil.format(invoice.getGst())));
            totals.addCell(total("Total"));
            totals.addCell(valueBold(MoneyUtil.format(invoice.getTotal())));

            doc.add(totals);
            doc.add(spacer(12));

            /* ================= FOOTER ================= */
            PdfPTable footer = new PdfPTable(2);
            footer.setWidthPercentage(100);
            footer.setKeepTogether(true);

            PdfPCell sign = new PdfPCell();
            sign.setBorder(0);
            sign.addElement(new Phrase("Uptrix Hub", bold));
            sign.addElement(new Phrase("Digitally Signed Invoice", muted));

            PdfPCell qr = new PdfPCell();
            qr.setBorder(0);
            qr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            qr.addElement(generateQr(
                    "https://www.uptrixhub.online/invoice/" + invoice.getOrderId()
            ));

            footer.addCell(sign);
            footer.addCell(qr);
            doc.add(footer);

            doc.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }

    /* ================= HELPERS ================= */

    private PdfPCell box(String l, String v, Font lf, Font vf) {
        PdfPCell c = new PdfPCell();
        c.setPadding(12);
        c.setBorderColor(BORDER);
        c.setBackgroundColor(SOFT_BG);
        c.addElement(new Phrase(l, lf));
        c.addElement(new Phrase(v, vf));
        return c;
    }

    private void header(PdfPTable t, String s) {
        PdfPCell c = new PdfPCell(new Phrase(s, new Font(Font.HELVETICA, 11, Font.BOLD)));
        c.setPadding(10);
        c.setBackgroundColor(SOFT_BG);
        t.addCell(c);
    }

    private PdfPCell cell(String t) {
        PdfPCell c = new PdfPCell(new Phrase(t));
        c.setPadding(10);
        return c;
    }

    private PdfPCell cell(String t, int a) {
        PdfPCell c = cell(t);
        c.setHorizontalAlignment(a);
        return c;
    }

    private PdfPCell total(String t) {
        PdfPCell c = new PdfPCell(new Phrase(t, new Font(Font.HELVETICA, 11, Font.BOLD)));
        c.setBorder(Rectangle.NO_BORDER);
        return c;
    }

    private PdfPCell value(String t) {
        PdfPCell c = new PdfPCell(new Phrase(t));
        c.setBorder(Rectangle.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return c;
    }

    private PdfPCell valueBold(String t) {
        PdfPCell c = new PdfPCell(new Phrase(t, new Font(Font.HELVETICA, 13, Font.BOLD)));
        c.setBorder(Rectangle.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return c;
    }

    private Paragraph spacer(int h) {
        Paragraph p = new Paragraph(" ");
        p.setSpacingAfter(h);
        return p;
    }

    private Image generateQr(String text) throws Exception {
        QRCodeWriter w = new QRCodeWriter();
        BitMatrix m = w.encode(text, BarcodeFormat.QR_CODE,100, 100);
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < 100; x++)
            for (int y = 0; y < 100; y++)
                img.setRGB(x, y, m.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ImageIO.write(img, "png", b);
        return Image.getInstance(b.toByteArray());
    }
}

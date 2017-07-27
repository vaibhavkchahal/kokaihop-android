package com.kokaihop.utility;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Vaibhav Chahal on 27/7/17.
 */

public class MyPrintDocumentAdapter extends PrintDocumentAdapter {
    Context context;
    private int pageHeight;
    private int pageWidth;
    public PdfDocument myPdfDocument;
    public int totalpages = 1;
    private List<String> stringList;


    public MyPrintDocumentAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes,
                         PrintAttributes newAttributes,
                         CancellationSignal cancellationSignal,
                         LayoutResultCallback callback,
                         Bundle metadata) {
        myPdfDocument = new PrintedPdfDocument(context, newAttributes);
        pageHeight =
                newAttributes.getMediaSize().getHeightMils() / 1000 * 72;
        pageWidth =
                newAttributes.getMediaSize().getWidthMils() / 1000 * 72;
        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }
        if (totalpages > 0) {
            PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                    .Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(totalpages);
            PrintDocumentInfo info = builder.build();
            callback.onLayoutFinished(info, true);
        } else {
            callback.onLayoutFailed("Page count is zero.");
        }
    }


    @Override
    public void onWrite(final PageRange[] pageRanges,
                        final ParcelFileDescriptor destination,
                        final CancellationSignal cancellationSignal,
                        final WriteResultCallback callback) {
        for (int i = 0; i < totalpages; i++) {
            if (pageInRange(pageRanges, i)) {
                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,
                        pageHeight, i).create();
                PdfDocument.Page page =
                        myPdfDocument.startPage(newPage);
                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    myPdfDocument.close();
                    myPdfDocument = null;
                    return;
                }
                drawPage(page, i);
                myPdfDocument.finishPage(page);

            }
        }
        try {
            myPdfDocument.writeTo(new FileOutputStream(
                    destination.getFileDescriptor()));

      /*   String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cookbook";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);


            File file = new File(dir, "recipe.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            myPdfDocument.writeTo(fOut);

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(myPdfDocument);
            bw.close();*/
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            myPdfDocument.close();
            myPdfDocument = null;
        }
        callback.onWriteFinished(pageRanges);

    }

    private boolean pageInRange(PageRange[] pageRanges, int page) {
        for (int i = 0; i < pageRanges.length; i++) {
            if ((page >= pageRanges[i].getStart()) &&
                    (page <= pageRanges[i].getEnd()))
                return true;
        }
        return false;
    }

    private void drawPage(PdfDocument.Page page,
                          int pagenumber) {
        Canvas canvas = page.getCanvas();
        pagenumber++; //
        int titleBaseLine = 90;
        int leftMargin = 50;

        /*Drawable icon = getResources().getDrawable(R.drawable.pot);
        icon.setBounds(leftMargin - 40, 40, 60, 80);
        icon.getIntrinsicHeight();
        icon.getIntrinsicWidth();

        icon.draw(canvas);*/
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
//        Typeface fontRegular = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
//        paint.setTypeface(fontRegular);
        canvas.drawText("PDF SAMPLE", leftMargin, titleBaseLine, paint);
        paint.setTextSize(14);
        canvas.drawText("PDF LINE NO 1", leftMargin, titleBaseLine + 30, paint);
        paint.setTextSize(12);
        canvas.drawText("PDF LINE NO 1", leftMargin, titleBaseLine + 50, paint);
        paint.setTextSize(14);
        canvas.drawText("PDF LINE NO 1", leftMargin, titleBaseLine + 80, paint);
        paint.setTextSize(12);
        canvas.drawText("PDF LINE NO 1", leftMargin, titleBaseLine + 100, paint);
    }
}

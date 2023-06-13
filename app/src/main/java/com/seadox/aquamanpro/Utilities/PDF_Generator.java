package com.seadox.aquamanpro.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.seadox.aquamanpro.Models.Drill;
import com.seadox.aquamanpro.Models.DrillList;
import com.seadox.aquamanpro.R;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PDF_Generator {
    private final String FILE_FORMAT = ".pdf";
    private final String DIRACTION = "/Download";
    private final String SAVE_SUCCESS = "PDF file generated successfully";
    private final String FILE_NAME = "AquamanPro_";
    private DrillList drillList;
    private Context context;
    private PdfDocument pdfDocument;
    int pageHeight = 1120, pagewidth = 792;

    public PDF_Generator(Context context, DrillList drillList) {
        this.drillList = drillList;
        this.context = context;

        pdfDocument = new PdfDocument();
    }

    public void generatePDF() {
        Paint paint = new Paint();
        Paint title = new Paint();

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(28);
        title.setColor(ContextCompat.getColor(context, R.color.black));

        Paint drillText = new Paint();
        drillText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        drillText.setTextSize(24);
        drillText.setColor(ContextCompat.getColor(context, R.color.black));

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Bitmap bmpFins = BitmapFactory.decodeResource(context.getResources(), R.drawable.fins);
        Bitmap bmpBoard = BitmapFactory.decodeResource(context.getResources(), R.drawable.kikboard);
        Bitmap bmpPaddles = BitmapFactory.decodeResource(context.getResources(), R.drawable.paddles);
        Bitmap bmpPully = BitmapFactory.decodeResource(context.getResources(), R.drawable.pull_buoy);

        Bitmap scaled[] = {
                Bitmap.createScaledBitmap(bmpFins, 32, 32, false),
                Bitmap.createScaledBitmap(bmpBoard, 32, 32, false),
                Bitmap.createScaledBitmap(bmpPaddles, 32, 32, false),
                Bitmap.createScaledBitmap(bmpPully, 32, 32, false)
        };

        int line = 100;
        canvas.drawText("Warmup:", 50, 50, title);
        for (Drill drill : drillList.getWarmup()) {
            page = writeDrill(drill, line, pdfDocument, page, pageInfo, canvas, drillText);
            addEquipment(canvas, paint, scaled, line, drill.isFins(), drill.isKikboard(), drill.isPaddles(), drill.isPullBuoy());
            line += 40;
        }

        canvas.drawText("Main:", 50, line + 50, title);
        line += 100;
        for (Drill drill : drillList.getMain()) {
            page = writeDrill(drill, line, pdfDocument, page, pageInfo, canvas, drillText);
            addEquipment(canvas, paint, scaled, line, drill.isFins(), drill.isKikboard(), drill.isPaddles(), drill.isPullBuoy());
            line += 40;
        }

        canvas.drawText("Cool Down:", 50, line + 50, title);
        line += 100;
        for (Drill drill : drillList.getWarmdown()) {
            page = writeDrill(drill, line, pdfDocument, page, pageInfo, canvas, drillText);
            addEquipment(canvas, paint, scaled, line, drill.isFins(), drill.isKikboard(), drill.isPaddles(), drill.isPullBuoy());
            line += 40;
        }

        pdfDocument.finishPage(page);

        saveFile(FILE_NAME + System.currentTimeMillis());

        pdfDocument.close();
    }

    private PdfDocument.Page writeDrill(Drill drill, int line, PdfDocument pdfDocument, PdfDocument.Page page, PdfDocument.PageInfo ageInfo, Canvas canvas, Paint drillText) {
        if (line + 40 >= pageHeight) {
            line = 100;
            pdfDocument.finishPage(page);
            page = pdfDocument.startPage(ageInfo);
            canvas = page.getCanvas();
        }
        String format = drill.getRounds() + " X " + drill.getDistance() + " " + drill.getStroke() + " @ " + drill.getTime();
        canvas.drawText(format, 80, line, drillText);

        writeCircle(drill, line, canvas);
        return page;
    }

    private void writeCircle(Drill drill, int line, Canvas canvas) {
        Paint paint = new Paint();
        int color;

        switch (drill.getColor()) {
            case 1:
                color = ContextCompat.getColor(context, R.color.workout_data_item_yellow);
                break;
            case 2:
                color = ContextCompat.getColor(context, R.color.workout_data_item_green);
                break;
            case 0:
            default:
                color = ContextCompat.getColor(context, R.color.workout_data_item_red);
                break;
        }
        paint.setColor(color);

        canvas.drawCircle(60, line - 7.5f, 12, paint);
    }


    private void addEquipment(Canvas canvas, Paint paint, Bitmap scaled[], int line, boolean fins, boolean board, boolean paddles, boolean pull_bouy) {
        if (fins)
            canvas.drawBitmap(scaled[0], 450, line - 20, paint);
        if (board)
            canvas.drawBitmap(scaled[1], 480, line - 20, paint);
        if (paddles)
            canvas.drawBitmap(scaled[2], 510, line - 20, paint);
        if (pull_bouy)
            canvas.drawBitmap(scaled[3], 540, line - 20, paint);
    }

    private void saveFile(String fileName) {
        File dir = new File(Environment.getExternalStorageDirectory(), DIRACTION);

        File file = new File(dir, fileName + FILE_FORMAT);

        try {
            pdfDocument.writeTo(Files.newOutputStream(file.toPath()));
            SignalGenerator.getInstance().toast(SAVE_SUCCESS, Toast.LENGTH_SHORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

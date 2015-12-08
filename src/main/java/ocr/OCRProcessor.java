package ocr;

/**
 * Created by Iuliia on 03.12.2015.
 */
public interface OCRProcessor {
    String executeTessaract();

    String[] parse(String outPath);
}

package ocr;

import java.io.IOException;

/**
 * Created by Iuliia on 03.12.2015.
 */
public class TesseractExecutorImpl implements TesseractExecutor {

    private final String pathtoTesseractEXE = "\\WEB-INF\\lib\\Tesseract-OCR\\tesseract.exe";
    private String testImg = "test_imgs\\DSC_0088.JPG";
    private String outPut = "test_imgs\\output";

    private String language = "-l eng";

    @Override
    public void execute() {

        String[] cmd = {pathtoTesseractEXE, testImg, outPut, language};
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

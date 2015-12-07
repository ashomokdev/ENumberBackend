package ocr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Iuliia on 03.12.2015.
 */
public class TesseractExecutorImpl implements TesseractExecutor {

    private final static Logger log = LoggerFactory.getLogger(TesseractExecutorImpl.class);
    private static final String pathtoTesseractEXE = "\\WEB-INF\\lib\\Tesseract-OCR\\tesseract.exe";
    private static final String language = "-l eng";
    private String testImg;
    private String outPut;
    private ServletContext context;

    public TesseractExecutorImpl(ServletContext context, String imgPath, String outputPath) {
        this.context = context;
        testImg = imgPath;
        outPut = outputPath;
    }


    @Override
    public void execute() {

        String fullPath = context.getRealPath(pathtoTesseractEXE);
        log.info("pathtoTesseractEXE obtained: {}", fullPath);

//        File file = new File(this.getClass().getClassLoader().getResource(".").getPath());
//
//        String osrFilePath = file.getParentFile()
//                .getAbsolutePath()
//                .concat("\\enumbservice-0.2.0\\WEB-INF\\lib\\Tesseract-OCR\\tesseract.exe");
//
//        File osrFile = new File(osrFilePath);
//
//        log.info("{}, {}", osrFile.getAbsoluteFile(), osrFile.exists());
//
//        if (osrFile.exists())
//            try {
//                Process ocrProcess = Runtime.getRuntime().exec(osrFilePath);
//                log.info(" Is OCR started {}", ocrProcess.isAlive());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//        if (!new File("WEB-INF").exists())
//        {
//            try {
//                throw new FileNotFoundException("Yikes!");
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }

/*        String[] cmd = {osrFilePath, testImg, outPut, language};
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}

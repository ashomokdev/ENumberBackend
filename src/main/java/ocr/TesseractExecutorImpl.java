package ocr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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
    HttpServletRequest request;

    public TesseractExecutorImpl(ServletContext context, HttpServletRequest request, String imgPath, String outputPath) {
        this.context = context;
        this.request = request;
        testImg = imgPath;
        outPut = outputPath;
    }


    @Override
    public void execute() {
        String fullPath;

//        String contextPath = request.getContextPath();
//        log.info("contextPath = {}", contextPath);
//
//        String pathToTesseract = context.getRealPath(pathtoTesseractEXE);
//        log.info("pathToTesseract = {}", pathToTesseract);
//
//        String pathToWEBINF = context.getRealPath("WEB-INF");
//        log.info("pathToWEB-INF = {}", pathToWEBINF);

        String realContextPath = context.getRealPath(request.getContextPath());
        if (realContextPath == null) //True in Debug mode (not distributed version)
        {
            realContextPath = context.getRealPath(request.getContextPath());
        } else {
            //Delete dublicate context folder from path.
            //Example of input D:\Programs_Files\apache-tomcat-8.0.27\webapps\enumbservice-0.2.0\enumbservice-0.2.0
            //enumbservice-0.2.0\enumbservice-0.2.0\ is duplicated part
            realContextPath = new File(realContextPath).getParent();
        }
        log.info("realContextPath = {}", realContextPath); //C:\Users\Iuliia\IdeaProjects\ENumbersBackend\src\main\webapp\

        fullPath = realContextPath.concat(pathtoTesseractEXE);
        log.info("pathtoTesseractEXE obtained: {}", fullPath);


        Process ocrProcess = null;
        try {
            ocrProcess = Runtime.getRuntime().exec(fullPath);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("ERROR: {}", e.getMessage());
        }
        log.info(" Is OCR started {}", ocrProcess.isAlive());

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

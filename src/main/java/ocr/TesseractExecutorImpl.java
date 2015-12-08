package ocr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

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

    public TesseractExecutorImpl(HttpServletRequest request, String imgPath, String outputPath) {
        this.request = request;
        this.context = request.getServletContext();
        testImg = imgPath;
        outPut = outputPath;
    }


    @Override
    public String execute() {

        String realContextPath = context.getRealPath(request.getContextPath());
        if (! request.getContextPath().isEmpty()) //distributed version
        {
            //Delete dublicate context folder from path.
            //Example of input D:\Programs_Files\apache-tomcat-8.0.27\webapps\enumbservice-0.2.0\enumbservice-0.2.0
            //enumbservice-0.2.0\enumbservice-0.2.0\ is duplicated part
            realContextPath = new File(realContextPath).getParent();
        }
        log.info("realContextPath = {}", realContextPath); //C:\Users\Iuliia\IdeaProjects\ENumbersBackend\src\main\webapp\

        String fullPathTesseractExecutor = realContextPath.concat(pathtoTesseractEXE);
        log.info("pathtoTesseractEXE obtained: {}", fullPathTesseractExecutor);


        String[] cmd = {fullPathTesseractExecutor, testImg, outPut, language};
        Process ocrProcess = null;
        try {
            ocrProcess = Runtime.getRuntime().exec(cmd);
            ocrProcess.waitFor();

            return outPut + ".txt";
        } catch (IOException e) {
            log.error("IOException {}", e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            log.error("InterruptedException {}", e.getMessage());
            e.printStackTrace();
        }
        return "";
    }
}

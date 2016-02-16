package ocr;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Iuliia on 03.12.2015.
 */
public class OCRProcessorImpl implements OCRProcessor {

    private final static Logger log = LoggerFactory.getLogger(OCRProcessorImpl.class);
    private static final String pathtoTesseractEXE = "\\WEB-INF\\lib\\Tesseract-OCR\\tesseract.exe";
    private static final String language = "-l eng";
    private ServletContext context;
    private String realContextPath;

    private final String tessdataPath = File.separator + "WEB-INF" + File.separator + "lib";

 //   private final String tessdataPathWindows = "\\WEB-INF\\lib\\"; //for windows
//    private final String tessdataPathLinux = "/WEB-INF/lib/"; //for linux

    private static final String REGEX_ENUMB = "E[ ]{0,2}[0-9]{3,4}[a-j]{0,1}";

    public OCRProcessorImpl(HttpServletRequest request) {
        this.context = request.getServletContext();

        realContextPath = context.getRealPath(request.getContextPath());
        if (! request.getContextPath().isEmpty()) //distributed version
        {
            //Delete dublicate context folder from path.
            //Example of input D:\Programs_Files\apache-tomcat-8.0.27\webapps\enumbservice-0.2.0\enumbservice-0.2.0
            //enumbservice-0.2.0\enumbservice-0.2.0\ is duplicated part
            realContextPath = new File(realContextPath).getParent();
        }
        log.info("realContextPath = {}", realContextPath); //C:\Users\Iuliia\IdeaProjects\ENumbersBackend\src\main\webapp\
    }


    @Override
    public String executeTessaractEXE(String imgPath, String outputPath) {

            String fullPathTesseractExecutor = realContextPath.concat(pathtoTesseractEXE);
            log.info("pathtoTesseractEXE obtained: {}", fullPathTesseractExecutor);

            String[] cmd = {fullPathTesseractExecutor, imgPath, outputPath, language};
            Process ocrProcess = null;
            try {
                ocrProcess = Runtime.getRuntime().exec(cmd);
                ocrProcess.waitFor();

                return outputPath + ".txt";
            } catch (IOException e) {
                log.error("IOException {}", e.getMessage());
                e.printStackTrace();
            } catch (InterruptedException e) {
                log.error("InterruptedException {}", e.getMessage());
                e.printStackTrace();
            } finally {
                //delete uploaded image
                new File(imgPath).delete();
            }
            return "";

    }

    @Override
    public String[] parseTXT(String path) {
        File pathfile = new File(path);
        String[] result = new String[0];
        try {
            String content = FileUtils.readFileToString(pathfile, "UTF-8");
            result = parseResult(content);
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            //delete .txt with results
           pathfile.delete();
        }
        return result;
    }

    @Override
    public String[] doOCR(File imageFile) throws Exception {
        //33189561455 13391156620
        //14936928704 10229086919
        //16887334562

        if (! imageFile.canRead())
        {
            throw new Exception("Image file not exist or can not be readen");
        }

        long startTime = System.nanoTime();

         //ITesseract instance = new Tesseract(); // JNA Interface Mapping
        ITesseract instance = new Tesseract1(); // JNA Direct Mapping

        instance.setTessVariable("LC_NUMERIC", "C"); //for linux

        String fullTessdataPath = realContextPath.concat(tessdataPath);
        instance.setDatapath(fullTessdataPath);

        try {

            String result = instance.doOCR(imageFile);

            long endTime = System.nanoTime();
            long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
            System.out.println( "doOCR: " + duration + "/n");

            return parseResult(result);
        }
        catch (TesseractException e) {
            System.err.println(e.getMessage());
        }

        return new String[0];
    }


    private String[] parseResult(String input) {
        final String E = "E";
        final int lengthOfWord = 8;

        if (input.contains(E)) {
            //get possible E-numbers

            Set<String> words =  new HashSet<String>();

            int fromIndex = 0;
            while (fromIndex < input.length()) {

                int wordStart = input.indexOf(E, fromIndex);
                if (wordStart >= 0) { //if E - numbers exist

                    int wordEnd = input.indexOf(E, fromIndex) + lengthOfWord;
                    if (wordEnd > input.length() - 1) {
                        wordEnd = input.length();
                    }
                    String word = input.substring(wordStart, wordEnd);

                    String result = parseWord(word);
                    if (result != null) {
                        words.add(result);
                    }
                    fromIndex = wordStart + 1;
                }
                else
                {
                    fromIndex = input.length();
                }
            }
            return words.toArray(new String[words.size()]);
        } else {
            return new String[0];
        }
    }

    private String parseWord(String word) {
        Pattern pattern = Pattern.compile(REGEX_ENUMB);
        Matcher matcher = pattern.matcher(word);
        if (matcher.find()) {
            String result = matcher.group(0).replaceAll("\\s", "");
            return result;
        } else {
            return null;
        }
    }
}

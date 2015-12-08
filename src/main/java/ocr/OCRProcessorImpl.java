package ocr;

import org.apache.commons.io.FileUtils;
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
    private String imgPath;
    private String outPut;
    private ServletContext context;
    private HttpServletRequest request;


    private static final String REGEX_ENUMB = "E[ ]{0,2}[0-9]{3,4}[a-j]{0,1}";

    public OCRProcessorImpl(HttpServletRequest request, String imgPath, String outputPath) {
        this.request = request;
        this.context = request.getServletContext();
        this.imgPath = imgPath;
        outPut = outputPath;
    }


    @Override
    public String executeTessaract() {

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


        String[] cmd = {fullPathTesseractExecutor, imgPath, outPut, language};
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
        finally {
            //delete uploaded image
            new File(imgPath).delete();
        }
        return "";
    }

    @Override
    public String[] parse(String path) {
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

package ocr;

import java.io.File;

/**
 * Created by Iuliia on 03.12.2015.
 */
public interface OCRProcessor {

    /**
     *
     * @return path to txt with result
     */
    @Deprecated
    String executeTessaractEXE(String imgPath, String outputPath);


    /**
     *
     * @param outPath path to txt with result
     * @return array of e-numbers
     */
    @Deprecated
    String[] parseTXT(String outPath);

    /**
     *
     * @param imageFile
     * @return array of e-numbers
     */
    String[] doOCR(File imageFile) throws Exception;
}

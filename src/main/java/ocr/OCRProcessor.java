package ocr;

import java.io.File;

/**
 * Created by Iuliia on 03.12.2015.
 */
public interface OCRProcessor {


    /**
     *
     * @param imageFile
     * @return array of e-numbers
     */
    String[] doOCR(File imageFile) throws Exception;
}

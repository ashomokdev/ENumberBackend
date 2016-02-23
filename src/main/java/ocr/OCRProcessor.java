package ocr;

import java.awt.image.BufferedImage;
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
    @Deprecated
    String[] doOCR(File imageFile) throws Exception;


    /**
     *
     * @param image
     * @return array of e-numbers
     */
    String[] doOCR(BufferedImage image);
}

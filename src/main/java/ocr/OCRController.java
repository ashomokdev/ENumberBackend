package ocr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * Created by Iuliia on 02.12.2015.
 */
@RestController
public class OCRController {

    private final static Logger log = LoggerFactory.getLogger(OCRController.class);

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public
    @ResponseBody
    String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String uploadsDir = "/uploads/";
                String realPathtoUploads = request.getServletContext().getRealPath(uploadsDir);
                CreatePathIfNeeded(realPathtoUploads);
                log.info("realPathtoUploads = {}", realPathtoUploads);


                String orgName = file.getOriginalFilename();
                String filePath = realPathtoUploads + orgName;
                File dest = new File(filePath);
                file.transferTo(dest);

                String outPath;
                String[] jsonResult = new String[0];
                if (dest.exists()) {

                    String out = filePath;
                    OCRProcessor processor = new OCRProcessorImpl(request, filePath, out); //.txt sufix will be added to out.
                    outPath = processor.executeTessaract();
                    if (!new File(outPath).exists()) {
                        log.error("Result from OCR was not obtained, file {} not found", outPath);
                    } else {
                        log.info("Result from OCR obtained.");
                        jsonResult = processor.parse(outPath);
                    }
                } else {
                    log.error("image not uploaded, file {} not found", filePath);
                }

                // String json = "Recognized result: " + outPath;//convert entity to json
                return new ResponseEntity(jsonResult, HttpStatus.ACCEPTED);

            } catch (Exception e) {
                return new ResponseEntity("You failed to upload file." + e.getMessage(), HttpStatus.BAD_REQUEST);

            }
        } else {
            return new ResponseEntity("You failed to upload file because the file was empty.", HttpStatus.BAD_REQUEST);
        }
    }

    private void CreatePathIfNeeded(String path) throws IOException {
        File dir = new File(path);
        if (!dir.exists()) {
            boolean iscreated = dir.mkdir();
            if (iscreated) {
                return;
            } else {
                throw new IOException("Directory " + path + " can not be created");
            }
        }
    }
}
package ocr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Iuliia on 02.12.2015.
 */
@RestController
public class GreetingController {

    private final static Logger log = LoggerFactory.getLogger(GreetingController.class);

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
                String realPathtoUploads =  request.getServletContext().getRealPath(uploadsDir);
                CreatePathIfNeeded(realPathtoUploads);
                log.info("realPathtoUploads = {}", realPathtoUploads);


                String orgName = file.getOriginalFilename();
                String filePath = realPathtoUploads + orgName;
                File dest = new File(filePath);
                file.transferTo(dest);

                String outPath = "empty";
                if (dest.exists()) {

                    String out = filePath;
                    TesseractExecutor executor = new TesseractExecutorImpl(request, filePath, out);
                    outPath = executor.execute();
                    if (! new File(outPath).exists())
                    {
                        log.error("Result from OCR was not obtained, file {} not found", outPath);
                    }
                    else
                    {
                        log.info("Result from OCR obtained.");
                        // //TODO recognize, parse, delete
                    }
                }
                else
                {
                    log.error("image not uploaded, file {} not found", filePath);
                }



//                String realImgPath = request.getServletContext().getRealPath(filePath);
//                log.info("realImgPath = {}", realImgPath);
//
//                File img = new File(realImgPath);
//                if(!img.exists())
//                {
//                    log.info("NOT EXIST! realImgPath = {}", realImgPath);
//                }





//                byte[] bytes = file.getBytes();
//                BufferedOutputStream stream =
//                        new BufferedOutputStream(new FileOutputStream(new File(name)));
//                stream.write(bytes);
//                stream.close();

                String json = "Recognized result: " + outPath;//convert entity to json
                return new ResponseEntity(json, HttpStatus.ACCEPTED);

            } catch (Exception e) {
                return new ResponseEntity("You failed to upload file." + e.getMessage(), HttpStatus.BAD_REQUEST);

            }
        } else {
            return new ResponseEntity("You failed to upload file because the file was empty.", HttpStatus.BAD_REQUEST);
        }
    }

    private void CreatePathIfNeeded(String path) throws IOException {
        File dir = new File(path);
        if(! dir.exists())
        {
           boolean iscreated= dir.mkdir();
            if (iscreated)
            {
                return;
            }
            else
            {
                throw new IOException("Directory "+ path+" can not be created");
            }
        }
    }

//    private ServletContext getContext() {
//
//
//    }


//
//    @RequestMapping(value = "/show", method = RequestMethod.GET)
//    public String displayForm() {
//        return "file_upload_form";
//    }
//
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    public String save(
//            @ModelAttribute("uploadForm") FileUploadForm uploadForm,
//            Model map) {
//
//        List<MultipartFile> files = uploadForm.getFiles();
//
//        List<String> fileNames = new ArrayList<String>();
//
//        if(null != files && files.size() > 0) {
//            for (MultipartFile multipartFile : files) {
//
//                String fileName = multipartFile.getOriginalFilename();
//                fileNames.add(fileName);
//                //Handle file content - multipartFile.getInputStream()
//
//            }
//        }
//
//        map.addAttribute("files", fileNames);
//        return "file_upload_success";
//    }
}
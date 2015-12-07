package ocr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletContext;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Iuliia on 02.12.2015.
 */
@RestController
public class GreetingController {

    public ServletContext getServletContext() {
        return servletContext;
    }

    @Autowired
    private ServletContext servletContext;

//    private static final String template = "Hello, %s!";
//    private final AtomicLong counter = new AtomicLong();
//
//    @RequestMapping("/greeting")
//    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
//        TesseractExecutor executor = new TesseractExecutorImpl();
//        executor.execute();
//        return new Greeting(counter.incrementAndGet(),
//                String.format(template, name));
//    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public
    @ResponseBody
    String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity handleFileUpload(@RequestParam("name") String name,
                                    @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
               //TODO create /uploads folder before
                String orgName = file.getOriginalFilename();
                String filePath = "/" + orgName;
                File dest = new File(filePath);
                file.transferTo(dest);

                ServletContext context = getServletContext();

                TesseractExecutor executor = new TesseractExecutorImpl(context, filePath, filePath+".txt");
                executor.execute();


//                byte[] bytes = file.getBytes();
//                BufferedOutputStream stream =
//                        new BufferedOutputStream(new FileOutputStream(new File(name)));
//                stream.write(bytes);
//                stream.close();

                String json = "You successfully uploaded file!" ;//convert entity to json
                return new ResponseEntity(json, HttpStatus.ACCEPTED);

            } catch (Exception e) {
                return new ResponseEntity("You failed to upload file." + e.getMessage(), HttpStatus.BAD_REQUEST);

            }
        } else {
            return new ResponseEntity("You failed to upload file because the file was empty.", HttpStatus.BAD_REQUEST);
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
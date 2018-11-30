package com.sislamoglu.ppmtool.web;

import com.sislamoglu.ppmtool.repositories.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/fileUpload/")
public class FileUploadController {

    @Autowired
    private StorageService storageRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable("filename") String filename){
        Resource file = storageRepository.loadAsResource(filename);
        return ResponseEntity.ok(file);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/")
    public void handleFileUpload(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes){
        storageRepository.store(file);
    }
}

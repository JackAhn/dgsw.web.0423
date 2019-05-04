package kr.hs.dgsw.web01blog.Controller;

import kr.hs.dgsw.web01blog.Domain.Attachment;
import kr.hs.dgsw.web01blog.Domain.User;
import kr.hs.dgsw.web01blog.Protocol.ResponseFormat;
import kr.hs.dgsw.web01blog.Protocol.ResponseType;
import kr.hs.dgsw.web01blog.Repository.AttachmentRepository;
import kr.hs.dgsw.web01blog.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
public class AttachmentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @PostMapping("/attachment/{type}/{id}")
    public ResponseFormat upload(@RequestPart MultipartFile srcFile, @PathVariable String type, @PathVariable Object id){
        String destFilename = "D:\\Spring_2\\web01blog\\upload\\"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd\\"))
                + UUID.randomUUID().toString() + "_"
                + srcFile.getOriginalFilename();
        try{
            File file = new File(destFilename);
            file.getParentFile().mkdirs();
            srcFile.transferTo(file);
            if(type.equals("post")){
                Attachment attachment = new Attachment(destFilename, (Long)id);
                this.attachmentRepository.save(attachment);
                return new ResponseFormat(ResponseType.ATTACHMENT_STORED, attachment);
            }
            else if(type.equals("user")){
                User u = this.userRepository.findByAccount((String)id)
                        .map(found->{
                            found.setProfilePath(destFilename);
                            return this.userRepository.save(found);
                        })
                        .orElse(null);
                return new ResponseFormat(ResponseType.ATTACHMENT_STORED, u);
            }
            else
                return new ResponseFormat(ResponseType.FAIL, null);

        }catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseFormat(ResponseType.FAIL, null);
        }
    }

    @GetMapping("/attachment/{type}/{id}")
    public void download(HttpServletRequest request , HttpServletResponse response, @PathVariable String type, @PathVariable Object id){
        String filepath = null;
        Attachment a = null;
        User u = null;
        File file = null;

        if(type.equals("post")){
            a = this.attachmentRepository.findById((Long)id).get();
            if(a.getStoredPath() == null)
                return;
            filepath = a.getStoredPath();
        }
        else if(type.equals("user")){
            u = this.userRepository.findByAccount((String)id).get();
            if(u.getProfilePath() == null)
                return;
            filepath = u.getProfilePath();
        }
        file = new File(filepath);
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null)
            mimeType = "application/octet-stream";

        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        response.setContentLength((int) file.length());
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(is, response.getOutputStream());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}


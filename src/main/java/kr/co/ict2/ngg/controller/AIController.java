package kr.co.ict2.ngg.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.ict2.ngg.service.AIService;
import kr.co.ict2.ngg.vo.AIResVO;

@RestController
@RequestMapping("/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    private final static String filePath = Paths.get("").toAbsolutePath() + "/front/public/img";

    @GetMapping("/hello")
    public String getMethodName() {
        return "HelloWorld";
    }

    @PostMapping("/saveTest")
    public ResponseEntity<Integer> saveTest(@RequestBody AIResVO vo) {
        int rnum = aiService.saveTest(vo);
        return ResponseEntity.ok(rnum);
    }

    @PostMapping("/updateHistory")
    public void updateHistory(@RequestBody AIResVO vo) {
        aiService.updateHistory(vo);
    }

    @PostMapping("/croppedImgSave")
    public ResponseEntity<?> croppedImgSave(@RequestParam("croppedImg") MultipartFile mf,
            HttpServletRequest request) {
        if (mf == null || mf.isEmpty()) {
            throw new IllegalArgumentException("파일이 전송되지 않았습니다.");
        }
        String oriFn = mf.getOriginalFilename();
        StringBuffer path = new StringBuffer();
        path.append(filePath).append("/before_image/");
        path.append(oriFn);
        System.out.println("FullPath :" + path);
        File f = new File(path.toString());
        try {
            mf.transferTo(f);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(1);
    }

    @PostMapping("/after_imageSave")
    public ResponseEntity<?> after_imageSave(@RequestParam("imgfile") MultipartFile mf,
            HttpServletRequest request) {
        System.out.println("after_imageSave왔니?");
        if (mf == null || mf.isEmpty()) {
            throw new IllegalArgumentException("파일이 전송되지 않았습니다.");
        }
        String oriFn = mf.getOriginalFilename();
        StringBuffer path = new StringBuffer();
        path.append(filePath).append("/after_image/");
        path.append(oriFn);
        System.out.println("FullPath :" + path);
        File f = new File(path.toString());
        try {
            mf.transferTo(f);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(oriFn);
    }

}

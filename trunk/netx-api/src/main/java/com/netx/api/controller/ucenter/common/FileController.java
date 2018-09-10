package com.netx.api.controller.ucenter.common;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
/**
 * Create by wongloong on 17-8-21
 */
@Api(description = "文件上传下载api")
@RestController
@RequestMapping("/api/file")
public class FileController {
   /* private Logger logger = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private FileService fileService;

    @ApiOperation(value = "上传文件",notes = "使用ajax上传图片,返回result中有文件名,下载时用此文件名下载文件")
    @PostMapping("/upload")
    public Result uploadFile(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        String ids = "";
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
                    ids += fileService.uploadFile(file.getInputStream(), fileType) + ",";
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    return Result.newFailure(ErrorCode.ERROR_FILE_UPLOAD);
                }
            }
        }
        return Result.newSuccess(ids.substring(0, ids.length() - 1));
    }

    @ApiOperation(value = "上传文件",notes = "使用ajax上传图片,返回result中有文件名,下载时用此文件名下载文件")
    @PostMapping("/uploadFileOne")
    public Result uploadFileOne(HttpServletRequest request,@RequestParam("file") MultipartFile file) {
        BufferedOutputStream stream = null;
        String ids = "";
        if (!file.isEmpty()) {
            try {
                String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
                ids += fileService.uploadFile(file.getInputStream(), fileType) + ",";
            } catch (IOException e) {
                logger.error(e.getMessage());
                return Result.newFailure(ErrorCode.ERROR_FILE_UPLOAD);
            }
        }
        return Result.newSuccess(ids.substring(0, ids.length() - 1));
    }

    @ApiOperation(value = "下载文件")
    @GetMapping("/download")
    public void downloadFile(String fileName, HttpServletResponse response) {
        fileService.downloadFile(fileName, response);
    }*/
}

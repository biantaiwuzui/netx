package com.netx.utils.picture;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.io.Files;
import com.netx.utils.sign.Base64;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public class PictureValidateUtil {
    private static BiMap<String, String> contentTypeMap = HashBiMap.create();

    static {
        contentTypeMap.put(".jpg", "image/jpg");
        contentTypeMap.put(".gif", "image/gif");
        contentTypeMap.put(".png", "image/png");
        contentTypeMap.put(".jpeg", "image/jpeg");
        contentTypeMap.put(".bmp", "application/x-bmp");
    }

    /**
     * 检查是否是图片
     *
     * @param name
     * @return
     */
    public static boolean isPicture(String name) {
        name = name.toLowerCase();
        for(String type : contentTypeMap.keySet()) {
            if(name.toLowerCase().endsWith(type))
                return true;
        }
        return false;
    }

    public static String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String getImageContentType(String name) {
        String fileType = getFileType(name).toLowerCase();
        return contentTypeMap.get(fileType);
    }

    public static MultipartFile uploadBase64PictureParse(String fileName, String base64) {
        if(base64 == null || base64.length() < 6 || !base64.startsWith("data:")) {
            throw new RuntimeException("图片格式错误");
        }
        int mimeIndexOf = base64.indexOf(';');
        if(mimeIndexOf < 6) {
            throw new RuntimeException("图片格式错误");
        }
        int base64InfoStartIndex = mimeIndexOf + 1;
        int base64InfoEndIndex = base64InfoStartIndex + 6;
        if(base64.length() < base64InfoEndIndex) {
            throw new RuntimeException("图片格式错误");
        }

        String base64Info = base64.substring(base64InfoStartIndex, base64InfoEndIndex);
        if(!"base64".equalsIgnoreCase(base64Info)) {
            throw new RuntimeException("图片格式错误");
        }

        String mime = base64.substring(5, mimeIndexOf);
        BiMap<String, String> mimeMap = contentTypeMap.inverse();
        String suffix = mimeMap.get(mime);
        if(suffix == null) {
            throw new RuntimeException("图片格式错误");
        }

        String realBase64 = base64.substring(base64InfoEndIndex + 1);

        byte[] decode = Base64.decode(realBase64);

        return new SimpleMultipartFile(fileName + suffix, decode, mime);
    }

    private static class SimpleMultipartFile implements MultipartFile {
        private final String fileName;
        private final byte[] content;
        private final String contentType;

        public SimpleMultipartFile(String fileName, byte[] content, String contentType) {
            this.fileName = fileName;
            this.content = content;
            this.contentType = contentType;
        }

        @Override
        public String getName() {
            return fileName;
        }

        @Override
        public String getOriginalFilename() {
            return fileName;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content == null || content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return content;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            Files.write(content, dest);
        }
    }
}

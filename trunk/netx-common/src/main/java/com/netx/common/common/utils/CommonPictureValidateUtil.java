package com.netx.common.common.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Created by CloudZou on 1/10/2018.
 */
public class CommonPictureValidateUtil {
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


}
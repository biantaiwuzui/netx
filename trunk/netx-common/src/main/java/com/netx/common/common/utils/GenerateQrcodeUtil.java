package com.netx.common.common.utils;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created By wj.liu
 * Description: 二维码生成工具
 * Date: 2017-11-08
 */
public class GenerateQrcodeUtil {

    /**
     * 根据内容，生成指定宽高、指定格式的二维码图片文件
     * @param text   内容
     * @param width  宽
     * @param height 高
     * @param format 图片格式
     * @return 生成的二维码图片路径
     * @throws Exception
     */
    public static String generateQRCodeFile(String text, int width, int height, String format) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        String pathName = "qrcode"+System.currentTimeMillis()+".png";
        File outputFile = new File(pathName);
        MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
//        OutputStream os = new FileOutputStream(pathName);
//        MatrixToImageWriter.writeToStream(bitMatrix, format, os);
        return pathName;
    }

    /**
     * 根据内容，生成指定宽高、指定格式的二维码文件流
     * @param text   内容
     * @param width  宽
     * @param height 高
     * @param format 图片格式
     * @return 生成的二维码图片base64
     * @throws Exception
     */
    public static String generateQRCodeStream(String text, int width, int height, String format) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream os = new ByteArrayOutputStream();//新建流。
        ImageIO.write(image, format, os);//利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流。
        byte b[] = os.toByteArray();//从流中获取数据数组。
        String qrcodeBase64Str = new BASE64Encoder().encode(b).trim();
//        System.out.println(qrcodeBase64Str);
        return qrcodeBase64Str;
    }

    /**
     * 解析指定路径下的二维码图片
     *
     * @param filePath 二维码图片路径
     * @return
     */
    public static String parseQRCode(String filePath) {
        String content = "";
        try {
            File file = new File(filePath);
            BufferedImage image = ImageIO.read(file);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            MultiFormatReader formatReader = new MultiFormatReader();
            Result result = formatReader.decode(binaryBitmap, hints);

            System.out.println("result 为：" + result.toString());
            System.out.println("resultFormat 为：" + result.getBarcodeFormat());
            System.out.println("resultText 为：" + result.getText());
            //设置返回值
            content = result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

//    public static void main(String[] args) throws IOException{
//        int width = 300;    //二维码图片的宽
//        int height = 300;   //二维码图片的高
//        String format = "png";  //二维码图片的格式
//        String text = "www.baidu.com";
//        try {
//            //生成二维码图片，并返回图片路径
////            String pathName = generateQRCodeFile(text, width, height, format);
////            System.out.println("生成二维码的图片路径： " + pathName);
//
//            //生成二维码base64
//            String base64Str = generateQRCodeStream(text, width, height, format);
//            System.out.println(base64Str.length());
////            String content = parseQRCode(pathName);
////            System.out.println("解析出二维码的图片的内容为： " + content);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

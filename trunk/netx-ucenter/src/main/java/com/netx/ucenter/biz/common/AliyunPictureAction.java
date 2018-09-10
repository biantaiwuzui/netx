package com.netx.ucenter.biz.common;

import com.aliyun.openservices.ServiceException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSErrorCode;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.CannedAccessControlList;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectListing;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.netx.common.common.enums.AliyunBucketType;
import com.netx.common.common.utils.CommonPictureValidateUtil;
import com.netx.utils.time.DateTimeFormatterProvider;
import com.netx.utils.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * Created by CloudZou on 1/10/2018.
 */
@Service
public class AliyunPictureAction {
    private static final Log LOG = LogFactory.getLog(AliyunPictureAction.class);

    /**
     * 云端上面的BucketName.相当于命名空间 ,这个是默认的.
     */
    private String defaultBucketName;

    /**
     * 阿里云STS版本信息
     */
    private String stsVersion;

    /**
     * 阿里云STS服务api版本信息
     */
    private String stsApiVersion;

    /**
     * 阿里云STS服务RegionId
     */
    private String stsRegionId;

    private String aliyunCategory = "Aliyun";


    @Value("${netx.oss.end-point}")
    private String ossEndPoint;

    @Value("${netx.oss.bucket-prefix}")
    private String bucketPrefix;

    @Value("${netx.oss.access.key}")
    private String accessId;

    @Value("${netx.oss.access.secret}")
    private String accessKey;

    /**
     * 上传图片
     *
     * @param multipartFile MultipartFile
     * @param bucketName    阿里云上次面的bucketName,相当于命名空间.
     * @param directory     阿里云端上面的目录路径 如：abc/pop/jim/kao/，注:最左面没有"/",最右面需要带"/"
     * @param pictureName   图片名称
     * @throws Exception
     */
    public String uploadPicture(MultipartFile multipartFile, String bucketName, String directory, String pictureName)
            throws Exception {

        if (directory.startsWith("/")) {
            directory = directory.substring(1);
        }
        if (!directory.endsWith("/")) {
            directory = directory + "/";
        }
        if (pictureName.startsWith("/")) {
            pictureName = pictureName.substring(1);
        }
        // 上传文件到云端bucket的完整路径.
        final String objectFilePathInBucket = directory + pictureName;
        // final String objectFilePathInBucket = pictureName;
        // 使用默认的OSS服务器地址创建OSSClient对象。
        //OSSClient client = new OSSClient("http://oss-cn-shenzhen.aliyuncs.com", systemConstantWrapper.getAccessId(), systemConstantWrapper.getAccessKey());

        OSSClient client = new OSSClient(ossEndPoint,  accessId, accessKey);

        bucketName = bucketPrefix + "-" + bucketName;

        // 如果此Bucket不存在,则创建,如果存在则不做处理
        ensureBucket(client, bucketName);
        try {
            // 把Bucket设置为可读写的
            setBucketPublicReadable(client, bucketName);

            ObjectMetadata objectMeta = new ObjectMetadata();
            objectMeta.setContentLength(multipartFile.getSize());
            objectMeta.setContentType(CommonPictureValidateUtil.getImageContentType(multipartFile.getOriginalFilename()));

            // 上传.
            client.putObject(bucketName, objectFilePathInBucket, multipartFile.getInputStream(), objectMeta);

            return "http://" + bucketName + "." + client.getBucketLocation(bucketName) + ".aliyuncs.com/" + directory
                    + pictureName;
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(e.getMessage(), e);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传图片
     *
     * @param contentLength
     * @param stream        stream 本方法不会close stream，需要调用者自行管理
     * @param bucketName    阿里云上次面的bucketName,相当于命名空间.
     * @param directory     阿里云端上面的目录路径 如：abc/pop/jim/kao/，注:最左面没有"/",最右面需要带"/"
     * @param pictureName   图片名称
     * @throws Exception
     */
    public String uploadPicture(long contentLength, InputStream stream, String bucketName, String directory, String pictureName)
            throws Exception {

        if (directory.startsWith("/")) {
            directory = directory.substring(1);
        }
        if (!directory.endsWith("/")) {
            directory = directory + "/";
        }
        if (pictureName.startsWith("/")) {
            pictureName = pictureName.substring(1);
        }
        // 上传文件到云端bucket的完整路径.
        final String objectFilePathInBucket = directory + pictureName;
        // final String objectFilePathInBucket = pictureName;
        // 使用默认的OSS服务器地址创建OSSClient对象。
        OSSClient client = new OSSClient(ossEndPoint, accessId, accessKey);
        bucketName = bucketPrefix + "-" + bucketName;
        // 如果此Bucket不存在,则创建,如果存在则不做处理
        ensureBucket(client, bucketName);
        try {
            // 把Bucket设置为可读写的
            setBucketPublicReadable(client, bucketName);

            ObjectMetadata objectMeta = new ObjectMetadata();
            objectMeta.setContentLength(contentLength);
            objectMeta.setContentType(CommonPictureValidateUtil.getImageContentType(pictureName));

            // 上传.
            client.putObject(bucketName, objectFilePathInBucket, stream, objectMeta);

            return "http://" + bucketName + "." + client.getBucketLocation(bucketName) + ".aliyuncs.com/" + directory
                    + pictureName;
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(e.getMessage(), e);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 从本地上传图片到阿里云.
     *
     * @param fullFilePath 本地文件的完整路径
     * @param bucketName   阿里云上面的bucketName,相当于命名空间.
     * @param directory    阿里云端上面的目录路径 如：abc/pop/jim/kao/
     * @param pictureName  图片名称
     * @throws Exception
     */
    public String uploadPicture(String fullFilePath, String bucketName, String directory, String pictureName)
            throws Exception {

        if (directory.startsWith("/")) {
            directory = directory.substring(1);
        }
        if (!directory.endsWith("/")) {
            directory = directory + "/";
        }
        if (pictureName.startsWith("/")) {
            pictureName = pictureName.substring(1);
        }
        // 上传文件到云端bucket的完整路径.
        final String objectFilePathInBucket = directory + pictureName;
        // 使用默认的OSS服务器地址创建OSSClient对象。
        OSSClient client = new OSSClient(ossEndPoint, accessId, accessKey);

        // 如果此Bucket不存在,则创建,如果存在则不做处理
        ensureBucket(client, bucketName);

        InputStream inputStream = null;
        try {
            // 把Bucket设置为可读写的
            setBucketPublicReadable(client, bucketName);

            File file = new File(fullFilePath);
            inputStream = new FileInputStream(file);
            ObjectMetadata objectMeta = new ObjectMetadata();
            objectMeta.setContentLength(file.length());
            objectMeta.setContentType(CommonPictureValidateUtil.getImageContentType(file.getName()));
            // 可以在metadata中标记文件类型

            // 上传.
            client.putObject(bucketName, objectFilePathInBucket, inputStream, objectMeta);
            return "http://" + bucketName + "." + client.getBucketLocation(bucketName) + ".aliyuncs.com/" + directory
                    + pictureName;
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(e.getMessage(), e);
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e2) {
                }
            }
        }
        return "";
    }

    /**
     * 下载网络图片
     * @param url
     * @return
     */
    public InputStream downloadNetPicture(String url){
        try {
            URL urls = new URL(url);
            //打开链接
            HttpURLConnection conn = (HttpURLConnection)urls.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            return conn.getInputStream();
        }catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 上传网络图片
     * @param inputStream 图片流
     * @param aliyunBucketType 阿里云上面的bucketName对应的枚举
     * @return
     * @throws Exception
     */
    public String uploadNetPicture(InputStream inputStream, AliyunBucketType aliyunBucketType) throws Exception{
        String realPictureName = UUID.randomUUID().toString().replace("-", "").toUpperCase() + ".png";
        String folder = DateUtils.formatNow(DateTimeFormatterProvider.yyyyMM);
        String relativePath = folder + "/" + realPictureName;
        //return uploadNetPicture(inputStream,aliyunBucketType.getName(),folder,realPictureName)==null?null:uploadImgDomainPre + aliyunBucketType.getName() + uploadImgDomainSuffix + "/" + relativePath;
        return uploadNetPicture(inputStream,aliyunBucketType.getName(),folder,realPictureName)==null?null:"/" + relativePath;
    }

    /**
     * 上传网络图片
     * @param inputStream 图片流
     * @param bucketName 阿里云上面的bucketName
     * @param directory 阿里云端上面的目录路径 如：abc/pop/jim/kao/
     * @param pictureName 图片名称
     * @return
     * @throws Exception
     */
    public String uploadNetPicture(InputStream inputStream, String bucketName, String directory, String pictureName) throws  Exception{
        // 上传文件到云端bucket的完整路径.
        final String objectFilePathInBucket = directory + "/" + pictureName;
        // final String objectFilePathInBucket = pictureName;
        // 使用默认的OSS服务器地址创建OSSClient对象。
        OSSClient client = new OSSClient(ossEndPoint, accessId, accessKey);
        bucketName = bucketPrefix + "-" + bucketName;
        // 如果此Bucket不存在,则创建,如果存在则不做处理
        ensureBucket(client, bucketName);
        try {
            // 把Bucket设置为可读写的
            setBucketPublicReadable(client, bucketName);
            ObjectMetadata objectMeta = new ObjectMetadata();
            objectMeta.setContentLength(inputStream.available());
            objectMeta.setContentType(CommonPictureValidateUtil.getImageContentType(pictureName));
            // 上传.
            client.putObject(bucketName, objectFilePathInBucket, inputStream, objectMeta);
            return "http://" + bucketName + "." + client.getBucketLocation(bucketName) + ".aliyuncs.com/" + directory
                    + pictureName;
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(e.getMessage(), e);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 从本地上传图片到阿里云.
     *
     * @param fullFilePath 本地文件的完整路径
     * @param directory    阿里云端上面的目录路径 如：abc/pop/jim/kao/
     * @param pictureName  图片名称
     * @throws Exception
     */
    public String uploadPicture(String fullFilePath, String directory, String pictureName) throws Exception {
        return uploadPicture(fullFilePath, this.defaultBucketName, directory, pictureName);
    }

    /**
     * 上传图片
     *
     * @param multipartFile MultipartFile
     * @param directory     阿里云端上面的目录路径 如：abc/pop/jim/kao/
     * @param pictureName   图片名称
     * @throws Exception
     */
    public String uploadPicture(MultipartFile multipartFile, String directory, String pictureName) throws Exception {
        return this.uploadPicture(multipartFile, defaultBucketName, directory, pictureName);
    }

    /**
     * 上传图片
     *
     * @param multipartFile MultipartFile
     * @param directory     阿里云端上面的目录路径 如：abc/pop/jim/kao/，注:最左面没有"/",最右面需要带"/"
     * @param pictureName   图片名称
     * @throws Exception
     */
    public String uploadPicture(MultipartFile multipartFile, AliyunBucketType aliyunBucketType, String directory,
                                String pictureName) throws Exception {
        return uploadPicture(multipartFile, aliyunBucketType.getName(), directory, pictureName);
    }

    /**
     * 从本地上传图片到阿里云
     *
     * @param fullFilePath 本地文件的完整路径
     * @param directory    阿里云端上面的目录路径 如：abc/pop/jim/kao/
     * @param pictureName  图片名称
     * @throws Exception
     */
    public String uploadPicture(String fullFilePath, AliyunBucketType aliyunBucketType, String directory,
                                String pictureName) throws Exception {
        return uploadPicture(fullFilePath, aliyunBucketType.getName(), directory, pictureName);
    }

    /**
     * @param uploadContent
     * @param bucketName
     * @param directory
     * @param fileName
     * @return String
     * @throws Exception
     * @throws
     * @Description: 上传以字符串形式的文件
     */
    public String uploadFile(String uploadContent, String bucketName, String directory, String fileName)
            throws Exception {

        if (directory.startsWith("/")) {
            directory = directory.substring(1);
        }
        if (!directory.endsWith("/")) {
            directory = directory + "/";
        }
        if (fileName.startsWith("/")) {
            fileName = fileName.substring(1);
        }
        // 上传文件到云端bucket的完整路径.
        final String objectFilePathInBucket = directory + fileName;
        // 使用默认的OSS服务器地址创建OSSClient对象。
        OSSClient client = new OSSClient(accessId, accessKey);

        // 如果此Bucket不存在,则创建,如果存在则不做处理
        ensureBucket(client, bucketName);

        InputStream inputStream = null;
        try {
            // 把Bucket设置为可读写的
            setBucketPublicReadable(client, bucketName);

            ObjectMetadata objectMeta = new ObjectMetadata();
            objectMeta.setContentLength(uploadContent.length());
            inputStream = new ByteArrayInputStream(uploadContent.getBytes());

            // 上传.
            client.putObject(bucketName, objectFilePathInBucket, inputStream, objectMeta);
            return "http://" + bucketName + "." + client.getBucketLocation(bucketName) + ".aliyuncs.com/" + directory
                    + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            if (LOG.isErrorEnabled()) {
                LOG.error(e.getMessage(), e);
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e2) {
                }
            }
        }
        return "";
    }



    private String getPolicy(String bucketName) {
        String policy = "{" +
                "    \"Version\": \"1\", " +
                "    \"Statement\": [" +
                "        {" +
                "            \"Action\": [" +
                "                \"oss:PutObject\"," +
                "                \"oss:ListObjects\"" +
                "            ], " +
                "            \"Resource\": [" +
                "                \"acs:oss:*:*:" + bucketName + "\"," + //bucket级API
                "                \"acs:oss:*:*:" + bucketName + "/*\"" +  //object级API
                "            ], " +
                "            \"Effect\": \"Allow\"," +
                "            \"Condition\": {" +
//                "                \"StringEquals\": {" +
//                "                    \"acs:UserAgent\": \"vmei_app\", " +
//                "                    \"oss:Delimiter\": \"/\", " +
//                "                    \"oss:Prefix\": \"foo\"" +
//                "                } " +
//                "                \"IpAddress\": {" +
//                "                    \"acs:SourceIp\": \"192.168.0.0\"" +
//                "                }" +
                "            }" +
                "        }" +
                "    ]" +
                "}";
        return policy;
    }

    /**
     * 创建Bucket,如果不存在则创建如果存在则不做处理
     *
     * @param client     OSSClient
     * @param bucketName bucketName
     * @throws OSSException
     * @throws Exception
     */
    private void ensureBucket(OSSClient client, String bucketName) throws OSSException, Exception {
        try {
            // 创建bucket
            client.createBucket(bucketName);
        } catch (ServiceException e) {
            if (!OSSErrorCode.BUCKES_ALREADY_EXISTS.equals(e.getErrorCode())) {
                // 如果Bucket已经存在，则忽略
                throw e;
            }
        }
    }

    /**
     * 把Bucket设置为所有人可读.
     *
     * @param client
     * @param bucketName
     * @throws OSSException
     * @throws Exception
     */
    private void setBucketPublicReadable(OSSClient client, String bucketName) throws OSSException,
            Exception {
        // 创建bucket
        //client.createBucket(bucketName);

        // 设置bucket的访问权限，public-read-write权限
        client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
    }

    /**
     * 删除一个Bucket和其中的所有Objects....慎用
     *
     * @param client
     * @param bucketName
     * @throws OSSException
     * @throws Exception
     */
    private void deleteBucket(OSSClient client, String bucketName) throws OSSException, Exception {
        ObjectListing ObjectListing = client.listObjects(bucketName);
        List<OSSObjectSummary> listDeletes = ObjectListing.getObjectSummaries();
        for (int i = 0; i < listDeletes.size(); i++) {
            String objectName = listDeletes.get(i).getKey();
            // 如果不为空，先删除bucket下的文件
            client.deleteObject(bucketName, objectName);
        }
        client.deleteBucket(bucketName);
    }
}
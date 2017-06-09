package com.huntkey.rx.fdfs.client.demo.tests;

import com.github.tobato.fastdfs.domain.FileInfo;
import com.github.tobato.fastdfs.domain.MateData;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.proto.storage.DownloadFileWriter;
import com.github.tobato.fastdfs.service.DefaultGenerateStorageClient;
import com.huntkey.rx.fdfs.client.demo.FdfsClientDemoApplicationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * DefaultGenerateStorageClient 用法示例
 *
 * Created by chenfei on 2017/5/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FdfsClientDemoApplicationTest.class)
public class GenerateStorageClientTests {

    @Autowired
    private DefaultGenerateStorageClient defaultGenerateStorageClient;

    /**
     * 测试上传文件
     */
    @Test
    public void testUpload() {
        try {
            Resource resource = new ClassPathResource("images/cat.jpg");
            File file = resource.getFile();
            String groupName = "group1";
            InputStream inputStream = new FileInputStream(file);
            long fileSize = inputStream.available();
            String fileExtName = "jpg";
            StorePath path = defaultGenerateStorageClient.uploadFile(groupName, inputStream, fileSize, fileExtName);
            System.out.println(path.getFullPath());
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //group1/M00/00/00/wKgNIFkmnaCALuY7AADJbd1CckA179.jpg

    /**
     * 测试下载，返回字节数组
     */
    @Test
    public void testDownload() {

        String groupName = "group1";
        String path = "M00/00/00/wKgNIFkmnaCALuY7AADJbd1CckA179.jpg";
        DownloadByteArray callback = new DownloadByteArray();
        byte[] content = defaultGenerateStorageClient.downloadFile(groupName, path, callback);
        Assert.assertEquals(51565, content.length);
    }

    /**
     * 测试下载，落地成文件
     */
    @Test
    public void testDownload2() {

        String groupName = "group1";
        String path = "M00/00/00/wKgNIFkmnaCALuY7AADJbd1CckA179.jpg";
        DownloadFileWriter callback = new DownloadFileWriter("/tmp/cat.jpg");
        String fileName = defaultGenerateStorageClient.downloadFile(groupName, path, callback);
        Assert.assertEquals("/tmp/cat.jpg", fileName);
    }

    /**
     * 测试断点下载
     */
    @Test
    public void testAppendDownload() {
        String groupName = "group1";
        String path = "M00/00/00/wKgNIFkmnaCALuY7AADJbd1CckA179.jpg";
        DownloadFileWriter callback = new DownloadFileWriter("/tmp/cat.jpg");

        long fileOffset = 0L;
        long fileSize = 1000L;

        long fileOffset2 = 1001L;
        long fileSize2 = 51565L;

        defaultGenerateStorageClient.downloadFile(groupName, path, fileOffset, fileSize, callback);
        defaultGenerateStorageClient.downloadFile(groupName, path, fileOffset2, fileSize2, callback);
    }

    /**
     * 测试添加文件属性
     */
    @Test
    public void testOverwriteMetaData() {
        Set<MateData> metaDataSet = new HashSet<MateData>();

        metaDataSet.add(new MateData("width", "800"));
        metaDataSet.add(new MateData("bgcolor", "FFFFFF"));
        metaDataSet.add(new MateData("author", "FirstMateData"));

        String groupName = "group1";
        String path = "M00/00/00/wKgNIFkmnaCALuY7AADJbd1CckA179.jpg";
        defaultGenerateStorageClient.overwriteMetadata(groupName, path, metaDataSet);

    }

    /**
     * 测试查询文件属性
     */
    @Test
    public void testMetaData() {
        String groupName = "group1";
        String path = "M00/00/00/wKgNIFkmnaCALuY7AADJbd1CckA179.jpg";

        Set<MateData> mateDataSet =  defaultGenerateStorageClient.getMetadata(groupName, path);
        System.out.println(mateDataSet);
        Assert.assertNotNull(mateDataSet);
    }

    /**
     * 测试合并文件属性
     */
    @Test
    public void testMergeMetaData() {
        Set<MateData> metaDataSet = new HashSet<MateData>();

        metaDataSet.add(new MateData("length", "1000"));
        String groupName = "group1";
        String path = "M00/00/00/wKgNIFkmnaCALuY7AADJbd1CckA179.jpg";
        defaultGenerateStorageClient.mergeMetadata(groupName, path, metaDataSet);

    }

    /**
     * 测试查询文件基本信息
     */
    @Test
    public void testFileInfo() {
        String groupName = "group1";
        String path = "M00/00/00/wKgNIFkmnaCALuY7AADJbd1CckA179.jpg";

        FileInfo fileInfo = defaultGenerateStorageClient.queryFileInfo(groupName, path);
        System.out.println(fileInfo);
        Assert.assertNotNull(fileInfo);
    }

    /**
     * 测试删除文件
     */
    @Test
    public void testDeleteFile() {
        try {
            Resource resource = new ClassPathResource("images/cat.jpg");
            File file = resource.getFile();
            String groupName = "group1";
            InputStream inputStream = new FileInputStream(file);
            long fileSize = inputStream.available();
            String fileExtName = "jpg";
            StorePath path = defaultGenerateStorageClient.uploadFile(groupName, inputStream, fileSize, fileExtName);
            System.out.println(path.getFullPath());
            defaultGenerateStorageClient.deleteFile(path.getGroup(), path.getPath());
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package com.huntkey.rx.fdfs.client.demo.tests;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.DefaultAppendFileStorageClient;
import com.huntkey.rx.fdfs.client.demo.FdfsClientDemoApplicationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by chenfei on 2017/5/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FdfsClientDemoApplicationTest.class)
public class AppendFileStorageClientTests {

    @Autowired
    private DefaultAppendFileStorageClient defaultAppendFileStorageClient;

    /**
     * 测试断点续传
     */
    @Test
    public void testAppendFile() {

        try {
            String groupName = "group1";
            String firstText = "This is first paragraph.\r\n";
            InputStream firstIn = getTextInputStream(firstText);
            long firstSize = firstIn.available();
            // 先上传第一段文字
            StorePath path = defaultAppendFileStorageClient.uploadAppenderFile(groupName, firstIn, firstSize, "txt");

            // 上传第二段文字
            String secondText = "This is second paragraph. \r\n";
            InputStream secondIn = getTextInputStream(secondText);
            long secondSize = secondIn.available();

            defaultAppendFileStorageClient.appendFile(groupName, path.getPath(), secondIn, secondSize);
            System.out.println(path.getFullPath());
            firstIn.close();
            secondIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 测试文件内容修改
     */
    @Test
    public void testModifyFile() {
        try {
            String groupName = "group1";
            String firstText = "This is first paragraph.\r\n";
            InputStream firstIn = getTextInputStream(firstText);
            long firstSize = firstIn.available();
            StorePath path = defaultAppendFileStorageClient.uploadAppenderFile(groupName, firstIn, firstSize, "txt");

            String secondText = "This is modify paragraph. \r\n";
            InputStream secondIn = getTextInputStream(secondText);
            long secondSize = secondIn.available();
            // 从头开始进行修改
            long fileOffset = 0L;

            defaultAppendFileStorageClient.modifyFile(groupName, path.getPath(), secondIn, secondSize, fileOffset);
            System.out.println(path.getFullPath());
            firstIn.close();
            secondIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试清空文件
     */
    @Test
    public void testTruncateFile() {
        try {
            String groupName = "group1";
            String firstText = "This is first paragraph.\r\n";
            InputStream firstIn = getTextInputStream(firstText);
            long firstSize = firstIn.available();
            StorePath path = defaultAppendFileStorageClient.uploadAppenderFile(groupName, firstIn, firstSize, "txt");

            // 从第 4 个字符开始截取
            // 如果是 0 的话，则文件的内容被清空
            // FIXME 测试过程中发现如果fileOffset不为0的情况，会有异常抛出
            long fileOffset = 0L;

            defaultAppendFileStorageClient.truncateFile(groupName, path.getPath(), fileOffset);
            System.out.println(path.getFullPath());
            firstIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private InputStream getTextInputStream(String text) throws Exception {
        return new ByteArrayInputStream(text.getBytes("UTF-8"));
    }
}

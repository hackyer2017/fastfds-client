package com.huntkey.rx.fdfs.client.demo.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 导入依赖组件
 *
 * Created by chenfei on 2017/5/25.
 */
@Configuration
@Import(FdfsClientConfig.class)
public class ComponetImport {

}


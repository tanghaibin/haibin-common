package com.haibin.common.util;

import com.haibin.common.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

/**
 * 新建文件工具
 *
 * @author haibin.tang
 * @create 2018-06-21 下午10:08
 **/
public class FileUitl {

    private static final Logger LOG = LoggerFactory.getLogger(FileUitl.class);

    /**
     * 新建图片
     *
     * @param inputStream 图片流
     * @param path        路径
     * @param type 类型
     * @return
     */
    public static String createImg(InputStream inputStream, final String path, final String type) {
        FileImageOutputStream fileImageOutputStream = null;
        try {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = StringUtil.getUUID() + "." + type;
            File newFile = new File(path + fileName);
            LOG.info("新建图片:{}", path + fileName);
            boolean success = newFile.createNewFile();
            if(!success) {
                throw new BizException("新建文件失败");
            }
            fileImageOutputStream = new FileImageOutputStream(newFile);
            byte[] buf = new byte[1024 * 1024];
            while (inputStream.read(buf) != -1) {
                fileImageOutputStream.write(buf, 0, buf.length);
            }
            return fileName;
        } catch (Exception e) {
            throw new BizException("新建图片失败", e);
        } finally {
            if (fileImageOutputStream != null) {
                try {
                    fileImageOutputStream.close();
                } catch (IOException e) {
                    LOG.error("关闭图片流失败", e);
                }
            }
        }
    }
}

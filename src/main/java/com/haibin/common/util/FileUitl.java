package com.haibin.common.util;

import com.haibin.common.exception.BizException;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * 新建文件工具
 *
 * @author haibin.tang
 * @create 2018-06-21 下午10:08
 **/
public class FileUitl {

    /**
     * 新建图片
     * @param inputStream  图片流
     * @param path 路径
     * @return
     */
    public static String newImg(InputStream inputStream, final String path, final String suffix) {
        try {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = StringUtil.getUUID() + suffix;
            File newFile = new File(path + fileName);
            newFile.createNewFile();
            FileImageOutputStream fileImageOutputStream = new FileImageOutputStream(newFile);
            byte[] buf = new byte[1024 * 1024];
            while (inputStream.read(buf) != -1) {
                fileImageOutputStream.write(buf, 0, buf.length);
            }
            return fileName;
        }catch (Exception e) {
            throw new BizException("新建图片失败", e);
        }
    }
}

package com.tcomponent.secretkeeper.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;


@Slf4j
public class FileUtil {

    /**
     * 从文件中读取整个字符串
     *
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), Charset.forName("UTF-8")));

            while (StringUtils.hasLength(temp = bufferedReader.readLine())) {
                stringBuffer.append(temp + "\n");
            }
            //关闭流
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            log.error("文件不存在:{}", filePath);
            throw new RuntimeException("文件不存在:" + filePath);
        } catch (IOException e) {
            log.error("读取文件出错:{}", filePath);
            throw new RuntimeException("读取文件出错:" + filePath);
        }
        return stringBuffer.toString();
    }

    /**
     * 写入文件(覆盖形式)
     *
     * @param str
     * @param filePath
     */
    public static void writeFile(String str, String filePath) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), Charset.forName("UTF-8")));
            bufferedWriter.write(str);
            //关闭流
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            log.error("文件不存在:{}", filePath);
            throw new RuntimeException("文件不存在:" + filePath);
        } catch (IOException e) {
            log.error("写入文件出错:{}", filePath);
            throw new RuntimeException("写入文件出错:" + filePath);
        }
    }
}

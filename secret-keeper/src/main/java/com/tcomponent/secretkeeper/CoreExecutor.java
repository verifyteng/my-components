package com.tcomponent.secretkeeper;

import com.tcomponent.secretkeeper.entity.OperatorMode;
import com.tcomponent.secretkeeper.util.AESEncryptor;
import com.tcomponent.secretkeeper.util.FileUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class CoreExecutor {

    public void doParseAndExecute(String[] args) {
        //命令行参数解析
        CMDParam cmdParam = new CMDParam();
        for (int i = 0; i < args.length; i++) {
            if (!args[i].startsWith("-")) {
                log.error("入参格式错误,请按\"-k value\"的格式进行入参输入");
                throw new RuntimeException("入参格式错误,请按\"-k value\"的格式进行入参输入");
            }

            char c = args[i].charAt(1);

            switch (c) {
                case 'h':
                    System.out.println("参数说明:\n-p 加密密钥\n" +
                            "-r 读文件地址\n" +
                            "-w 写文件地址(注意:非追加模式下,写文件的内容将会被全部覆盖)\n" +
                            "-m 工作模式,加密为e,解密为d,加密并追加为ea\n" +
                            "-s 输入的字符串\n" +
                            "-c 是否打印加解密字符串到控制台,加上此参数就表示启用\n");
                    break;
                case 'c':
                    cmdParam.setConsolePrint(true);
                    break;
                case 'p':
                    cmdParam.setPassword(args[++i]);
                    break;
                case 'r':
                    cmdParam.setRFile(args[++i]);
                    break;
                case 'w':
                    cmdParam.setWFile(args[++i]);
                    break;
                case 'm':
                    cmdParam.setMode(Enum.valueOf(OperatorMode.class, args[++i]));
                    break;
                case 's':
                    cmdParam.setStr(args[++i]);
                    break;
                default:
                    throw new RuntimeException("错误的参数名");
            }
        }

        //执行命令(必须选择了一种模式,才表示要执行其他的逻辑,以便于兼容-h的情况)
        if (Objects.nonNull(cmdParam.getMode())) {
            doExecute(cmdParam);
        }
    }

    private void doExecute(CMDParam cmdParam) {
        if (Objects.isNull(cmdParam.getPassword())) {
            log.error("加密密钥不能为空");
            throw new RuntimeException("加密密钥不能为空");
        }

        if (Objects.nonNull(cmdParam.getRFile()) && Objects.nonNull(cmdParam.getStr())) {
            log.error("只能有一个输入途径(文件和输入字符串请二选一)");
            throw new RuntimeException("只能有一个输入途径(文件和输入字符串请二选一)");
        }

        if (Objects.isNull(cmdParam.getRFile()) && Objects.isNull(cmdParam.getStr())) {
            log.error("必须要有一个输入项");
            throw new RuntimeException("必须要有一个输入项");
        }

        if (Objects.isNull(cmdParam.getMode())) {
            log.error("请选择一种工作模式");
            throw new RuntimeException("请选择一种工作模式");
        }

        if (cmdParam.consolePrint) {
            System.out.println("获取到密钥:" + cmdParam.getPassword() + "(请谨记此密钥,我们不会对其进行保存,一旦丢失则文件永远无法解密)");
        }
        AESEncryptor aesEncryptor = new AESEncryptor(cmdParam.getPassword());

        switch (cmdParam.getMode()) {
            case e:
                String encryptStr = "";
                if (Objects.nonNull(cmdParam.getRFile())) {
                    String readStr = FileUtil.readFile(cmdParam.getRFile());
                    encryptStr = aesEncryptor.encrypt(readStr);
                    if (cmdParam.consolePrint) {
                        System.out.println("加密后的字符串:\n" + encryptStr);
                    }
                }
                if (Objects.nonNull(cmdParam.getStr())) {
                    encryptStr = aesEncryptor.encrypt(cmdParam.getStr());
                    if (cmdParam.consolePrint) {
                        System.out.println("加密后的字符串:\n" + encryptStr);
                    }
                }

                if (Objects.nonNull(cmdParam.getWFile())) {
                    FileUtil.writeFile(encryptStr, cmdParam.getWFile());
                }
                break;
            case d:
                String decryptStr = "";

                if (Objects.nonNull(cmdParam.getRFile())) {
                    String readStr = FileUtil.readFile(cmdParam.getRFile());
                    decryptStr = aesEncryptor.decrypt(readStr);
                    if (cmdParam.consolePrint) {
                        System.out.println("解密后的字符串:" + decryptStr);
                    }
                }
                if (Objects.nonNull(cmdParam.getStr())) {
                    decryptStr = aesEncryptor.decrypt(cmdParam.getStr());
                    if (cmdParam.consolePrint) {
                        System.out.println("解密后的字符串:" + decryptStr);
                    }
                }

                if (Objects.nonNull(cmdParam.getWFile())) {
                    FileUtil.writeFile(decryptStr, cmdParam.getWFile());
                }
                break;
            case ea:
                if (Objects.isNull(cmdParam.getWFile())) {
                    log.error("追加模式下,写入文件不能为空");
                    throw new RuntimeException("追加模式下,写入文件不能为空");
                }
                //待追加的内容
                String enter = Objects.nonNull(cmdParam.getStr()) ? cmdParam.getStr() : FileUtil.readFile(cmdParam.getRFile());
                if (cmdParam.consolePrint) {
                    System.out.println("获取到待追加的字符串:" + enter);
                }

                //已加密的文件内容
                String fileStr = FileUtil.readFile(cmdParam.getWFile());
                String fileDecrypt = aesEncryptor.decrypt(fileStr);

                //最终要写入的内容
                String finalStr = fileDecrypt + "\n" + enter;
                if (cmdParam.consolePrint) {
                    System.out.println("追加后的内容:" + finalStr);
                }

                //加密并写入
                FileUtil.writeFile(aesEncryptor.encrypt(finalStr), cmdParam.getWFile());
                break;
        }
    }

    /**
     * 命令行参数
     */
    @Data
    class CMDParam {
        /**
         * 加密密钥
         */
        private String password;

        /**
         * 读文件地址
         */
        private String rFile;

        /**
         * 写文件地址
         */
        private String wFile;

        /**
         * 操作类型
         */
        private OperatorMode mode;

        /**
         * 输入的字符串
         */
        private String str;

        /**
         * 是否打印加解密字符串到控制台
         */
        private boolean consolePrint;
    }
}

package com.viewer.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jodconverter.core.office.InstalledOfficeManagerHolder;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeUtils;
import org.jodconverter.core.util.OSUtils;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.context.annotation.Configuration;
import org.apache.commons.lang3.StringUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author hcw
 * @date 2025/7/25 15:39:33
 */
@Configuration
public class OfficeConfig {

    private LocalOfficeManager officeManager;

    @PostConstruct
    public void startOfficeManager() throws OfficeException {
        killProcess();
        Path libreoffice = Paths.get(System.getProperty("user.dir")).resolve("app/libreoffice");
        officeManager = LocalOfficeManager.builder()
                .officeHome(libreoffice.toString())
                .portNumbers(2222, 2223) // 默认端口2002
                .taskExecutionTimeout(30_000L) // 任务超时时间（毫秒）
                .processTimeout(30_000L)
                .maxTasksPerProcess(200)
                .build();
        officeManager.start();
        InstalledOfficeManagerHolder.setInstance(officeManager);
        System.out.println("启动 OfficeManager");
    }

    private void killProcess() {
        try {
            if (OSUtils.IS_OS_WINDOWS) {
                Process p = Runtime.getRuntime().exec("cmd /c tasklist ");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream os = p.getInputStream();
                byte[] b = new byte[256];
                while (os.read(b) > 0) {
                    baos.write(b);
                }
                String s = baos.toString();
                if (s.contains("soffice.bin")) {
                    Runtime.getRuntime().exec("taskkill /im " + "soffice.bin" + " /f");
                }
            } else if (OSUtils.IS_OS_MAC || OSUtils.IS_OS_MAC_OSX) {
                Process p = Runtime.getRuntime().exec(new String[]{"sh", "-c", "ps -ef | grep " + "soffice.bin"});
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream os = p.getInputStream();
                byte[] b = new byte[256];
                while (os.read(b) > 0) {
                    baos.write(b);
                }
                String s = baos.toString();
                if (StringUtils.ordinalIndexOf(s, "soffice.bin", 3) > 0) {
                    String[] cmd = {"sh", "-c", "kill -15 `ps -ef|grep " + "soffice.bin" + "|awk 'NR==1{print $2}'`"};
                    Runtime.getRuntime().exec(cmd);
                }
            } else {
                Process p = Runtime.getRuntime().exec(new String[]{"sh", "-c", "ps -ef | grep " + "soffice.bin" + " |grep -v grep | wc -l"});
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream os = p.getInputStream();
                byte[] b = new byte[256];
                while (os.read(b) > 0) {
                    baos.write(b);
                }
                String s = baos.toString();
                if (!s.startsWith("0")) {
                    String[] cmd = {"sh", "-c", "ps -ef | grep soffice.bin | grep -v grep | awk '{print \"kill -9 \"$2}' | sh"};
                    Runtime.getRuntime().exec(cmd);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @PreDestroy
    public void destroyOfficeManager() {
        if (null != officeManager && officeManager.isRunning()) {
            System.out.println("停止 OfficeManager");
            OfficeUtils.stopQuietly(officeManager);
        }
    }
}

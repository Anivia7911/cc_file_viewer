package com.viewer.trivial.utils;

import com.viewer.model.FileAttributeModel;
import com.viewer.trivial.enumdata.FileType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: TODO
 * @author: Jie Bugui
 * @create: 2025-06-22 20:43
 */
public class FileUtils {

    /**
     * 获取文件后缀
     */
    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static FileAttributeModel getFileAttribute(HttpServletRequest req, String fileUrl) {
        FileAttributeModel fileAttribute = new FileAttributeModel();
        String fullFileName = getUrlParameterReg(fileUrl, "fullFileName");
        if (StringUtils.hasText(fullFileName)) {
            fileAttribute.setFileName(fullFileName);
            fileAttribute.setFileType(typeFromFileName(fullFileName));
        } else {
            fileAttribute.setFileName(getFileNameFromURL(fileUrl));
            fileAttribute.setFileType(typeFromUrl(fileUrl));
        }

        fileAttribute.setFileUrl(fileUrl);
        return fileAttribute;
    }

    public static FileType typeFromFileName(String fileName) {
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        String lowerCaseFileType = fileType.toLowerCase();
        return FileType.getFileType(lowerCaseFileType);
    }

    /**
     * 从url中剥离出文件名
     *
     * @param url 格式如：http://www.com.cn/20171113164107_月度绩效表模板(新).xls?UCloudPublicKey=ucloudtangshd@weifenf.com14355492830001993909323&Expires=&Signature=I D1NOFtAJSPT16E6imv6JWuq0k=
     * @return 文件名
     */
    public static String getFileNameFromURL(String url) {
        if (url.toLowerCase().startsWith("file:")) {
            try {
                URL urlObj = new URL(url);
                url = urlObj.getPath().substring(1);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        // 因为url的参数中可能会存在/的情况，所以直接url.lastIndexOf("/")会有问题
        // 所以先从？处将url截断，然后运用url.lastIndexOf("/")获取文件名
        String noQueryUrl = url.substring(0, url.contains("?") ? url.indexOf("?") : url.length());
        return noQueryUrl.substring(noQueryUrl.lastIndexOf("/") + 1);
    }

    /**
     * 获取url中的参数
     *
     * @param url  url
     * @param name 参数名
     * @return 参数值
     */
    public static String getUrlParameterReg(String url, String name) {
        Map<String, String> mapRequest = new HashMap<>();
        String strUrlParam = truncateUrlPage(url);
        if (strUrlParam == null) {
            return "";
        }
        //每个键值为一组
        String[] arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = strSplit.split("[=]");
            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else if (!arrSplitEqual[0].equals("")) {
                //只有参数没有值，不加入
                mapRequest.put(arrSplitEqual[0], "");
            }
        }
        return mapRequest.get(name);
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String truncateUrlPage(String strURL) {
        String strAllParam = null;
        strURL = strURL.trim();
        String[] arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }
        return strAllParam;
    }

    /**
     * 查看文件类型(防止参数中存在.点号或者其他特殊字符，所以先抽取文件名，然后再获取文件类型)
     *
     * @param url url
     * @return 文件类型
     */
    public static FileType typeFromUrl(String url) {
        String nonPramStr = url.substring(0, url.contains("?") ? url.indexOf("?") : url.length());
        String fileName = nonPramStr.substring(nonPramStr.lastIndexOf("/") + 1);
        return typeFromFileName(fileName);
    }
}
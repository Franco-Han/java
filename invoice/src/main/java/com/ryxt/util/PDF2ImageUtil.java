package com.ryxt.util;

import org.springframework.web.multipart.MultipartFile;

import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PDF2ImageUtil {
    /**
     * 图片文件格式
     */
    public static final String FORMAT_NAME = "jpeg";
    /**
     * 图片文件后缀名
     */
    public static final String PNG_SUFFIX = ".jpeg";
    /**
     * 压缩文件后缀名
     */
    public static final String ZIP_SUFFIX = ".zip";


    public static List<File> pdf2Image(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        Document document = new Document();
        document.setByteArray(file.getBytes(), 0, file.getBytes().length, fileName);
        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            BufferedImage image = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN,
                    Page.BOUNDARY_CROPBOX, 0F, 2.5F);
            File imageFile = new File(StringUtil.generateUUID() + PNG_SUFFIX);
            ImageIO.write(image, FORMAT_NAME, imageFile);
            image.flush();
            fileList.add(imageFile);
        }
        document.dispose();
        return fileList;
    }


    /**
     * 对外的开放接口，用于将PDF文件转换为图片文件压缩包进行下载
     *
     * @param file SpringMVC获取的图片文件
     * @param response
     * @throws Exception
     */
    public static void pdfToImage(MultipartFile file, HttpServletResponse response) throws Exception {
        File zipFile = generateImageFile(file);
        downloadZipFile(zipFile, response);
    }


    /**
     * 将PDF文件转换为多张图片并放入一个压缩包中
     *
     * @param file SpringMVC获取的图片文件
     * @return 图片文件压缩包
     * @throws Exception 抛出异常
     */
    private static File generateImageFile(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        Document document = new Document();
        document.setByteArray(file.getBytes(), 0, file.getBytes().length, fileName);

        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            BufferedImage image = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN,
                    Page.BOUNDARY_CROPBOX, 0F, 2.5F);
            File imageFile = new File((i + 1) + PNG_SUFFIX);
            ImageIO.write(image, FORMAT_NAME, imageFile);
            image.flush();
            fileList.add(imageFile);
        }
        document.dispose();

        String directoryName = fileName.substring(0, fileName.lastIndexOf("."));
        File zipFile = new File(directoryName + ZIP_SUFFIX);
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        zipFile(fileList, zos);
        zos.close();
        return zipFile;
    }

    /**
     * 下载zip文件
     *
     * @param zipFile  zip压缩文件
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    private static void downloadZipFile(File zipFile, HttpServletResponse response) throws IOException {
        FileInputStream fis = new FileInputStream(zipFile);
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes);
        fis.close();

        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(zipFile.getName(), "UTF-8"));
        OutputStream out = response.getOutputStream();
        out.write(bytes);
        out.flush();
        out.close();
        zipFile.delete();
    }

    /**
     * 压缩文件
     *
     * @param inputFiles 具体需要压缩的文件集合
     * @param zos        ZipOutputStream对象
     * @throws IOException IO异常
     */
    private static void zipFile(List<File> inputFiles, ZipOutputStream zos) throws IOException {
        byte[] buffer = new byte[1024];
        for (File file : inputFiles) {
            if (file.exists()) {
                if (file.isFile()) {
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    int size = 0;
                    while ((size = bis.read(buffer)) > 0) {
                        zos.write(buffer, 0, size);
                    }
                    zos.closeEntry();
                    bis.close();
                    file.delete();
                } else {
                    File[] files = file.listFiles();
                    List<File> childrenFileList = Arrays.asList(files);
                    zipFile(childrenFileList, zos);
                }
            }
        }
    }
}

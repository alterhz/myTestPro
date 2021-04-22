package com.magazine.tools.redis;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class FileUtils {

    /**
     * 遍历文件的每一行
     * @param fileName 文件路径
     * @throws IOException 文件读取失败可能抛出的异常
     */
    public static List<String> readLines(String fileName) {
        File file = new File(fileName);
        return readLines(file);
    }

    /**
     * 读取多行数据
     * @param file 文件 {@link File}类型
     * @throws IOException 文件读取失败可能抛出的异常
     */
    public static List<String> readLines(File file) {
        List<String> lines = new ArrayList<>();
        try(final FileReader fileReader = new FileReader(file, Charset.forName("UTF-8"));
            final BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String str = bufferedReader.readLine();
            while (str != null) {
                lines.add(str);

                // 读取下一行
                str = bufferedReader.readLine();
            }
        } catch (IOException e) {
            System.out.println("读取文件[" + file.getName() + "]失败 = " + e.getMessage());
        }

        return lines;
    }

    //---------------------------------------- 写入多行文本数据 ----------------------------------------

    /**
     * 写入多行数据，每次都会从新写入
     * @param fileName 文件名（全路径）
     * @param lines 多行数据，数据格式{@link String}
     * @throws IOException 写入文件是吧
     */
    public static void writeLines(String fileName, Collection<String> lines) {
        File file = new File(fileName);
        writeLines(file, lines, false);
    }

    /**
     * 写入多行数据
     * @param file {@link File}
     * @param lines 多行数据，数据格式{@link String}
     * @throws IOException 写入文件是吧
     */
    public static void writeLines(File file, Collection<String> lines, boolean append) {
        try(final FileWriter fileWriter = new FileWriter(file, Charset.forName("UTF-8"), append);
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            System.out.println("写入文件[" + file.getName() + "]失败 = " + e.getMessage());
        }
    }

    /**
     * 写入多行数据，追加方式写
     * @param fileName 文件名（全路径）
     * @param lines 多行数据，数据格式{@link String}
     * @throws IOException 写入文件是吧
     */
    public static void appendWriteLines(String fileName, Collection<String> lines) {
        File file = new File(fileName);
        writeLines(file, lines, true);
    }


    //---------------------------------------- 文件夹处理函数 ----------------------------------------


    /**
     * 获取根据最后修改时间升序排列的文件列表
     * @param dirPath 目录
     * @return 排序好的文件列表
     */
    public static File[] getAscLogFiles(String dirPath) {
        final File[] files = listLogFiles(dirPath);
        return sortByLastModifiedAsc(files);
    }

    /**
     * 指定目录下的所有log文件
     * @param dirPath 目录
     * @return 日志文件(*.log)数组
     */
    public static File[] listLogFiles(String dirPath) {
        File fileDir = new File(dirPath);
        return listLogFiles(fileDir);
    }

    public static File[] listLogFiles(File fileDir) {
        return fileDir.listFiles(f -> f.getName().endsWith(".log"));
    }

    /**
     * 根据文件最后修改时间升序排列
     * @param files 文件列表
     * @return 排序后的文件数组
     */
    public static File[] sortByLastModifiedAsc(final File[] files) {
        // 按照文件最后修改时间，旧新排序
        Arrays.sort(files, Comparator.comparingLong(File::lastModified));
        return files;
    }

    /**
     * 根据文件最后修改时间升序排列
     * @param files 文件列表
     * @return 排序后的文件数组
     */
    public static List<File> sortByLastModifiedAsc(List<File> files) {
        Collections.sort(files, Comparator.comparingLong(File::lastModified));
        return files;
    }

    /**
     * 文件或者路径是否存在
     * @param path
     * @return
     */
    public static boolean exists(String path) {
        return new File(path).exists();
    }

}

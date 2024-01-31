package com.emt.pdgo.next.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileCutUtils {

    private List<File> littlefilelist=new ArrayList<>();
    private String FileCathePath = Environment.getExternalStorageDirectory() + File.separator + "update";   //切片视频切割后缓存地址

    public String filePath = FileCathePath + File.separator;

    /**
     * 文件分割方法
     * @param targetFile 分割的文件
     * @param cutSize 分割文件的大小
     * @return int 文件切割的个数
     */
    public int getSplitFile(File targetFile, long cutSize) {

        //计算切割文件大小
        int count = targetFile.length() % cutSize == 0 ? (int) (targetFile.length() / cutSize) :
                (int) (targetFile.length() / cutSize + 1);

        RandomAccessFile raf = null;
        try {
            //获取目标文件 预分配文件所占的空间 在磁盘中创建一个指定大小的文件   r 是只读
            raf = new RandomAccessFile(targetFile, "r");
            long length = raf.length();//文件的总长度
            long maxSize = length / count;//文件切片后的长度
            long offSet = 0L;//初始化偏移量
//            long len = targetFile.length() % cutSize;
            for (int i = 0; i < count - 1; i++) { //最后一片单独处理
                long begin = offSet;
                long end = (i + 1) * maxSize;
                offSet = getWrite(targetFile.getAbsolutePath(), i, begin, end);
            }
            if (length - offSet > 0) {
                getWrite(targetFile.getAbsolutePath(), count-1, offSet, length);
            }

        } catch (FileNotFoundException e) {
//            System.out.println("没有找到文件");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return count;
    }
    /**
     * 指定文件每一份的边界，写入不同文件中
     * @param file 源文件地址
     * @param index 源文件的顺序标识
     * @param begin 开始指针的位置
     * @param end 结束指针的位置

     * @return long
     */
    public long getWrite(String file,int index,long begin,long end){

        long endPointer = 0L;

        String a=file.split(suffixName(new File(file)))[0];

        try {
            //申明文件切割后的文件磁盘
            RandomAccessFile in = new RandomAccessFile(new File(file), "r");
            //定义一个可读，可写的文件并且后缀名为.tmp的二进制文件
            //判断文件夹是否存在,如果不存在则创建文件夹
            createFileFolder(FileCathePath);
            //读取切片文件
            File mFile = new File(filePath + "pd-go" + "_" + index + file.substring(file.lastIndexOf(".")));
            littlefilelist.add(mFile);
            //如果存在
            if (!isFileExist(mFile)) {
                RandomAccessFile out = new RandomAccessFile(mFile, "rw");
                //申明具体每一文件的字节数组
                byte[] b = new byte[2048];
                int n = 0;
                //从指定位置读取文件字节流
                in.seek(begin);
                //判断文件流读取的边界
//                while ((n = in.read(b)) != -1 && in.getFilePointer() <= end) {
//                    //从指定每一份文件的范围，写入不同的文件
//                    out.write(b, 0, n);
//                }
                while(in.getFilePointer() <= end && (n = in.read(b)) != -1) {
                    //从指定每一份文件的范围，写入不同的文件
                    out.write(b, 0, n);
                }

                    //定义当前读取文件的指针
                endPointer = in.getFilePointer();
                //关闭输入流
                in.close();
                //关闭输出流
                out.close();
            }else {
                //不存在

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return endPointer;
    }

    /**
     * 获取文件后缀名 例如：.mp4 /.jpg /.apk
     * @param file 指定文件
     * @return String 文件后缀名
     */
    public static String suffixName (File file){
        String fileName=file.getName();
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     *
     * @param path 文件夹路径
     */
    public void createFileFolder(String path) {
        File file = new File(path);
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            file.mkdir();
        }
    }

    // 判断文件是否存在
    public static boolean isFileExist(File file) {
        return file.exists();
    }

    public void deleteLittlelist() {
        if (littlefilelist!=null&&littlefilelist.size()>0){
            littlefilelist.clear();
        }
        deleteDirWihtFile(new File(FileCathePath));
    }

    public List<File> getLittlefilelist() {
        return littlefilelist;
    }

    /**
     * 删除文件
     *
     * @param dir
     */
    public void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    /** 删除单个文件
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public  void deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
            } else {
                Log.e("--Method--","删除单个文件" + filePath$Name + "失败！");
            }
        } else {

        }
    }


}

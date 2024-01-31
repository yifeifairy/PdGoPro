package com.emt.pdgo.next.util.logger;

/**
 * 输出工作台的格式
 *
 * @author chenjh
 * @date 2018/11/14 16:54
 */
public interface LogTool {
    void d(String tag, String message);

    void e(String tag, String message);

    void w(String tag, String message);

    void i(String tag, String message);

    void v(String tag, String message);

    void wtf(String tag, String message);
}
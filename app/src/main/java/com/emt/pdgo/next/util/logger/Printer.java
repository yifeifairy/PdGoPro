package com.emt.pdgo.next.util.logger;

/**
 * 输出工作台的格式
 *
 * @author chenjh
 * @date 2018/11/14 16:53
 */
public interface Printer {


    Printer t(String tag, int methodCount);

    Settings init(String tag);

    Settings getSettings();

    void d(String message, Object... args);

    void e(String message, Object... args);

    void e(Throwable throwable, String message, Object... args);

    void w(String message, Object... args);

    void i(String message, Object... args);

    void v(String message, Object... args);

    void wtf(String message, Object... args);

    void json(String json);

    void xml(String xml);

    void clear();


}

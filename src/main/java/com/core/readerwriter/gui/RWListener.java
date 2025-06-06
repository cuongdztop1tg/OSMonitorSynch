package com.core.readerwriter.gui;

public interface RWListener {
    void onReaderWaiting(int id);

    void onWriterWaiting(int id);

    void onReaderStart(int id);

    void onReaderStop(int id);

    void onWriterStart(int id);

    void onWriterStop(int id);

    void logMessage(String message);
}
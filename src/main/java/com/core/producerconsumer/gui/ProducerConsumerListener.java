package com.core.producerconsumer.gui;

public interface ProducerConsumerListener<T> {
    void onProducerWaiting(int id);

    void onConsumerWaiting(int id);

    void onProducerResumed(int id);

    void onConsumerResumed(int id);

    void onProduce(int producerId, T item);

    void onConsume(int consumerId, T item);

    void logMessage(String message);
}
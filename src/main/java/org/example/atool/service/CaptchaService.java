package org.example.atool.service;

public interface CaptchaService {
    void send(String type, String target);
    void verify(String type, String target, String code);
}

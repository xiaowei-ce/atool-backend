package org.example.atool.service;

import org.example.atool.entity.vo.SkyGiftVO;

public interface SkyService {

    String data(String id);


    SkyGiftVO gift(String id);
}

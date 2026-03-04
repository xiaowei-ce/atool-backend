package org.example.atool.service;

import org.example.atool.entity.dto.LoginDTO;
import org.example.atool.entity.dto.RegisterDTO;
import org.example.atool.entity.vo.RecordVO;
import org.example.atool.entity.vo.UserDetailVO;

import java.util.List;

public interface UserService {
    void register(String type, RegisterDTO registerDTO);

    String login(LoginDTO loginDTO, String authorization);

    UserDetailVO details();

    List<RecordVO> pageGetRecords(Integer page, Integer size);

    void exchange(String key);

    Long lottery();
}

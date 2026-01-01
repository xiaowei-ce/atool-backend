package org.example.atool.service;

import org.example.atool.entity.dto.RegisterDTO;

public interface UserService {
    void register(String type, RegisterDTO registerDTO);
}

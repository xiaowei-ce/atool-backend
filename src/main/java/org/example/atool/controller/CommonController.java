package org.example.atool.controller;

import lombok.AllArgsConstructor;
import org.example.atool.entity.Result;
import org.example.atool.entity.po.Affiche;
import org.example.atool.service.impl.CommonServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/common")
@RestController
@AllArgsConstructor
public class CommonController {

    private final CommonServiceImpl commonServiceImpl;

    @GetMapping("/affiche")
    public Result affiche(){
       List<Affiche> affiches = commonServiceImpl.affiche(5);
       return Result.ok("success",affiches);
    }
}

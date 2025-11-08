package com.example.demo.controladores;


import com.example.demo.servicios.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/general")
public class GeneralController {


    @Autowired
    private GeneralService generalService;


}

package com.dzemianenka.controller;

import com.dzemianenka.model.GeffeInputModel;
import com.dzemianenka.service.GeffeServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/")
public class GeffeController {

    @PostMapping("/encrypt")
    public String encrypt(@ModelAttribute GeffeInputModel inputModel, Model model) throws IOException {
        GeffeServiceImpl geffeService = new GeffeServiceImpl(inputModel);
        model.addAttribute("modelOutput", geffeService.encrypt(inputModel));
        return "index";
    }
}

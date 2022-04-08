package com.dzemianenka.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class GeffeInputModel {

    private String reg1;
    private String reg2;
    private String reg3;
    private MultipartFile attachment;
}

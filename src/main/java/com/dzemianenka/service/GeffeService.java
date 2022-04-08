package com.dzemianenka.service;

import com.dzemianenka.model.GeffeInputModel;
import com.dzemianenka.model.GeffeOutputModel;

import java.io.IOException;

public interface GeffeService {

    GeffeOutputModel encrypt(GeffeInputModel inputModel) throws IOException;

    byte[] generateKey(GeffeInputModel inputModel) throws IOException;
}

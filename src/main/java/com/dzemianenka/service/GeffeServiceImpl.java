package com.dzemianenka.service;

import com.dzemianenka.exception.InvalidLengthException;
import com.dzemianenka.generator.LFSRGeneratorImpl;
import com.dzemianenka.model.GeffeInputModel;
import com.dzemianenka.model.GeffeOutputModel;

import java.io.IOException;

public class GeffeServiceImpl implements GeffeService {

    private static final int[] defaultPolynomial1 = {23, 5, 1};
    private static final int[] defaultPolynomial2 = {31, 3, 1};
    private static final int[] defaultPolynomial3 = {39, 4, 1};
    private final int[] polynomial1;
    private final int[] polynomial2;
    private final int[] polynomial3;
    long register1;
    long register2;
    long register3;

    public GeffeServiceImpl(GeffeInputModel model) {
        this(model.getReg1(), model.getReg2(), model.getReg3());
    }

    public GeffeServiceImpl(String initRegister1, String initRegister2, String initRegister3) {
        this(initRegister1, initRegister2, initRegister3, defaultPolynomial1, defaultPolynomial2, defaultPolynomial3);
    }

    public GeffeServiceImpl(String initRegister1, String initRegister2, String initRegister3, int[] polynomial1, int[] polynomial2, int[] polynomial3) {
        this.polynomial1 = polynomial1;
        this.polynomial2 = polynomial2;
        this.polynomial3 = polynomial3;
        if (initRegister1.length() > polynomial1[0]) {
            throw new InvalidLengthException(String.format("The length of the obtained register (%s) exceeds the" +
                    " maximum degree of the polynomial (%s)", initRegister1.length(), polynomial1[0])
            );
        }
        if (initRegister2.length() > polynomial2[0]) {
            throw new InvalidLengthException(String.format("The length of the obtained register (%s) exceeds the" +
                    " maximum degree of the polynomial (%s)", initRegister2.length(), polynomial2[0])
            );
        }
        if (initRegister3.length() > polynomial3[0]) {
            throw new InvalidLengthException(String.format("The length of the obtained register (%s) exceeds the" +
                    " maximum degree of the polynomial (%s)", initRegister3.length(), polynomial3[0])
            );
        }
        register1 = Long.parseLong(initRegister1, 2);
        register2 = Long.parseLong(initRegister2, 2);
        register3 = Long.parseLong(initRegister3, 2);
    }

    @Override
    public GeffeOutputModel encrypt(GeffeInputModel inputModel) throws IOException {
        GeffeOutputModel outputModel = new GeffeOutputModel();

        byte[] bytes = inputModel.getAttachment().getBytes();
        outputModel.setInput(getBitesString(bytes));

        byte[] key = generateKey(inputModel);
        outputModel.setKey(getBitesString(key));

        byte[] outputBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            outputBytes[i] = (byte) (bytes[i] ^ key[i]);
        }
        outputModel.setOutput(getBitesString(outputBytes));

        return outputModel;
    }

    private String getBitesString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return builder.toString();
    }

    @Override
    public byte[] generateKey(GeffeInputModel inputModel) throws IOException {
        int len = inputModel.getAttachment().getBytes().length;

        LFSRGeneratorImpl lfsr = new LFSRGeneratorImpl(inputModel.getReg1(), polynomial1);
        byte[] key1 = lfsr.generateKey(len);
        lfsr = new LFSRGeneratorImpl(inputModel.getReg2(), polynomial2);
        byte[] key2 = lfsr.generateKey(len);
        lfsr = new LFSRGeneratorImpl(inputModel.getReg3(), polynomial3);
        byte[] key3 = lfsr.generateKey(len);

        byte[] key = new byte[len];
        for (int i = 0; i < len; i++) {
            key[i] = (byte) ((byte) (key1[i] & key2[i]) | (~key1[i] & key3[i]));
        }
        return key;
    }
}

package com.dzemianenka.generator;

import com.dzemianenka.exception.InvalidLengthException;

public class LFSRGeneratorImpl implements LFSRGenerator {

    private final long register;
    private final int[] polynomial;

    private long currRegister;
    private long mask;

    public LFSRGeneratorImpl(String initRegister, int[] polynomial) {
        this.polynomial = polynomial;
        if (initRegister.length() > polynomial[0]) {
            throw new InvalidLengthException(String.format("The length of the obtained register (%s) exceeds the" +
                    " maximum degree of the polynomial (%s)", initRegister.length(), polynomial[0])
            );
        }
        register = Long.parseLong(initRegister, 2);
        generateMask();
    }

    @Override
    public byte[] generateKey(int len) {
        currRegister = register;
        byte[] key = new byte[len];
        for (int i = 0; i < key.length; i++) {
            for (int j = 0; j < 8; j++) {

                byte abortedBit = getBitAtPos(polynomial[0]);
                key[i] = (byte) (key[i] | (abortedBit << (8 - j - 1)));

                byte newFirstBit = abortedBit;
                for (int k = 1; k < polynomial.length; k++) {
                    newFirstBit ^= getBitAtPos(polynomial[k]);
                }
                currRegister = (currRegister << 1) & mask;
                currRegister = currRegister | newFirstBit;
            }
        }
        return key;
    }

    private byte getBitAtPos(int pos) {
        return (byte) ((byte) (currRegister >> pos - 1) & 1);
    }

    private void generateMask() {
        mask = Long.parseLong("1".repeat(Math.max(0, polynomial[0])), 2);
    }
}

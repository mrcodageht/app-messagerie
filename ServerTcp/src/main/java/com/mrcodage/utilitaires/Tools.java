package com.mrcodage.utilitaires;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public interface Tools {
    static byte[] getByteSaltEncodeBase64(String saltBase64){
        return Base64.getDecoder().decode(saltBase64);
    }
    static String getStringFromByteEncodeBase64(byte[] byteToDecode){
        return DatatypeConverter.printBase64Binary(byteToDecode);
    }
}

package com.mrcodage.utilitaires;

import java.util.Base64;

public interface Tools {
    static byte[] getByteSaltEncodeBase64(String saltBase64){
        return Base64.getDecoder().decode(saltBase64);
    }
}

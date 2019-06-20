package org.fisco.bcos.LAGCreditSDK;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.crypto.ECKeyPair;
import org.fisco.bcos.web3j.crypto.Keys;
import org.fisco.bcos.web3j.utils.Numeric;


@Slf4j
public class UserKryUtils {
    static final int PUBLIC_KEY_SIZE = 64;
    static final int PUBLIC_KEY_LENGTH_IN_HEX = PUBLIC_KEY_SIZE << 1;

    public static void createUserKey() {
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair();
            String publicKey = Numeric.toHexStringWithPrefixZeroPadded(keyPair.getPublicKey(), PUBLIC_KEY_LENGTH_IN_HEX);
            String privateKey = Numeric.toHexStringNoPrefix(keyPair.getPrivateKey());
            String address = "0x" + Keys.getAddress(publicKey);

            log.info("public Key: {}",publicKey);
            log.info("private Key: {}", privateKey);
            log.info("address: {}", address);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

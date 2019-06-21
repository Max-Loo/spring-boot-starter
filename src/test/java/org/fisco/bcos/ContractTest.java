package org.fisco.bcos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.fisco.bcos.constants.GasConstants;
import org.fisco.bcos.temp.HelloWorld;
import org.fisco.bcos.temp.LAGCredit;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

public class ContractTest extends BaseTest {

    @Autowired private Web3j web3j;
    @Autowired private Credentials credentials;

//    @Test
//    public void deployAndCallHelloWorld() throws Exception {
//        // deploy contract
//        HelloWorld helloWorld =
//                HelloWorld.deploy(
//                                web3j,
//                                credentials,
//                                new StaticGasProvider(
//                                        GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT))
//                        .send();
//        if (helloWorld != null) {
//            System.out.println("HelloWorld address is: " + helloWorld.getContractAddress());
//            // call set function
//            helloWorld.set("Hello, World!").send();
//            // call get function
//            String result = helloWorld.get().send();
//            System.out.println(result);
//            assertTrue("Hello, World!".equals(result));
//        }
//    }

    @Test
    public  void deployAndCallLAGCredit() throws  Exception {
        // target address(generate by get_account.sh)
        String targetAddress = "0x0c1f490580a6161d605660fd0081e4fd9206ba74";

        //deploy contract
        LAGCredit lagCredit = LAGCredit.deploy(
                web3j,
                credentials,
                new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT),
                new BigInteger("10000"), "LAGC", "LAG"
        ).send();

        if (lagCredit != null) {
            // print the address
            System.out.println("LAGCredit address is: " + lagCredit.getContractAddress());
            System.out.println("My address is: " + lagCredit.getMyAddress().send());

            // call getTotalSupply function
            long total = lagCredit.getTotalSupply().send().longValue();
            System.out.println("Total supply is: " + total);
            assertEquals(10000, total);

            // get my balance
            long myBalance = lagCredit.balanceOfMine().send().longValue();
            System.out.println("My balance is: " + myBalance);
            assertEquals(10000, myBalance);

            // get balance of target address
            long targetBalance = lagCredit.balanceOf(targetAddress).send().longValue();
            System.out.println("Target balance is: " + targetBalance);
            assertEquals(0, targetBalance);

            // transfer 100 to target address
            System.out.println("Transfer 100 to " + targetAddress);
            TransactionReceipt receipt = lagCredit.transfer(targetAddress, new BigInteger("100")).send();
            System.out.println("Receipt status: " + receipt.getStatus());

            // get balance of target address again
            targetBalance = lagCredit.balanceOf(targetAddress).send().longValue();
            System.out.println("After transfer, target balance is: " + targetBalance);
            assertEquals(100, targetBalance);

            // get my balance again
            myBalance = lagCredit.balanceOfMine().send().longValue();
            System.out.println("After transfer, my balance is: " + myBalance);
            assertEquals(9900, myBalance);

        }
    }
}

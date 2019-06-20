package org.fisco.bcos.LAGCreditSDK;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.autoconfigure.CredentialsConfig;
import org.fisco.bcos.autoconfigure.GroupChannelConnectionsPropertyConfig;
import org.fisco.bcos.autoconfigure.ServiceConfig;
import org.fisco.bcos.autoconfigure.Web3jConfig;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.channel.handler.GroupChannelConnectionsConfig;
import org.fisco.bcos.constants.GasConstants;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

@Slf4j
public class LAGCreditSDK {
    private ServiceConfig serviceConfig;
    private Web3jConfig web3jConfig;
    private GroupChannelConnectionsPropertyConfig groupChannelConnectionsPropertyConfig;
    private CredentialsConfig credentialsConfig;

    private GroupChannelConnectionsConfig groupChannelConnectionsConfig;
    private Service service;
    private Web3j web3j;
    private Credentials credentials;


    // Constructor
    public LAGCreditSDK() {
        try {
            serviceConfig = new ServiceConfig();
            web3jConfig = new Web3jConfig();
            groupChannelConnectionsPropertyConfig = new GroupChannelConnectionsPropertyConfig();
            credentialsConfig = new CredentialsConfig();

            groupChannelConnectionsConfig = groupChannelConnectionsPropertyConfig.getGroupChannelConnections();
            service = serviceConfig.getService(groupChannelConnectionsConfig);
            web3j = web3jConfig.getWeb3j(service);         // Exception
            credentials = credentialsConfig.getCredentials();

        } catch (Exception e) {
            log.error("build lagcredit fail: {}",e.getMessage());
        }
    }

    // deploy a new LAGCredit contract
    public LAGCredit deploy() {
        LAGCredit lagcredit = null;
        try {
            lagcredit.deploy(web3j, credentials, new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT), new BigInteger("100000"), "LAGC", "LAG").send();
            log.info("LAGC address is {}", lagcredit.getContractAddress());
            return lagcredit;
        } catch (Exception e) {
            log.error("deploy lagc contract fail: {}", e.getMessage());
        }
        return lagcredit;
    }

    // get LAGCredit contract
    public LAGCredit load(String creditAddress) {
        LAGCredit lagCredit = LAGCredit.load(creditAddress, web3j, credentials, new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT));
        return  lagCredit;
    }

    // transfer
    public boolean transfer(String creditAddress, String to, BigInteger value) {
        try {
            LAGCredit lagCredit = load(creditAddress);
            TransactionReceipt receipt = lagCredit.transfer(to, value).send();
            log.info("status: {}", receipt.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public long getBalanceByOwner(String creditAddress, String owner) throws Exception {
        LAGCredit lagCredit = load(creditAddress);
        BigInteger balance = lagCredit.balanceOf(owner).send();
        return balance.longValue();
    }

    public long getBalanceOfMine(String creditAddress) throws Exception {
        LAGCredit lagCredit = load(creditAddress);
        BigInteger balance = lagCredit.balanceOfMine().send();
        return balance.longValue();
    }

    public long getTotalSupply(String creditAddress) throws Exception {
        LAGCredit lagCredit = load(creditAddress);
        BigInteger total = lagCredit.getTotalSupply().send();
        return total.longValue();
    }

    public String getMyAddress(String creditAddress) throws Exception {
        LAGCredit lagCredit = load(creditAddress);
        String address = lagCredit.getMyAddress().send();
        return address;
    }

}

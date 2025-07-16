package com.omvaultchain.blockchain.config;

import com.omvaultchain.blockchain.contracts.VersionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

@Configuration
public class BlockchainConfig {

    @Value("${blockchain.wallet.private-key}")
    private String privateKey;

    @Value("${spring.web3j.client-address}")
    private String web3jClientAddress;

    @Value("${contract.address.version-manager}")
    private String contractAddress;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(web3jClientAddress));
    }

    @Bean
    public Credentials credentials() {
        return Credentials.create(privateKey);
    }

    @Bean
    public ContractGasProvider contractGasProvider() {return new DefaultGasProvider();}

    @Bean
    public VersionManager versionManager(Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {return VersionManager.load(contractAddress, web3j, credentials, gasProvider);}
}

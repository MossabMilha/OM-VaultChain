package com.omvaultchain.blockchain.config;

import com.omvaultchain.blockchain.contracts.VersionManager;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.util.concurrent.TimeUnit;

@Configuration
public class BlockchainConfig {

    @Value("${blockchain.wallet.private-key}")
    private String privateKey;

    @Value("${spring.web3j.client-address}")
    private String web3jClientAddress;

    @Value("${contract.address.version-manager}")
    private String contractAddress;

    // Timeout configuration properties with default values
    @Value("${blockchain.http.connection-timeout:60}")
    private int connectionTimeoutSeconds;

    @Value("${blockchain.http.read-timeout:60}")
    private int readTimeoutSeconds;

    @Value("${blockchain.http.write-timeout:60}")
    private int writeTimeoutSeconds;

    @Bean
    public Web3j web3j() {
        // Create custom OkHttpClient with increased timeouts
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(connectionTimeoutSeconds, TimeUnit.SECONDS)
                .readTimeout(readTimeoutSeconds, TimeUnit.SECONDS)
                .writeTimeout(writeTimeoutSeconds, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        OkHttpClient httpClient = httpClientBuilder.build();
        HttpService httpService = new HttpService(web3jClientAddress, httpClient);

        return Web3j.build(httpService);
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

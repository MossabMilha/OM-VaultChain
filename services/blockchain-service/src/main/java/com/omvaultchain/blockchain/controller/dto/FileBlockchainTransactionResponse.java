package com.omvaultchain.blockchain.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileBlockchainTransactionResponse {
    private String transactionHash;
    private long blockNumber;
    private String status;
    private Integer chainId;
}

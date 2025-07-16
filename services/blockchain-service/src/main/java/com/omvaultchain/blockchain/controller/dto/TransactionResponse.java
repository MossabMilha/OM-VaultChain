package com.omvaultchain.blockchain.controller.dto;

import lombok.Data;

@Data
public class TransactionResponse {
    private String transactionHash;
    public TransactionResponse(String transactionHash) {this.transactionHash = transactionHash;}
}

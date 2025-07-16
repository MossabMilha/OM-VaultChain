package com.omvaultchain.blockchain.controller.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GrantMultipleAccessRequest {
    private String cid;
    private List<Map<String,String>> userId;
}

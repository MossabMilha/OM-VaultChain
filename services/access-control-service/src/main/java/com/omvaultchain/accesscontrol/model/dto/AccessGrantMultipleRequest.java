package com.omvaultchain.accesscontrol.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AccessGrantMultipleRequest {
    @NotBlank(message = "FileId Is Required")
    private String fileId;

    @NotEmpty(message = "Users List Cannot Be Empty")
    private List<UserAccessItem> users;

    @Data
    public static class UserAccessItem{
        @NotBlank(message = "UserId Is Required")
        private String userId;

        @NotBlank(message = "EncryptedKey Is Required")
        private String encryptedKey;
    }

}

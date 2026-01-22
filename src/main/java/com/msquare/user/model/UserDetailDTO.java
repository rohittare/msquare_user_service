package com.msquare.user.model;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDetailDTO {
    private UUID userId;
    private String fullName;
    private String email;
    private String phone;
    private String userProfilePicture;
    private String role;
    private Boolean asctive;
    private List<AddressDTO> addresses;
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();
    
}

package com.company.SkillSyncAI.dtos;

import com.company.SkillSyncAI.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {

    private String name;
    private String email;
    private String password;
    private String role;
}

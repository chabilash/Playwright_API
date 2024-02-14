package com.api.java;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.testng.annotations.CustomAttribute;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User_Lombok {
    private String id;
    private String name;
    private String email;
    private String gender;
    private String status;

}

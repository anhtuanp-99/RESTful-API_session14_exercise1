package com.tuan.employee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO trả về cho client sau khi đăng nhập thành công.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private String accessToken; // Chuỗi JWT
    private String tokenType = "Bearer"; // Theo chuẩn Bearer Token
    private String username;
    private String role;
}

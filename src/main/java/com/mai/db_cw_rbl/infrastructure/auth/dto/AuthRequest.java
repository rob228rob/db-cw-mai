package com.mai.db_cw_rbl.infrastructure.auth.dto;

public record AuthRequest(String username, char[] password) {

    public String getMaskPassword() {
        return "*".repeat(password.length);
    }

}

package com.beyond.basic.b2_board.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonErrorDTO {
    private int status_code;
    private String status_message;
}

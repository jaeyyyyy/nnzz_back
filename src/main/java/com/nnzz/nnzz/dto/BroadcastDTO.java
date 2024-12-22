package com.nnzz.nnzz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Date;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "방송 출연 정보")
public class BroadcastDTO {
    @Schema(description = "출연한 방송 이름")
    private String broadcastName;
    @Schema(description = "출연한 방송 회차")
    private String episode;
    @Schema(description = "출연한 방송 일자")
    private Date broadcastDate;
    @Schema(description = "출연한 방송 주제")
    private String topic;
}

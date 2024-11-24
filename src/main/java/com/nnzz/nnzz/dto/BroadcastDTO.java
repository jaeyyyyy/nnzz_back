package com.nnzz.nnzz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Date;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BroadcastDTO {
    private String broadcastName;
    private String episode;
    private Date broadcastDate;
    private String topic;
}

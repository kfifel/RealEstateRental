package com.fil.rouge.web.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentStatisticsResponse {

    private Long totalRent;
    private Long totalPendingRent;
    private Long totalOngoingRent;
    private Long totalApprovedRent;
    private Long totalRejectedRent;
    private Long totalCanceledRent;
    private Long totalFinishedRent;
}

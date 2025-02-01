package org.blueline.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveUsersDto {

    private int totalActiveUsers;

    private Map<Timestamp, Integer> activeUsersPerPeriod;
}

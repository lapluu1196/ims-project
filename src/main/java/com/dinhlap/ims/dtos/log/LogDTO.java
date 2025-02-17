package com.dinhlap.ims.dtos.log;

import com.dinhlap.ims.dtos.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TimeZoneColumn;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogDTO {
    
    private Long logId;

    private String action;
    
    private UserDTO user;
    
    private String entityType;
    
    private Integer entityId;
   
    private String description;

    @TimeZoneColumn
    private LocalDateTime timestamp = LocalDateTime.now();
}

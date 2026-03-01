package org.example.atool.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class RecordVO {
    private Long id;
    private Long change;
    private String abstr;
    private Timestamp time;
    private String detail;
}

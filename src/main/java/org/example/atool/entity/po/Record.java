package org.example.atool.entity.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@ToString
public class Record {
    private Long id;
    private Long userId;
    private Long typeId;
    private String abstr;
    private Timestamp time;
    private String detail;
    private Long change;
}

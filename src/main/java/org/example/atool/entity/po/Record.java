package org.example.atool.entity.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@ToString
public class Record {

    public final static Long BONUS = 3L, QUERY = 1L, RECHARGE = 2L;

    private Long id;
    private Long userId;
    private Long typeId;
    private String abstr;
    private Timestamp time;
    private String detail;
    private Long change;
}

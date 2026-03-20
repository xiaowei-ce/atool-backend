package org.example.atool.entity.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class Affiche {

    public static final String INFO = "info", DANGER = "danger", SUCCESS = "success", WARNING = "warning";

    private String tag;
    private String type;
    private String tittle;
    private Timestamp time;
    private String description;
}

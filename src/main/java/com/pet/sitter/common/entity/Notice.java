package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no_no;

    @Column(length = 45)
    @NotNull
    private String no_tilte;

    @Column(columnDefinition = "text")
    @NotNull
    private String no_content;

    @Column
    @NotNull
    private LocalDateTime no_date;

    @Column
    @NotNull
    @ColumnDefault("0")
    private Integer no_view_cnt;

    @Column(length = 255)
    private String no_file;
}

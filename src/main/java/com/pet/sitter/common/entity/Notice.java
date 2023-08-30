package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noNo;

    @Column(length = 45)
    @NotNull
    private String noTitle;

    @Column(columnDefinition = "text")
    @NotNull
    private String noContent;

    @Column
    @NotNull
    private LocalDateTime noDate;

    @Column
    @NotNull
    @ColumnDefault("0")
    private Integer noViewCnt;

}

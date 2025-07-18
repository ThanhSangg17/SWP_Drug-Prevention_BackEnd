package com.swp.drugprevention.backend.io.response;

import com.swp.drugprevention.backend.model.Consultant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class  OfflineCourseResponse {

    private Long id;
    private String tenKhoaHoc;
    private Consultant consultant;
    private Double giaTien;
    private String diaDiem;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;

    private Integer soLuongToiDa;
}

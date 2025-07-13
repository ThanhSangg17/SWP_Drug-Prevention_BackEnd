package com.swp.drugprevention.backend.io.response;

import com.swp.drugprevention.backend.model.Consultant;

import java.time.LocalDateTime;

public class UserEnrolledCourseResponse {
    private Long courseId;
    private String tenKhoaHoc;
    private String diaDiem;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;
    private Consultant consultant;

    public UserEnrolledCourseResponse() {}

    public UserEnrolledCourseResponse(Long courseId, String tenKhoaHoc, String diaDiem,
                                      LocalDateTime thoiGianBatDau, LocalDateTime thoiGianKetThuc, Consultant consultant) {
        this.courseId = courseId;
        this.tenKhoaHoc = tenKhoaHoc;
        this.diaDiem = diaDiem;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
        this.consultant = consultant;
    }

    // Getters & Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getTenKhoaHoc() {
        return tenKhoaHoc;
    }

    public void setTenKhoaHoc(String tenKhoaHoc) {
        this.tenKhoaHoc = tenKhoaHoc;
    }

    public String getDiaDiem() {
        return diaDiem;
    }

    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
    }

    public LocalDateTime getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public void setThoiGianBatDau(LocalDateTime thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }

    public LocalDateTime getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public void setThoiGianKetThuc(LocalDateTime thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    public Consultant getConsultant() {
        return consultant;
    }

    public void setConsultant(Consultant consultant) {
        this.consultant = consultant;
    }
}

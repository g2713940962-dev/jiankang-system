package com.jiankang.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiankang.dto.CheckRecordDTO;
import com.jiankang.entity.CheckRecord;

import java.time.LocalDate;
import java.util.List;

/**
 * 打卡记录服务
 */
public interface CheckRecordService {

    void add(CheckRecordDTO dto);

    void update(Long id, CheckRecordDTO dto);

    void delete(Long id);

    Page<CheckRecord> page(Integer page, Integer size, String checkType, LocalDate startDate, LocalDate endDate, Long userId);

    List<CheckRecord> today();
}

package com.jiankang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiankang.common.ResultCode;
import com.jiankang.common.exception.BusinessException;
import com.jiankang.common.util.UserContext;
import com.jiankang.dto.CheckRecordDTO;
import com.jiankang.entity.CheckRecord;
import com.jiankang.mapper.CheckRecordMapper;
import com.jiankang.service.CheckRecordService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * 打卡记录服务实现
 */
@Service
public class CheckRecordServiceImpl implements CheckRecordService {

    @Resource
    private CheckRecordMapper checkRecordMapper;

    @Override
    public void add(CheckRecordDTO dto) {
        CheckRecord record = build(dto);
        record.setUserId(UserContext.getUserId());
        checkRecordMapper.insert(record);
    }

    @Override
    public void update(Long id, CheckRecordDTO dto) {
        CheckRecord record = checkRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        checkOwnership(record);
        CheckRecord update = build(dto);
        update.setId(id);
        update.setUserId(record.getUserId());
        checkRecordMapper.updateById(update);
    }

    @Override
    public void delete(Long id) {
        CheckRecord record = checkRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        checkOwnership(record);
        checkRecordMapper.deleteById(id);
    }

    @Override
    public Page<CheckRecord> page(Integer page, Integer size, String checkType, LocalDate startDate, LocalDate endDate, Long userId) {
        Page<CheckRecord> p = new Page<>(page, size);
        QueryWrapper<CheckRecord> qw = new QueryWrapper<>();
        // 管理员可查询指定用户，普通用户只能查看自己
        if (UserContext.isAdmin() && userId != null) {
            qw.eq("user_id", userId);
        } else {
            qw.eq("user_id", UserContext.getUserId());
        }
        if (StringUtils.hasText(checkType)) {
            qw.eq("check_type", checkType);
        }
        if (startDate != null) {
            qw.ge("check_date", startDate);
        }
        if (endDate != null) {
            qw.le("check_date", endDate);
        }
        qw.orderByDesc("check_date").orderByDesc("id");
        return checkRecordMapper.selectPage(p, qw);
    }

    @Override
    public List<CheckRecord> today() {
        QueryWrapper<CheckRecord> qw = new QueryWrapper<>();
        qw.eq("user_id", UserContext.getUserId());
        qw.eq("check_date", LocalDate.now());
        qw.orderByDesc("id");
        return checkRecordMapper.selectList(qw);
    }

    private CheckRecord build(CheckRecordDTO dto) {
        CheckRecord record = new CheckRecord();
        record.setCheckDate(dto.getCheckDate());
        record.setCheckType(dto.getCheckType());
        record.setDuration(dto.getDuration());
        record.setCalories(dto.getCalories() == null ? 0 : dto.getCalories());
        record.setStepCount(dto.getStepCount() == null ? 0 : dto.getStepCount());
        record.setRemark(dto.getRemark());
        return record;
    }

    private void checkOwnership(CheckRecord record) {
        if (!UserContext.isAdmin() && !record.getUserId().equals(UserContext.getUserId())) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }
}

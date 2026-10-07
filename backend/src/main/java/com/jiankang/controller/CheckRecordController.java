package com.jiankang.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiankang.common.Result;
import com.jiankang.dto.CheckRecordDTO;
import com.jiankang.entity.CheckRecord;
import com.jiankang.service.CheckRecordService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * 打卡记录接口
 */
@RestController
@RequestMapping("/api/check")
public class CheckRecordController {

    @Resource
    private CheckRecordService checkRecordService;

    @PostMapping
    public Result<Void> add(@Valid @RequestBody CheckRecordDTO dto) {
        checkRecordService.add(dto);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody CheckRecordDTO dto) {
        checkRecordService.update(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        checkRecordService.delete(id);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<Page<CheckRecord>> page(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          @RequestParam(required = false) String checkType,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                          @RequestParam(required = false) Long userId) {
        return Result.success(checkRecordService.page(page, size, checkType, startDate, endDate, userId));
    }

    @GetMapping("/today")
    public Result<List<CheckRecord>> today() {
        return Result.success(checkRecordService.today());
    }
}

package com.fengwenyi.learnmybatisplus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fengwenyi.javalib.util.StringUtil;
import com.fengwenyi.learnmybatisplus.business.AppBusiness;
import com.fengwenyi.learnmybatisplus.enums.GenderEnum;
import com.fengwenyi.learnmybatisplus.model.City;
import com.fengwenyi.learnmybatisplus.model.Idcard;
import com.fengwenyi.learnmybatisplus.model.Student;
import com.fengwenyi.learnmybatisplus.service.MPCityService;
import com.fengwenyi.learnmybatisplus.service.MPStudentService;
import io.swagger.annotations.Api;
import net.iutil.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 测试
 *
 * @author Wenyi Feng
 * @since 2018-09-01
 */
@RestController
@RequestMapping(value = "/app", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags = "App 测试示例")
public class AppController {

    @Autowired
    private MPCityService mpCityService;

    @Autowired
    private AppBusiness appBusiness;

    @Autowired
    private MPStudentService mpStudentService;

    // 查询所有城市
    @GetMapping("/queryCityAll")
    public ApiResult queryCityAll() {
        List<City> cities = mpCityService.queryCityAll();
        return ApiResult.success(cities);
    }


    // 添加城市
    @PostMapping("/addCity")
    public ApiResult addCity(String name) {
        if (StringUtil.isEmpty(name))
            return ApiResult.error();
        boolean rs = mpCityService.addCity(new City().setName(name));
        if (rs)
            return ApiResult.success();
        return ApiResult.error();
    }

    // 添加学生
    @PostMapping("/addStudent")
    public ApiResult addStudent(String name, Integer age, String gender, String info, String idCardCode, String cityName) {

        // 检验参数
        if (StringUtil.isEmpty(name)
                || age == null
                || StringUtil.isEmpty(gender)
                || StringUtil.isEmpty(info)
                || StringUtil.isEmpty(idCardCode)
                || StringUtil.isEmpty(cityName))
            return ApiResult.error();

        // 获取GenderEnum
        GenderEnum genderEnum = GenderEnum.getEnumByDesc(gender);

        // student
        Student student = new Student()
                .setName(name)
                .setAge(age)
                .setGender(genderEnum)
                .setInfo(info);

        // city
        City city = new City().setName(cityName);

        // idCard
        Idcard idcard = new Idcard().setCode(idCardCode);

        // service
        boolean rs = appBusiness.addStudent(student, city, idcard);
        if (rs)
            return ApiResult.success();
        return ApiResult.error();
    }

    // 分页查询学生
    @GetMapping("/queryStudentByPage/{currentPage}")
    public ApiResult queryStudentByPage(@PathVariable("currentPage") Long currentPage) {
        if (currentPage == null)
            return ApiResult.error();
        IPage<Student> studentIPage = mpStudentService.queryStudentByPage(currentPage);
        return ApiResult.success(studentIPage);
    }
}

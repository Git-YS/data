package com.hrms.data.controller;

import com.hrms.common.Response;
import com.hrms.data.bean.ExcelCell;
import com.hrms.data.bean.ExcelError;
import com.hrms.data.bean.ExcelQuery;
import com.hrms.data.service.IDataService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


@Controller
public class DataController {
    @Autowired
    private IDataService dataService;

    @PostMapping("upload")
    @ResponseBody
    public Response upload(MultipartFile file, ExcelQuery query) {
        //1、读取excel，返回sheet列表
        try {
            Workbook sheets = WorkbookFactory.create(file.getInputStream());
            return Response.ok(dataService.getSheets(sheets, query));
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            return Response.error();
        }
    }

    @PostMapping("import")
    @ResponseBody
    public Response importTest(@RequestBody ExcelQuery query) {
        return query.getFirst() > 1 ? Response.error(Collections.singletonList(ExcelError.create(1,1,"这个错误是用来测试的^.^"))) : Response.ok();
    }
}

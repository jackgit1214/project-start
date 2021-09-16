package com.project.resources;

import com.project.api.LogApi;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.model.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class LogController implements LogApi {

    @Override
    public ResponseEntity<Void> deleteLog(String logId, String apiKey) {
        log.info("execute deleteLog.........................");
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteLogs(String[] logIds, String apiKey) {
        log.info("execute deleteLogs.........................");
        return null;
    }

    @Override
    public ResponseEntity<List<SysLog>> findLogs(QueryModel queryModel, int page, int limit) {
        log.info("execute findLogs.........................");
        return null;
    }

    @Override
    public ResponseEntity<List<SysLog>> findLog(String logId, String apiKey) {
        log.info("execute findLog.........................");
        return null;
    }

}

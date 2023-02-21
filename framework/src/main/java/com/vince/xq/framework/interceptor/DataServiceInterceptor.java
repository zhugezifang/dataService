package com.vince.xq.framework.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.vince.xq.common.core.domain.AjaxResult;
import com.vince.xq.system.domain.ApiParam;
import com.vince.xq.system.service.IApiConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;

@Component
public class DataServiceInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(DataServiceInterceptor.class);

    @Autowired
    private IApiConfigService apiConfigService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getServletPath();
        String method = request.getMethod();
        log.info("======DataServiceInterceptor path:{},method:{}=======", path, method);
        if (path.contains("dataService")) {
            printData(request, response);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    private void printData(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<ApiParam> apiParamList = new ArrayList<>();
        Map<String, String[]> map = request.getParameterMap();
        Set<Map.Entry<String, String[]>> keys = map.entrySet();
        Iterator<Map.Entry<String, String[]>> it = keys.iterator();
        String path = request.getServletPath();
        log.info("======printData path:{}=======", path);
        String apiName = path.split("/")[2];
        //path.split("dataService")[1].split("\\\\/");
        while (it.hasNext()) {
            Map.Entry<String, String[]> itMap = it.next();
            ApiParam apiParam = new ApiParam();
            apiParam.setName(itMap.getKey());
            apiParam.setValue(StringUtils.join(itMap.getValue(), ","));
            apiParamList.add(apiParam);
            //log.info("参数--" + itMap.getKey() + ":" + Arrays.toString(itMap.getValue()));
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        log.info("=========apiName:{},apiParamList:{}=============", apiName, JSONObject.toJSONString(apiParamList));
        long startTime = System.currentTimeMillis();
        AjaxResult.Response result = apiConfigService.runApiByType(apiName, apiParamList, request.getMethod(), startTime);
        long costTime = System.currentTimeMillis() - startTime;
        log.info("costTime:{}", costTime);
        String res = JSONObject.toJSONString(result);
        try {
            out = response.getWriter();
            out.append(res);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

}

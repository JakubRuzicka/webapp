package org.literacyapp.rest.admin;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.literacyapp.dao.ApplicationDao;
import org.literacyapp.dao.ApplicationVersionDao;
import org.literacyapp.model.admin.Application;
import org.literacyapp.model.admin.ApplicationVersion;
import org.literacyapp.model.enums.Locale;
import org.literacyapp.model.enums.admin.ApplicationStatus;
import org.literacyapp.model.json.admin.ApplicationJson;
import org.literacyapp.model.json.admin.ApplicationVersionJson;
import org.literacyapp.rest.JavaToJsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/admin/application")
public class ApplicationRestController {
    
    private Logger logger = Logger.getLogger(getClass());
    
    @Autowired
    private ApplicationDao applicationDao;
    
    @Autowired
    private ApplicationVersionDao applicationVersionDao;
    
    @RequestMapping("/list")
    public List<ApplicationJson> list(
            HttpServletRequest request,
            @RequestParam String deviceId,
            // TODO: checksum
            @RequestParam Locale locale,
            @RequestParam String deviceModel,
            @RequestParam Integer osVersion,
            @RequestParam Integer appVersionCode
    ) {
        logger.info("list");
        
        logger.info("deviceId: " + deviceId);
//        logger.info("checksum: " + checksum);
        logger.info("locale: " + locale);
        logger.info("deviceModel: " + deviceModel);
        logger.info("osVersion: " + osVersion);
        logger.info("appVersionCode: " + appVersionCode);
        
        List<ApplicationJson> applicationJsons = new ArrayList<>();
        for (Application application : applicationDao.readAllByStatus(locale, ApplicationStatus.ACTIVE)) {
            ApplicationJson applicationJson = JavaToJsonConverter.getApplicationJson(application);
            
            List<ApplicationVersion> applicationVersions = applicationVersionDao.readAll(application);
            List<ApplicationVersionJson> applicationVersionList = new ArrayList<>();
            for (ApplicationVersion applicationVersion : applicationVersions) {
                ApplicationVersionJson applicationVersionJson = JavaToJsonConverter.getApplicationVersionJson(applicationVersion);
                applicationVersionList.add(applicationVersionJson);
            }
            applicationJson.setApplicationVersionList(applicationVersionList);
            
            applicationJsons.add(applicationJson);
        }
        return applicationJsons;
    }
}
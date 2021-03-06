package ai.elimu.web.project.app;

import ai.elimu.dao.project.AppCategoryDao;
import ai.elimu.dao.project.AppGroupDao;
import java.io.IOException;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import ai.elimu.dao.ApplicationDao;
import ai.elimu.dao.ApplicationVersionDao;
import ai.elimu.dao.project.ProjectApplicationDao;
import ai.elimu.dao.project.ProjectDao;
import ai.elimu.model.admin.Application;
import ai.elimu.model.admin.ApplicationVersion;
import ai.elimu.model.Contributor;
import ai.elimu.model.enums.Environment;
import ai.elimu.model.enums.admin.ApplicationStatus;
import ai.elimu.model.enums.admin.ApplicationVersionStatus;
import ai.elimu.model.project.AppCategory;
import ai.elimu.model.project.AppGroup;
import ai.elimu.model.project.Project;
import ai.elimu.rest.service.project.ProjectJsonService;
import ai.elimu.util.ChecksumHelper;
import ai.elimu.util.Mailer;
import ai.elimu.util.SlackApiHelper;
import ai.elimu.web.context.EnvironmentContextLoaderListener;
import java.net.URLEncoder;
import java.util.List;
import net.dongliu.apk.parser.ByteArrayApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

/**
 * This controller handles both creation of new {@link Application}s as well as updates of existing {@link Application}s.
 */
@Controller
@RequestMapping("/project/{projectId}/app-category/{appCategoryId}/app-group/{appGroupId}/app/create")
public class AppCreateController {
    
    private final Logger logger = Logger.getLogger(getClass());
    
    @Autowired
    private ProjectJsonService projectJsonService;
    
    @Autowired
    private ProjectDao projectDao;
    
    @Autowired
    private AppCategoryDao appCategoryDao;
    
    @Autowired
    private AppGroupDao appGroupDao;
    
    @Autowired
    private ApplicationDao applicationDao;
    
    @Autowired
    private ProjectApplicationDao projectApplicationDao;
    
    @Autowired
    private ApplicationVersionDao applicationVersionDao;

    @RequestMapping(method = RequestMethod.GET)
    public String handleRequest(
            @PathVariable Long projectId,
            @PathVariable Long appCategoryId,
            @PathVariable Long appGroupId,
            @RequestParam(required = false) Long applicationId,
            Model model
    ) {
    	logger.info("handleRequest");
        
        Project project = projectDao.read(projectId);
        model.addAttribute("project", project);
        
        AppCategory appCategory = appCategoryDao.read(appCategoryId);
        model.addAttribute("appCategory", appCategory);
        
        AppGroup appGroup = appGroupDao.read(appGroupId);
        model.addAttribute("appGroup", appGroup);
        
        ApplicationVersion applicationVersion = new ApplicationVersion();
        
        logger.info("applicationId: " + applicationId);
        if (applicationId != null) {
            Application application = applicationDao.read(applicationId);
            applicationVersion.setApplication(application);
        }
        
        model.addAttribute("applicationVersion", applicationVersion);

        return "project/app/create";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String handleSubmit(
            ApplicationVersion applicationVersion,
            @RequestParam("bytes") MultipartFile multipartFile,
            BindingResult result,
            Model model,
            HttpSession session,
            @PathVariable Long appGroupId,
            @PathVariable Long projectId,
            @PathVariable Long appCategoryId
    ) {
    	logger.info("handleSubmit");
        
        Project project = projectDao.read(projectId);
        AppCategory appCategory = appCategoryDao.read(appCategoryId);
        AppGroup appGroup = appGroupDao.read(appGroupId);
        
        boolean isUpdateOfExistingApplication = applicationVersion.getApplication() != null;
        logger.info("isUpdateOfExistingApplication: " + isUpdateOfExistingApplication);
        
        Contributor contributor = (Contributor) session.getAttribute("contributor");
        
        String packageName = null;
        
        if (multipartFile.isEmpty()) {
            result.rejectValue("bytes", "NotNull");
        } else {
            try {
                byte[] bytes = multipartFile.getBytes();
                
                Integer fileSizeInKb = bytes.length / 1024;
                logger.info("fileSizeInKb: " + fileSizeInKb + " (" + (fileSizeInKb / 1024) + "MB)");
                
                String contentType = multipartFile.getContentType();
                logger.info("contentType: " + contentType);
                
                // Auto-detect applicationId, versionCode, etc.
                ByteArrayApkFile byteArrayApkFile = new ByteArrayApkFile(bytes);
                ApkMeta apkMeta = byteArrayApkFile.getApkMeta();
                
                packageName = apkMeta.getPackageName();
                logger.info("packageName: " + packageName);
                
                String label = apkMeta.getLabel();
                logger.info("label: " + label);
                
                byte[] icon = byteArrayApkFile.getIconFile().getData();
                logger.info("icon.length: " + (icon.length / 1024) + "kB");
                
                Integer versionCode = apkMeta.getVersionCode().intValue();
                logger.info("versionCode: " + versionCode);
                
                String versionName = apkMeta.getVersionName();
                logger.info("versionName: " + versionName);
                
                Integer minSdkVersion = Integer.valueOf(apkMeta.getMinSdkVersion());
                logger.info("minSdkVersion: " + minSdkVersion);
                
                // Check if Application already exists in the same AppCategory
                // TODO
                
                applicationVersion.setBytes(bytes);
                applicationVersion.setFileSizeInKb(fileSizeInKb);
                applicationVersion.setChecksumMd5(ChecksumHelper.calculateMD5(bytes));
                applicationVersion.setContentType(contentType);
                applicationVersion.setVersionCode(versionCode);
                applicationVersion.setVersionName(versionName);
                applicationVersion.setLabel(label);
                applicationVersion.setMinSdkVersion(minSdkVersion);
                applicationVersion.setIcon(icon);
                applicationVersion.setTimeUploaded(Calendar.getInstance());
                applicationVersion.setContributor(contributor);
                applicationVersion.setApplicationVersionStatus(ApplicationVersionStatus.PENDING_APPROVAL);
                
                if (isUpdateOfExistingApplication) {
                    // Verify that the packageName of the APK update matches that of the Application
                    if (!applicationVersion.getApplication().getPackageName().equals(packageName)) {
                        result.rejectValue("application", "NonUnique");
                    }
                }
                
                if (!isUpdateOfExistingApplication) {
                    // Verify that an Application with identical packageName has not already been uploaded withing the same Project
                    Application existingApplication = projectApplicationDao.readByPackageName(project, packageName);
                    if (existingApplication != null) {
                        result.rejectValue("application", "NonUnique", new String[] {"application"}, null);
                    }
                }
                
                if (isUpdateOfExistingApplication) {
                    // Verify that the versionCode is higher than previous ones
                    List<ApplicationVersion> existingApplicationVersions = applicationVersionDao.readAll(applicationVersion.getApplication());
                    for (ApplicationVersion existingApplicationVersion : existingApplicationVersions) {
                        if (existingApplicationVersion.getVersionCode() >= applicationVersion.getVersionCode()) {
                            result.rejectValue("versionCode", "TooLow");
                            break;
                        }
                    }
                }
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
        
        if (result.hasErrors()) {
            model.addAttribute("project", project);
            model.addAttribute("appCategory", appCategory);
            model.addAttribute("appGroup", appGroup);
            return "project/app/create";
        } else {
            Application application = applicationVersion.getApplication();
            logger.info("application: " + application);
            if (application == null) {
                // First-time upload for packageName
                
                // Create new Application
                application = new Application();
                application.setLocale(contributor.getLocale()); // TODO: Add Locale to each Project?
                application.setPackageName(packageName);
                application.setApplicationStatus(ApplicationStatus.MISSING_APK); // Will be changed to "ApplicationStatus.ACTIVE" once the corresponding ApplicationVersion has been approved
                application.setContributor(contributor);
                application.setAppGroup(appGroup);
                applicationDao.create(application);
                
                appGroup.getApplications().add(application);
                appGroupDao.update(appGroup);

                applicationVersion.setApplication(application);
                applicationVersionDao.create(applicationVersion);
            } else {
                // Update of existing packageName previously uploaded
                
                // Create new ApplicationVersion for the existing Application
                applicationVersionDao.create(applicationVersion);
            }
            
            // Refresh REST API cache
//            projectJsonService.refreshApplicationsInAppCollection(appCollection);
            projectJsonService.refreshApplicationsInAppCollection();
            
            // Post upload notification in Slack
            if (EnvironmentContextLoaderListener.env == Environment.PROD) {
                String applicationDescription = !isUpdateOfExistingApplication ? "Application" : "APK version";
                String text = URLEncoder.encode(
                        contributor.getFirstName() + " (" + contributor.getEmail() + ") just uploaded a new " + applicationDescription + ":\n" + 
                        "• Project: \"" + project.getName() + "\"\n" +
                        "• App Category: \"" + appCategory.getName() + "\"\n" +
                        "• AppGroup: #" + appGroup.getId() + "\n" +
                        "• Package name: \"" + application.getPackageName() + "\"\n" + 
                        "• Version code: " + applicationVersion.getVersionCode() + "\n" +
                        "• Version name: \"" + applicationVersion.getVersionName()+ "\"\n" +
                        "• APK status: \"" + applicationVersion.getApplicationVersionStatus() + "\"\n" +
                        "Once the APK has been reviewed and approved, it will become available for download via the Appstore.\n" +
                        "See ") + "http://elimu.ai/project/" + project.getId() + "/app-category/" + appCategory.getId() + "/app-group/" + appGroup.getId() + "/app/" + application.getId() + "/edit";
                SlackApiHelper.postMessage("G6UR7UH2S", text, null, null);
            }
            
            // Send notification e-mail to admin with a reminder to review the APK
            String to = "elimu.ai <info@elimu.ai>";
            String from = "elimu.ai <info@elimu.ai>";
            String subject = "[" + project.getName() + "] A new APK version has been uploaded";
            String title = application.getPackageName() + " (" + applicationVersion.getVersionCode() + ")";
            String htmlText = "<p>" + contributor.getFirstName() + " (" + contributor.getEmail() + ") just uploaded a new APK version:</p>";
            htmlText += "<ul>";
                htmlText += "<li>Project: \"" + project.getName() + "\"</li>";
                htmlText += "<li>App Category: \"" + appCategory.getName() + "\"</li>";
                htmlText += "<li>AppGroup: #" + appGroup.getId() + "</li>";
                htmlText += "<li>Package name: \"" + application.getPackageName() + "\"</li>";
                htmlText += "<li>Version code: " + applicationVersion.getVersionCode() + "</li>";
                htmlText += "<li>Version name: \"" + applicationVersion.getVersionName()+ "\"</li>";
                htmlText += "<li>APK status: " + applicationVersion.getApplicationVersionStatus() + "</li>";
            htmlText += "</ul>";
            htmlText += "<h2>APK Review</h2>";
            htmlText += "<p>To review the APK, go to http://elimu.ai/admin/project/apk-reviews</p>";
            Mailer.sendHtml(to, null, from, subject, title, htmlText);
            
            if (!isUpdateOfExistingApplication) {
                return "redirect:/project/{projectId}/app-category/{appCategoryId}/app-group/{appGroupId}/app/list#" + application.getId();
            } else {
                return "redirect:/project/{projectId}/app-category/{appCategoryId}/app-group/{appGroupId}/app/" + application.getId() + "/edit";
            }
        }
    }
    
    /**
     * See http://www.mkyong.com/spring-mvc/spring-mvc-failed-to-convert-property-value-in-file-upload-form/
     * <p></p>
     * Fixes this error message:
     * "Cannot convert value of type [org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile] to required type [byte] for property 'bytes[0]'"
     */
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
    	logger.info("initBinder");
    	binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }
}

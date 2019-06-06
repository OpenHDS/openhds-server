package org.openhds.task;

import java.util.List;

import org.openhds.service.SocialGroupService;
import org.openhds.domain.SocialGroup;
import org.openhds.util.CalendarUtil;
import org.openhds.util.ShallowCopier;
import org.openhds.service.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("socialGroupXmlWriter")
public class SocialGroupXmlWriterTask extends XmlWriterTemplate<SocialGroup> {

    private SocialGroupService socialGroupService;

    @Autowired
    public SocialGroupXmlWriterTask(AsyncTaskService asyncTaskService, CalendarUtil calendarUtil,
            SocialGroupService socialGroupService) {
        super(asyncTaskService, AsyncTaskService.SOCIALGROUP_TASK_NAME);
        this.socialGroupService = socialGroupService;
    }

    @Override
    protected SocialGroup makeCopyOf(SocialGroup original) {
        return ShallowCopier.copySocialGroup(original);
    }

    @Override
    protected List<SocialGroup> getEntitiesInRange(TaskContext taskContext, int i, int pageSize) {
        return socialGroupService.getAllSocialGroupsInRange(i, pageSize);
    }

    @Override
    protected Class<?> getBoundClass() {
        return SocialGroup.class;
    }

    @Override
    protected String getStartElementName() {
        return "socialgroups";
    }

    @Override
    protected int getTotalEntityCount(TaskContext taskContext) {
        return (int) socialGroupService.getTotalSocialGroupCount();
    }

}

package org.openhds.task;

import java.util.List;

import org.openhds.service.LocationHierarchyService;
import org.openhds.domain.Location;
import org.openhds.util.CalendarUtil;
import org.openhds.util.ShallowCopier;
import org.openhds.service.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("locationXmlWriter")
public class LocationXmlWriterTask extends XmlWriterTemplate<Location> {

    private LocationHierarchyService locationHierarchyService;

    @Autowired
    public LocationXmlWriterTask(AsyncTaskService asyncTaskService, CalendarUtil calendarUtil,
            LocationHierarchyService locationHierarchyService) {
        super(asyncTaskService, AsyncTaskService.LOCATION_TASK_NAME);
        this.locationHierarchyService = locationHierarchyService;
    }

    @Override
    protected Location makeCopyOf(Location original) {
        return ShallowCopier.copyLocation(original);
    }

    @Override
    protected List<Location> getEntitiesInRange(TaskContext taskContext, int i, int pageSize) {
        return locationHierarchyService.getAllLocationsInRange(i, pageSize);
    }

    @Override
    protected Class<?> getBoundClass() {
        return Location.class;
    }

    @Override
    protected String getStartElementName() {
        return "locations";
    }

    @Override
    protected int getTotalEntityCount(TaskContext taskContext) {
        return (int) locationHierarchyService.getTotalLocationCount();
    }

}

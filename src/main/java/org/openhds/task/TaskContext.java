package org.openhds.task;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContext;

public class TaskContext {
    private File destinationFile;
    private SecurityContext securityContext;
    private Map<String, String> extraData = new HashMap<String, String>();

    public TaskContext(File destinationFile, SecurityContext securityContext) {
        this.destinationFile = destinationFile;
        this.securityContext = securityContext;
    }

    public File getDestinationFile() {
        return destinationFile;
    }

    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public void addExtraData(String name, String value) {
        extraData.put(name, value);
    }

    public String getExtraData(String name) {
        return extraData.get(name);
    }
}

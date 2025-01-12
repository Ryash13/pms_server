package com.jfs.pms.service.sprints;

import com.jfs.pms.domain.Sprint;
import com.jfs.pms.domain.User;

public interface ISprintService {

    Sprint addSprint(User loggedInUser, String projectId, Sprint sprint);
    Sprint updateSprint(User loggedInUser, Sprint sprint);
    boolean deleteSprint(User loggedInUser, long sprintId);
    Sprint getCurrentSprintForProject(User loggedInUser, String projectId);
}

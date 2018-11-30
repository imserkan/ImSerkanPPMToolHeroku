package com.sislamoglu.ppmtool.services;

import com.sislamoglu.ppmtool.domain.Backlog;
import com.sislamoglu.ppmtool.domain.Project;
import com.sislamoglu.ppmtool.domain.ProjectTask;
import com.sislamoglu.ppmtool.exceptions.ProjectIDException;
import com.sislamoglu.ppmtool.exceptions.ProjectNotFoundException;
import com.sislamoglu.ppmtool.repositories.BacklogRepository;
import com.sislamoglu.ppmtool.repositories.ProjectRepository;
import com.sislamoglu.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){
        try{
            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
            projectTask.setBacklog(backlog);
            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);
            projectTask.setProjectSequence(projectIdentifier+"-"+backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if(projectTask.getPriority() == null || projectTask.getPriority() == 0){
                projectTask.setPriority(3);
            }
            if(projectTask.getStatus() == null || projectTask.getStatus().equals("")){
                projectTask.setStatus("TO_DO");
            }
            return projectTaskRepository.save(projectTask);
        }catch (Exception ex){
            throw new ProjectNotFoundException("Project not found");
        }
    }

    public Iterable<ProjectTask> findBacklogById(String projectIdentifier, String username){
        projectService.findProjectByIdentifier(projectIdentifier, username);
        Iterable<ProjectTask> projectTasks = projectTaskRepository.findByProjectIdentifierOrderByPriority(projectIdentifier);
        for(ProjectTask projectTask: projectTasks){
           isIncomplete(projectTask);
        }
        return projectTasks;
    }

    public ProjectTask findPTByProjectSequence(String backlogId, String sequence, String username){
        projectService.findProjectByIdentifier(backlogId, username);
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(sequence);
        if(projectTask==null){
            throw new ProjectNotFoundException("Project Task '" + sequence + "' not found.");
        }

        if(!projectTask.getProjectIdentifier().equals(backlogId)){
            throw new ProjectNotFoundException("Project Task '" + sequence +
                    "' does not exists in the project: '" + backlogId + "'");
        }
        return projectTaskRepository.findByProjectSequence(sequence);
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlogId, String sequence, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlogId, sequence, username);
        projectTask = updatedTask;
        return projectTaskRepository.save(projectTask);
    }

    public void deleteByProjectSequence(String backlogId, String sequence, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlogId, sequence, username);
        projectTaskRepository.delete(projectTask);
    }

    public void isIncomplete(ProjectTask projectTask){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        if (!projectTask.getStatus().equals("DONE")){
            if (projectTask.getDueDate().compareTo(new Date()) < 0){
                projectTask.setStatus("INCOMPLETE");
            }
        }
    }
}

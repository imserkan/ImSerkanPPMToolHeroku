package com.sislamoglu.ppmtool.services;

import com.sislamoglu.ppmtool.domain.Backlog;
import com.sislamoglu.ppmtool.domain.Project;
import com.sislamoglu.ppmtool.domain.User;
import com.sislamoglu.ppmtool.exceptions.ProjectIDException;
import com.sislamoglu.ppmtool.exceptions.ProjectNotFoundException;
import com.sislamoglu.ppmtool.repositories.BacklogRepository;
import com.sislamoglu.ppmtool.repositories.ProjectRepository;
import com.sislamoglu.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private UserRepository userRepository;


    public Project saveOrUpdateProject(Project project, String username){
        if (project.getId() != null){
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            if(existingProject == null){
                throw new ProjectNotFoundException("Project with ID: '" + project.getProjectIdentifier() +
                        "' cannot be updated, because it doesn't exist.");
            }else{
                if(!existingProject.getProjectLeader().equals(username)){
                    throw new ProjectNotFoundException("Project is not found in your account");
                }
            }
        }
        try{
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            // Create Part
            if(project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }
            // Update Part
            else{
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
            return projectRepository.save(project);
        }catch (Exception ex){
            throw new ProjectIDException("Project ID '" +
                    project.getProjectIdentifier() + "' already exists.");
        }

    }

    public Project findProjectByIdentifier(String projectId, String username){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project==null){
            throw new ProjectIDException("Project ID '" + projectId + "' does not exist.");
        }
        if(!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project is not found in your account.");
        }
        return project;
    }

    public Iterable<Project> findAllProjects(String username){

        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId, String username){
        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }


}

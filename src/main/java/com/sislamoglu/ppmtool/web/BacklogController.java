package com.sislamoglu.ppmtool.web;

import com.sislamoglu.ppmtool.domain.Project;
import com.sislamoglu.ppmtool.domain.ProjectTask;
import com.sislamoglu.ppmtool.exceptions.ProjectNotFoundException;
import com.sislamoglu.ppmtool.repositories.ProjectRepository;
import com.sislamoglu.ppmtool.services.MapValidationErrorService;
import com.sislamoglu.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private ProjectRepository projectRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/{backlogId}")
    public ResponseEntity<?> createNewProjectTask(@Valid @RequestBody ProjectTask projectTask,
                                                  BindingResult bindingResult,
                                                  @PathVariable("backlogId") String backlogId,
                                                  Principal principal){

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(bindingResult);
        if(errorMap != null){
            return errorMap;
        }
        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlogId, projectTask, principal.getName());
        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{backlogId}")
    public ResponseEntity<Iterable<ProjectTask>> getProjectTasks(@PathVariable("backlogId") String backlogId,
                                                                 Principal principal){
        return new ResponseEntity<>(projectTaskService.findBacklogById(backlogId, principal.getName()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{backlogId}/{ptSeq}")
    public ResponseEntity<?> getProjectTask(@PathVariable("backlogId") String backlogId,
                                            @PathVariable("ptSeq") String projectTaskSequence,
                                            Principal principal){
        ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlogId, projectTaskSequence, principal.getName());
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{backlogId}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask updatedProjectTask,
                                               BindingResult result,
                                               @PathVariable("backlogId")String backlogId,
                                               @PathVariable("pt_id")String sequence,
                                               Principal principal){
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if(errorMap != null){
            return errorMap;
        }
        ProjectTask projectTask = projectTaskService.updateByProjectSequence(updatedProjectTask, backlogId, sequence, principal.getName());
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{backlogId}/{pt_id}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable("backlogId")String backlogId,
                                               @PathVariable("pt_id")String sequence,
                                               Principal principal){
        projectTaskService.deleteByProjectSequence(backlogId, sequence, principal.getName());
        return new ResponseEntity<String>("Project Task '" + sequence + "' in Project '"
                + backlogId + "' is successfully deleted.", HttpStatus.OK);
    }
}

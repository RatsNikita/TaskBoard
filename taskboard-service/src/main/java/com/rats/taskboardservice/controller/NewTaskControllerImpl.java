package com.rats.taskboardservice.controller;


import com.rats.taskboardservice.api.controller.NewTaskController;
import com.rats.taskboardservice.entity.TaskEntity;
import com.rats.taskboardservice.entity.UserEntity;
import com.rats.taskboardservice.api.dto.TaskDto;
import com.rats.taskboardservice.service.TaskService;
import com.rats.taskboardservice.service.UserService;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/new-task")
public class NewTaskControllerImpl implements NewTaskController {

  private final MapperFacade mapperFacade;

  private final TaskService taskService;

  private final UserService userService;

  @Override
  public String saveTask(Model model, TaskDto task, Cookie authUser) {
    UserEntity user = userService.findByNickname(authUser.getValue());
    TaskEntity taskEntity =  mapperFacade.map(task, TaskEntity.class);
    taskEntity.setUser(user);
    taskService.save(taskEntity,user);
    model.addAttribute("currentUser", user.getNickname());
    List<TaskEntity> tasksOfUser = taskService.getMyTasksOfUser(user);
    model.addAttribute("tasks", mapperFacade.mapAsList(tasksOfUser,TaskDto.class));
    return "my-tasks";
  }
}

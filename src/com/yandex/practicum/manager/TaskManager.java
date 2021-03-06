package com.yandex.practicum.manager;

import java.util.ArrayList;
import java.util.List;

import com.yandex.practicum.pattern.Epic;
import com.yandex.practicum.pattern.Subtask;
import com.yandex.practicum.pattern.Task;

public interface TaskManager {

     void newTask(Task task);

     ArrayList<Task> getTaskAll();
     Task getTaskId(Integer id);
     boolean updateTask(Task task);
     void deleteTaskAll();
     boolean deleteTaskById(Integer id);
     void moveSubtask(Subtask subtask);
     ArrayList<Subtask> getSubtaskAll();
    Subtask getSubtaskId(Integer id);
    boolean updateSubtask(Subtask subtask);
    void deleteSubtaskAll();
    boolean deleteSubtaskId(Integer id);

    void moveEpic(Epic epic);

    void addsSubtaskIdToEpic(Epic epic, Integer id);
    ArrayList<Epic> getEpicAll();
    Epic getEpicById(Integer id);
    boolean updateEpic(Epic epic);
    void deleteEpicAll();
    boolean deleteEpicById(Integer id);

    ArrayList<Subtask> getSubtaskInEpicAll(Epic epic);
    List<Task> getHistory();
}
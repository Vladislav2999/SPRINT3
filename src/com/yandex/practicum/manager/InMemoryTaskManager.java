package com.yandex.practicum.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.yandex.practicum.pattern.Epic;
import com.yandex.practicum.pattern.Status;
import com.yandex.practicum.pattern.Subtask;
import com.yandex.practicum.pattern.Task;

public class InMemoryTaskManager implements TaskManager {
    private Integer id = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager managerHistory = Managers.getDefaultHistory();

    @Override
    public void newTask(Task task) {
        Integer temp = createId();
        tasks.put(temp, task);
        task.setId(temp);
    }

    @Override
    public ArrayList<Task> getTaskAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task getTaskId(Integer id) {
        if (tasks.containsKey(id)) {
            managerHistory.add(tasks.get(id));
            return tasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public boolean updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.replace(task.getId(), task);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteTaskAll() {
        tasks.clear();
    }

    @Override
    public boolean deleteTaskById(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void moveSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            Integer temp = createId();
            subtasks.put(temp, subtask);
            subtask.setId(temp);
            epics.get(subtask.getEpicId()).setIdSubtaskValue(subtask.getId());
            checkStatusEpic(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public ArrayList<Subtask> getSubtaskAll() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Subtask getSubtaskId(Integer id) {
        if (subtasks.containsKey(id)) {
            managerHistory.add(subtasks.get(id));
            return subtasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.replace(subtask.getId(), subtask);
            checkStatusEpic(epics.get(subtask.getEpicId()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteSubtaskAll() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
            epic.getIdSubtask().clear();
        }
    }

    @Override
    public boolean deleteSubtaskId(Integer id) {
        if (subtasks.containsKey(id)) {
            Epic epic = epics.get(subtasks.get(id).getEpicId());
            epic.getIdSubtask().remove(id);
            subtasks.remove(id);
            checkStatusEpic(epic);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void moveEpic(Epic epic) {
        Integer temp = createId();
        epics.put(temp, epic);
        epic.setId(temp);
        epic.setStatus(Status.NEW);
    }

    @Override
    public void addsSubtaskIdToEpic(Epic epic, Integer id) {
        epic.setIdSubtaskValue(id);
    }

    @Override
    public ArrayList<Epic> getEpicAll() {
        return new ArrayList<>(epics.values());
    }



    @Override
    public Epic getEpicById(Integer id) {
        if (epics.containsKey(id)) {
            managerHistory.add(epics.get(id));
            return epics.get(id);
        } else {
            return null;
        }
    }

    //??????????????????
    @Override
    public boolean updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.replace(epic.getId(), epic);
            checkStatusEpic(epics.get(epic.getId()));
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void deleteEpicAll() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public boolean deleteEpicById(Integer id) {
        if (epics.containsKey(id)) {
            for (Integer integer : epics.get(id).getIdSubtask()) {
                subtasks.remove(integer);
            }
            epics.remove(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ArrayList<Subtask> getSubtaskInEpicAll(Epic epic) {
        ArrayList<Subtask> temp = new ArrayList<>();
        for (int i = 0; i < epic.getIdSubtask().size(); i++) {
            if (subtasks.get(epic.getIdSubtaskValue(i)) == null) {
                continue;
            } else
                temp.add(subtasks.get(epic.getIdSubtaskValue(i)));
        }
        return temp;
    }


   private Integer createId() {
        id += 1;
        return id;
    }

    private void checkStatusEpic(Epic epic) {
        int summ = 0;
        int summ1 = 0;
        if (epic.getIdSubtask().isEmpty()) {
            epic.setStatus(Status.NEW);
        }
        for (Integer integer : epic.getIdSubtask()) {
            if (subtasks.get(epic.getIdSubtask().get(integer)).getStatus().equals(Status.IN_PROGRESS)) {
                epic.setStatus(Status.IN_PROGRESS);
                break;
            } else if (subtasks.get(epic.getIdSubtask().get(integer)).getStatus().equals(Status.NEW)) {
                summ++;
                if (summ == epic.getIdSubtask().size()) {
                    epic.setStatus(Status.NEW);
                }
            } else if (subtasks.get(epic.getIdSubtask().get(integer)).getStatus().equals(Status.DONE)) {
                summ1++;
                if (summ1 == epic.getIdSubtask().size()) {
                    epic.setStatus(Status.DONE);
                }
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return managerHistory.getHistory();
    }
}


package co.netguru.todolist.ui.tasks;

public enum TasksType {
    FINISHING,
    TODO,
    DONE;

    public static TasksType fromOrdinal(int ordinal) {
        return TasksType.values()[ordinal];
    }
}
package co.netguru.todolist.ui.tasks;

import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.List;

import javax.inject.Inject;

import co.netguru.todolist.common.RxTransformers;
import co.netguru.todolist.domain.TaskController;
import co.netguru.todolist.domain.model.Task;
import co.netguru.todolist.ui.base.BasePresenter;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class TasksPresenter extends BasePresenter<TasksView> {

    private static final int FINISHING_TASKS_PERIOD = 7;

    private final TaskController taskController;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onDetach() {
        super.onDetach();
        compositeDisposable.dispose();
    }

    @Inject
    public TasksPresenter(TaskController taskController) {
        this.taskController = taskController;
    }

    public void handleAddTaskClick() {
        getMvpView().showAddTaskView();
    }

    public void setupTasksSubscription(TasksType taskType) {
        compositeDisposable.add(
                getTaskFlowableBasedOnType(taskType)
                        .compose(RxTransformers.applyFlowableIoSchedulers())
                        .subscribe(
                                tasks -> {
                                    if (tasks.isEmpty()) {
                                        getMvpView().showNoTasksView();
                                    } else {
                                        getMvpView().displayTasks(tasks);
                                    }
                                },
                                throwable -> Timber.d("Error occurred while loading tasks", throwable)
                        ));
    }

    private Flowable<List<Task>> getTaskFlowableBasedOnType(TasksType tasksType) {
        switch (tasksType) {
            case TODO:
                return taskController.getToDoTasks();
            case DONE:
                return taskController.getDoneTasks();
            case FINISHING:
                return taskController.getTasksWithDueDateBefore(LocalDate.now().plus(FINISHING_TASKS_PERIOD, ChronoUnit.DAYS));
            default:
                throw new IllegalArgumentException("Unknown task type");
        }
    }

    public void deleteTask(Task task) {
        compositeDisposable.add(
                taskController.deleteTask(task)
                        .compose(RxTransformers.applyCompletableIoSchedulers())
                        .subscribe(() -> getMvpView().showTaskDeletedMessage(task.getTitle()),
                                throwable -> Timber.e(throwable, "Error while deleting task")));
    }

    public void editTask(Task task) {
        getMvpView().showEditTaskView(task);
    }

    public void updateTaskDoneState(Task task) {
        compositeDisposable.add(
                taskController.updateTaskOnly(task)
                        .compose(RxTransformers.applyCompletableIoSchedulers())
                        .subscribe(() -> {
                            if (task.isDone()) {
                                getMvpView().showTaskMarkedAsDoneMessage(task.getTitle());
                            } else {
                                getMvpView().showTaskUnmarkedAsDoneMessage(task.getTitle());
                            }
                        }, throwable -> Timber.e(throwable, "Error while deleting task")));
    }
}

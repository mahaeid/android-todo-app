package co.netguru.todolist.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import co.netguru.todolist.R;
import co.netguru.todolist.ui.base.BaseActivity;
import co.netguru.todolist.ui.tasks.TasksFragment;
import co.netguru.todolist.ui.tasks.TasksType;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        setupNavigationListener();
    }

    private void setupNavigationListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_todo);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.action_finishing:
                fragment = TasksFragment.newInstance(TasksType.FINISHING);
                break;
            case R.id.action_todo:
                fragment = TasksFragment.newInstance(TasksType.TODO);
                break;
            case R.id.action_done:
                fragment = TasksFragment.newInstance(TasksType.DONE);
                break;
            default:
                throw new UnsupportedOperationException("Unhandled navigation item");
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        return true;
    }
}
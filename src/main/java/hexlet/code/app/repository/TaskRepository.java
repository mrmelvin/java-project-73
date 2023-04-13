package hexlet.code.app.repository;

import com.querydsl.core.types.dsl.SimpleExpression;
import hexlet.code.app.model.QTask;
import hexlet.code.app.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Predicate;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {}
//@Repository
//public interface TaskRepository extends JpaRepository<Task, Long>, QuerydslPredicateExecutor<Task>,
//        QuerydslBinderCustomizer<QTask> {


//    List<Task> findAll(Predicate predicate);
//
//    @Override
//    default void customize(QuerydslBindings bindings, QTask task) {
//        bindings.bind(task.taskStatus.id).first(SimpleExpression::eq);
//        bindings.bind(task.executor.id).first(SimpleExpression::eq);
//        bindings.bind(task.labels.any().id).first((SimpleExpression::eq));
//        bindings.bind(task.author.id).first(SimpleExpression::eq);
//        bindings.excluding(task.createdAt);
//        bindings.excluding(task.name);
//        bindings.excluding(task.description);
//    }
//}

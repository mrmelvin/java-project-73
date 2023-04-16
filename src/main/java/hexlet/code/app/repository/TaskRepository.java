package hexlet.code.app.repository;

import com.querydsl.core.types.dsl.SimpleExpression;
import hexlet.code.app.model.QTask;
import hexlet.code.app.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.Predicate;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, QuerydslPredicateExecutor<Task> {

	Iterable<Task> findAll(Predicate predicate);
	Optional<Task> findById(Long id);

//    @Override
//    default void customize(QuerydslBindings bindings, QTask task) {
//        bindings.bind(task.author.id).first(SimpleExpression::eq);
//        bindings.bind(task.executor.id).first(SimpleExpression::eq);
//        bindings.bind(task.taskStatus.id).first(SimpleExpression::eq);
//        bindings.bind(task.labels.any().id).first(SimpleExpression::eq);
//    }
}

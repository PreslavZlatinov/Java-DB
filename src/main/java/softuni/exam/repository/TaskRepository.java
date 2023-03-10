package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.CarType;
import softuni.exam.models.entity.Task;

import java.util.List;

// TODO:
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t.car.carMake, t.car.carModel, t.car.kilometers, t.mechanic.firstName, t.mechanic.lastName, t.id, t.car.engine, t.price from Task t WHERE t.car.carType = :carType ORDER BY t.price DESC")
    List<Object[]> findAllCarsCoupe(CarType carType);
}

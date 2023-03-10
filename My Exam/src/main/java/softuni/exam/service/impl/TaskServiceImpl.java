package softuni.exam.service.impl;

import com.sun.xml.bind.api.JAXBRIContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TaskImportDTO;
import softuni.exam.models.dto.TaskImportRootDTO;
import softuni.exam.models.entity.*;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.repository.PartRepository;
import softuni.exam.repository.TaskRepository;
import softuni.exam.service.TaskService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final Path path = Path.of("src", "main", "resources", "files", "xml", "tasks.xml");

    private final TaskRepository taskRepository;

    private final MechanicRepository mechanicRepository;

    private final CarRepository carRepository;

    private PartRepository partRepository;
    private final Unmarshaller unmarshaller;
    private final Validator validator;
    private final ModelMapper modelMapper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, MechanicRepository mechanicRepository, CarRepository carRepository, PartRepository partRepository) throws JAXBException {
        this.taskRepository = taskRepository;
        this.mechanicRepository = mechanicRepository;
        this.carRepository = carRepository;
        this.partRepository = partRepository;

        JAXBContext context = JAXBRIContext.newInstance(TaskImportRootDTO.class);
        this.unmarshaller = context.createUnmarshaller();

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

        this.modelMapper = new ModelMapper();

        this.modelMapper.addConverter(ctx -> LocalDateTime.parse(ctx.getSource(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), String.class, LocalDateTime.class);
    }

    @Override
    public boolean areImported() {
        return this.taskRepository.count() > 0;
    }

    @Override
    public String readTasksFileContent() throws IOException {
        return Files.readString(path);
    }

    @Override
    public String importTasks() throws IOException, JAXBException {
        TaskImportRootDTO taskImportRootDTO = (TaskImportRootDTO) this.unmarshaller.unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return taskImportRootDTO.getTasks().stream().map(this::importTask).collect(Collectors.joining("\n"));
    }

    private String importTask(TaskImportDTO taskImportDTO) {
        Set<ConstraintViolation<TaskImportDTO>> errors = this.validator.validate(taskImportDTO);

        if (!errors.isEmpty()) {
            return "Invalid task";
        }


        Optional<Mechanic> mechanic = this.mechanicRepository.findByFirstName(taskImportDTO.getMechanic().getFirstName());

        if (mechanic.isPresent()) {

            Task task = this.modelMapper.map(taskImportDTO, Task.class);

            Optional<Car> car = this.carRepository.findById(taskImportDTO.getCar().getId());
            Optional<Part> part = this.partRepository.findById(taskImportDTO.getPart().getId());

            task.setCar(car.get());
            task.setPart(part.get());
            task.setMechanic(mechanic.get());

            this.taskRepository.save(task);

            return "Successfully imported task " + task.getPrice();
        } else {
            return "Invalid task";
        }
    }

    @Override
    public String getCoupeCarTasksOrderByPrice() {

        List<Object[]> tasks = this.taskRepository.findAllCarsCoupe(CarType.coupe);

        List<String> data = new ArrayList<>();

        for (Object[] task : tasks) {
            data.add(String.format("Car %s %s with %dkm%n" +
                    "-Mechanic: %s %s - task №%d:%n" +
                    "--Engine: %.2f%n" +
                    "---Price: %.2f$%n", task[0],task[1],task[2],task[3],task[4],task[5],task[6],task[7]));

        }

        return String.join("",data);

    }
}

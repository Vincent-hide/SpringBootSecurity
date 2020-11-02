package hungrybird.springbootsecurity.student;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("management/api/v1/students")
public class StudentManagementController {
  private static final List<Student> STUDENTS = Arrays.asList(
    new Student(1, "Vince"),
    new Student(2, "Hide"),
    new Student(3, "Suho")
  );

  @GetMapping
  public List<Student> getAllStudents() {
    return this.STUDENTS;
  }

  @PostMapping
  public void registerNewStudent(@RequestBody Student student) {
    System.out.println(student);
  }

  @DeleteMapping(path = "{studentId}")
  public void deleteStudent(@PathVariable("studentId") Integer studentId) {
    System.out.println(studentId);
  }

  @PutMapping(path = "{studentId}")
  public void updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody Student student) {
    System.out.println(String.format("%s %s", studentId, student));
  }
}

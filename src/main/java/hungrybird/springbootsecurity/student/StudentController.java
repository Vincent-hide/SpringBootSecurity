package hungrybird.springbootsecurity.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {
  private static final List<Student> STUDENTS = Arrays.asList(
    new Student(1, "Vince"),
    new Student(2, "Hide"),
    new Student(3, "Suho")
  );

  @GetMapping(path = "{studentId}")
  public Student getStudent(@PathVariable("studentId") Integer studentId) {
    return STUDENTS.stream()
      .filter(student -> studentId.equals(student.getStudentId()))
      .findFirst()
      .orElseThrow(() -> new IllegalStateException(String.format("Student %d does not exist", studentId)));
  }
}

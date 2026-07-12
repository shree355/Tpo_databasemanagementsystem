package net.javaguides.sms.service;

import java.util.List;
import java.util.Optional;

import net.javaguides.sms.entity.RegisterEntity;

public interface RegisterService {
    List<RegisterEntity> getAllStudents();

    RegisterEntity saveStudent(RegisterEntity student);

    RegisterEntity getStudentById(Long id);

    Optional<RegisterEntity> getStudentByUserName(String userName);

    Optional<RegisterEntity> getStudentByEmail(String email);

    RegisterEntity updateStudent(RegisterEntity student);

    void deleteStudentById(Long id);
}

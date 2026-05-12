package com.scalable.enrollment.service;

import com.scalable.enrollment.domain.Enrollment;
import com.scalable.enrollment.domain.EnrollmentRepository;
import com.scalable.enrollment.feign.CourseClient;
import com.scalable.enrollment.feign.SectionDto;
import org.springframework.stereotype.Service;
import com.scalable.enrollment.messaging.EnrollmentPublisher;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseClient courseClient;
    private final EnrollmentPublisher enrollmentPublisher;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             CourseClient courseClient,
                             EnrollmentPublisher enrollmentPublisher) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseClient = courseClient;
        this.enrollmentPublisher = enrollmentPublisher;
    }

    public Enrollment create(Long studentId, Long sectionId) {

        Enrollment enrollment = new Enrollment(studentId, sectionId);

        enrollment = enrollmentRepository.save(enrollment);

        SectionDto section = courseClient.reserve(sectionId);

        enrollment.confirm(section.tuition());

        enrollment = enrollmentRepository.save(enrollment);

        enrollmentPublisher.publishConfirmed(enrollment);

        return enrollment;
    }
}
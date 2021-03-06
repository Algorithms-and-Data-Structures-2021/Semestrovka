package com.damir.services.impl;

import com.damir.dto.DtoMapper;
import com.damir.dto.HomeworkDto;
import com.damir.models.*;
import com.damir.repositories.LessonRepository;
import com.damir.services.FilesService;
import com.damir.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final FilesService filesService;
    private final DtoMapper dtoMapper;

    @Override
    public void createTask(Long lessonId, Task task) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        lesson.setTask(task);
        lessonRepository.save(lesson);
    }

    @Override
    public void passHomework(Long lessonId, HomeworkDto homeworkDto, MultipartFile[] multipart, User user) {
        List<FileInfo> photos = filesService.upload(multipart);
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();

        Homework homework = dtoMapper.homeworkDtoToHomework(homeworkDto);
        homework.setPhotos(photos);
        homework.setStudent(user);
        homework.setStatus(Homework.Status.UNCHECKED);
        homework.setLesson(lesson);

        lesson.getHomeworks().add(homework);
        lessonRepository.save(lesson);
    }

    @Override
    public Lesson getLesson(Long lessonId, User user) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        if (lesson.getCourse().getSubscribers().stream().noneMatch(x->x.getId().equals(user.getId())) && !user.getId().equals(lesson.getCourse().getCreator().getId()))
            throw new RuntimeException();
        return lesson;
    }

}

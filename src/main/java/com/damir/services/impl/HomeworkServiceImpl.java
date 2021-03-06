package com.damir.services.impl;

import com.damir.dto.HomeworkInfoDto;
import com.damir.models.Homework;
import com.damir.models.User;
import com.damir.repositories.HomeworkRepository;
import com.damir.services.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HomeworkServiceImpl implements HomeworkService {
    private final HomeworkRepository homeworkRepository;

    @Override
    public Homework getHomework(Long homeworkId, User user) {
        Homework homework = homeworkRepository.findById(homeworkId).orElseThrow();
        if (!user.getId().equals(homework.getStudent().getId()) && !user.getId().equals(homework.getLesson().getCourse().getCreator().getId()))
            throw new RuntimeException();
        return homework;
    }

    @Override
    public void checkHomework(Long homeworkId, Integer score) {
        Homework homework = homeworkRepository.findById(homeworkId).orElseThrow();
        homework.setScore(score);
        homework.setStatus(Homework.Status.CHECKED);
        homeworkRepository.save(homework);
    }

    @Override
    public HomeworkInfoDto getHomeworkInfo(Long homeworkId, User user) {
        boolean isChecked;
        boolean isPassed;
        Integer score;
        if (homeworkId == null ) {
            isChecked = false;
            isPassed = false;
            score = 0;
        } else {
            Homework homework = homeworkRepository.findById(homeworkId).orElseThrow();
            isChecked = homework.getStatus().equals(Homework.Status.CHECKED);
            isPassed = homework.getStatus().equals(Homework.Status.UNCHECKED);
            score = homework.getScore();

        }
        return HomeworkInfoDto.builder()
                .isChecked(isChecked)
                .isPassed(isPassed)
                .score(score)
                .build();
    }
}

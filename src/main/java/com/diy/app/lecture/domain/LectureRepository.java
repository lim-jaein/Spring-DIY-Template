package com.diy.app.lecture.domain;

import java.util.List;

public interface LectureRepository {
    List<Lecture> findAll();
    void save(Lecture lecture);
    boolean existsById(Long id);
    void delete(Long id);
}

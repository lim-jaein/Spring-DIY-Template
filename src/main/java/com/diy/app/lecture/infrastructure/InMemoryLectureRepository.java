package com.diy.app.lecture.infrastructure;

import com.diy.app.lecture.domain.Lecture;
import com.diy.app.lecture.domain.LectureRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryLectureRepository implements LectureRepository {
    private final Map<Long, Lecture> lectureMap = new ConcurrentHashMap<>();

    @Override
    public List<Lecture> findAll() {
        return lectureMap.values().stream().toList();
    }

    @Override
    public void save(Lecture lecture) {
        lectureMap.put(lecture.getId(), lecture);
    }

    @Override
    public boolean existsById(Long id) {
        return lectureMap.containsKey(id);
    }

    @Override
    public void delete(Long id) {
        lectureMap.remove(id);
    }
}

package com.diy.framework.web.beans.factory;

import com.diy.app.lecture.LectureController;
import com.diy.app.lecture.domain.LectureRepository;
import com.diy.framework.web.annotation.Bean;

public class BeanConfig {
    @Bean("lectureController")
    public LectureController lectureController(LectureRepository lectureRepository) {
        return new LectureController(lectureRepository);
    }
}

package com.diy.app.lecture;

import com.diy.app.lecture.domain.Lecture;
import com.diy.framework.web.Controller;
import com.diy.framework.web.model.Model;
import com.diy.framework.web.model.ModelAndView;
import com.diy.framework.web.view.ViewResolver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LectureController implements Controller {
    private final Map<Long, Lecture> lectureRepository = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AtomicLong idSequence = new AtomicLong();

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return switch (request.getMethod()) {
            case "GET" -> doGet(request, response);
            case "POST" -> doPost(request, response);
            case "PUT" -> doPut(request, response);
            case "DELETE" -> doDelete(request, response);
            default -> throw new IllegalStateException("존재하지 않는 메서드입니다.");
        };
    }

    /**
     * 강의 목록 조회 API
     * @param req
     * @param resp
     */
    public ModelAndView doGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        System.out.println("doGet called.");

        final Collection<Lecture> lectures = lectureRepository.values();

        Model model = new Model();
        model.addAttribute("lectures", lectures);

        return new ModelAndView("lecture-list", model);
    }

    /**
     * 강의 등록 API
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public ModelAndView doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doPost called.");

        try {
            final Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);

            final long id = idSequence.incrementAndGet();

            lecture.setId(id);
            lectureRepository.put(id, lecture);

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (JsonProcessingException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return new ModelAndView("redirect:/lectures");
    }

    /**
     * 강의 수정 API
     * @param req
     * @param resp
     */
    public ModelAndView doPut(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("doPut called.");

        try {
            final String requestUri = req.getRequestURI();
            final long id = Long.parseLong(requestUri.substring(requestUri.lastIndexOf("/") + 1));
            if(!lectureRepository.containsKey(id)) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            }

            final Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);
            lecture.setId(id);
            lectureRepository.put(id, lecture);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonProcessingException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return new ModelAndView("redirect:/lectures");
    }

    /**
     * 강의 삭제 API
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public ModelAndView doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doDelete called.");
        final String requestUri = req.getRequestURI();
        final long id = Long.parseLong(requestUri.substring(requestUri.lastIndexOf("/") + 1));
        if(!lectureRepository.containsKey(id)) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            lectureRepository.remove(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

        return new ModelAndView("redirect:/lectures");
    }
}

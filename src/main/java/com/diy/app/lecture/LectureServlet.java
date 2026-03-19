//package com.diy.app.lecture;
//
//import com.diy.app.lecture.domain.Lecture;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicLong;
//
//@WebServlet("/lectures/*")
//public class LectureServlet extends HttpServlet {
//
//    private final Map<Long, Lecture> lectureRepository = new ConcurrentHashMap<>();
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final AtomicLong idSequence = new AtomicLong();
//
//    /**
//     * 강의 목록 조회 API
//     * @param req
//     * @param resp
//     * @throws ServletException
//     * @throws IOException
//     */
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("doGet called.");
//
//        final Collection<Lecture> lectures = lectureRepository.values();
//        req.setAttribute("lectures", lectures);
//        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
//    }
//
//    /**
//     * 강의 등록 API
//     * @param req
//     * @param resp
//     * @throws ServletException
//     * @throws IOException
//     */
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("doPost called.");
//
//        try {
//            final Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);
//
//            final long id = idSequence.incrementAndGet();
//
//            lecture.setId(id);
//            lectureRepository.put(id, lecture);
//
//            resp.setStatus(HttpServletResponse.SC_CREATED);
//        } catch (JsonProcessingException e) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        } catch (Exception e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    /**
//     * 강의 수정 API
//     * @param req
//     * @param resp
//     * @throws ServletException
//     * @throws IOException
//     */
//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("doPut called.");
//
//        try {final String pathInfo = req.getPathInfo();
//
//            if (pathInfo == null || pathInfo.equals("/")) {
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                return;
//            }
//
//            final long id = Long.parseLong(pathInfo.substring(1));
//            if(!lectureRepository.containsKey(id)) {
//                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                return;
//            }
//
//            final Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);
//            lecture.setId(id);
//            lectureRepository.put(id, lecture);
//            resp.setStatus(HttpServletResponse.SC_OK);
//        } catch (JsonProcessingException e) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        } catch (Exception e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    /**
//     * 강의 삭제 API
//     * @param req
//     * @param resp
//     * @throws ServletException
//     * @throws IOException
//     */
//    @Override
//    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("doDelete called.");
//        final String pathInfo = req.getPathInfo();
//
//        if (pathInfo == null || pathInfo.equals("/")) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            return;
//        }
//
//        final long id = Long.parseLong(pathInfo.substring(1));
//        if(!lectureRepository.containsKey(id)) {
//            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            return;
//        }
//        lectureRepository.remove(id);
//        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
//    }
//}

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>강의 목록</title>
</head>
<body>
<a href="/lecture-registration.jsp">등록</a>
<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>이름</th>
            <th>가격</th>
            <th>관리</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="lecture" items="${lectures}">
            <tr>
                <td>${lecture.id}</td>
                <td>${lecture.name}</td>
                <td>${lecture.price}</td>
                <td>
                    <button onclick="editLecture(${lecture.id}, '${lecture.name}', ${lecture.price})">수정</button>
                    <button onclick="deleteLecture(${lecture.id})">삭제</button>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<script>
    function deleteLecture(id) {
        if (!confirm("삭제하시겠습니까?")) return;
        fetch("/lectures/" + id, {
            method: "DELETE"
        }).then(response => {
            if (response.ok) window.location.reload();
        });
    }

    function editLecture(id, name, price) {
        const newName = prompt("이름", name);
        const newPrice = prompt("가격", price);
        if (!newName || !newPrice) return;

        fetch("/lectures/" + id, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name: newName, price: parseInt(newPrice) })
        }).then(response => {
            if (response.ok) window.location.reload();
        });
    }
</script>
</body>
</html>

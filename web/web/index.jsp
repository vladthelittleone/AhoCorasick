<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <style>
    </style>
</head>
<body>

<div class='form'>
    <form action='http://localhost:8082/view' onsubmit="return validate()">
        <p><label for="patterns">Введите паттерны через пробел: </label><input name='patterns' id="patterns"></p>
        <p><label for="searchString">Введите строку, в которой осуществляется поиск: </label><input name='searchString' id="searchString"></p>
        <p><input type='submit'></p>
    </form>
</div>

<div class='clear'>
    <form action='http://localhost:8082/' onsubmit="return validate()">
        <p><input type='submit' value="Очистить"></p>
    </form>
</div>

<script>
    function validate ()
    {
        return document.getElementById('patterns').value != '' &&
                document.getElementById('searchString').value != '';
    }
</script>
</body>
</html>

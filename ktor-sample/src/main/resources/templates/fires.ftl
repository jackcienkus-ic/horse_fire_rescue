<html>
<body>
<h1>Fires:</h1>
<#list data.fires as name>
    <h2>Fire #${name?index + 1} is ${name}</h2>
</#list>
</body>
</html>


<html>
<body>
<h1>Fires:</h1>
<#list data as item>
    <h2>"${item.name}" is located at (${item.lat}, ${item.lng})</h2>
</#list>
</body>
</html>
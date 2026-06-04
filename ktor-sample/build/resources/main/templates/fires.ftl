<html>
<body>
<h1>Fires:</h1>
<#list data as item>
    <h2>Fire #${item?index + 1} is ${item.name} and is located at (${item.lat}, ${item.lng})</h2>
</#list>
</body>
</html>
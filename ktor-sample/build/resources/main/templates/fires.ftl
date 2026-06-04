<html>
<body>
<h1>Fires:</h1>
<#list data.fires as fire>
    <h2>Fire #${fire?index + 1} is ${fire} and is located at (${data.lat[fire?index]}, ${data.lng[fire?index ]})</h2>
</#list>
</body>
</html>

